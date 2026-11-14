package com.example.examplemod.magic;

import java.util.*;
import com.example.examplemod.magic.domain.runtime.TickQuota;
import com.example.examplemod.magic.feature.terrain.TerrainLeaseScheduler;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.*;
import net.minecraft.world.phys.AABB;

/** Persistent bounded placement and restoration queue for expansion spells only. */
public final class TerrainLeaseRepository extends SavedData {
	private record Lease(BlockPos pos,BlockState oldState,BlockState placed,long expiry) {
		static final Codec<Lease> CODEC=RecordCodecBuilder.create(i->i.group(BlockPos.CODEC.fieldOf("pos").forGetter(Lease::pos),BlockState.CODEC.fieldOf("old").forGetter(Lease::oldState),BlockState.CODEC.fieldOf("placed").forGetter(Lease::placed),Codec.LONG.fieldOf("expiry").forGetter(Lease::expiry)).apply(i,Lease::new));
	}
	private record Pending(BlockPos pos,BlockState expected,BlockState placed,long expiry,String owner) {
		static final Codec<Pending> CODEC=RecordCodecBuilder.create(i->i.group(BlockPos.CODEC.fieldOf("pos").forGetter(Pending::pos),BlockState.CODEC.fieldOf("expected").forGetter(Pending::expected),BlockState.CODEC.fieldOf("placed").forGetter(Pending::placed),Codec.LONG.fieldOf("expiry").forGetter(Pending::expiry),Codec.STRING.fieldOf("owner").forGetter(Pending::owner)).apply(i,Pending::new));
	}
	private final List<Lease> leases=new ArrayList<>();
	private final TickQuota quota = new TickQuota(400);
	private final List<Pending> pending=new ArrayList<>();
	private static final Codec<TerrainLeaseRepository> CODEC=RecordCodecBuilder.create(i->i.group(Lease.CODEC.listOf().fieldOf("leases").forGetter(d->d.leases),Pending.CODEC.listOf().fieldOf("pending").forGetter(d->d.pending)).apply(i,(leases,pending)->{var d=new TerrainLeaseRepository();d.leases.addAll(leases);d.pending.addAll(pending);return d;}));
	private static final SavedDataType<TerrainLeaseRepository> TYPE=new SavedDataType<>(ExampleMod.id("expansion_terrain"),TerrainLeaseRepository::new,CODEC,null);
	public static boolean enqueue(ServerPlayer owner,Map<BlockPos,BlockState> requested,int duration,boolean allOrNothing){
		if(duration<=0||requested.isEmpty()||requested.size()>8192||requested.values().stream().anyMatch(state->!state.is(MagicTerrain.EXPANSION_DIRT)))return false;
		var level=(ServerLevel)owner.level();var data=level.getDataStorage().computeIfAbsent(TYPE);var accepted=new ArrayList<Pending>();
		var occupied=new HashSet<BlockPos>();for(var lease:data.leases)occupied.add(lease.pos);for(var job:data.pending)occupied.add(job.pos);
		for(var entry:requested.entrySet()){
			var pos=entry.getKey();boolean valid=com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos)&&!occupied.contains(pos);
			if(valid){var old=level.getBlockState(pos);valid=!old.hasBlockEntity()&&(old.isAir()||old.canBeReplaced())&&old.getFluidState().isEmpty()&&level.getEntities((net.minecraft.world.entity.Entity)null,new AABB(pos),e->!e.isSpectator()&&e.isAlive()).isEmpty();if(valid)accepted.add(new Pending(pos.immutable(),old,entry.getValue(),level.getGameTime()+duration,owner.getUUID().toString()));}
			if(!valid&&allOrNothing)return false;
		}
		if(accepted.isEmpty())return false;
		if(allOrNothing){if(!data.quota.reserve(level.getGameTime(),accepted.size()))return false;var made=new ArrayList<Pending>();for(var job:accepted){if(!managedSet(level,job.pos,job.placed)){for(var placed:made)managedSet(level,placed.pos,placed.expected);return false;}made.add(job);}for(var job:made){data.leases.add(new Lease(job.pos,job.expected,job.placed,job.expiry));formation(level,job.pos,job.placed);}}
		else data.pending.addAll(accepted);data.setDirty();return true;
	}

	/** Replace only exposed full support cubes; restoration retains the exact original state. */
	public static void scorch(ServerPlayer owner,net.minecraft.world.phys.Vec3 center){scorch(owner,center,4,64,161);}
	public static void scorch(ServerPlayer owner,net.minecraft.world.phys.Vec3 center,int radius,int maximum,int duration){
		var level=(ServerLevel)owner.level();var data=level.getDataStorage().computeIfAbsent(TYPE);int count=0;var base=BlockPos.containing(center.add(0,-.01,0));
		var occupied=new HashSet<BlockPos>();for(var lease:data.leases)occupied.add(lease.pos);for(var job:data.pending)occupied.add(job.pos);
		for(int x=-radius;x<=radius;x++)for(int z=-radius;z<=radius;z++){if(x*x+z*z>radius*radius||count>=maximum)continue;var pos=base.offset(x,0,z);if(!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos)||occupied.contains(pos))continue;var old=level.getBlockState(pos);if(old.hasBlockEntity()||!old.getFluidState().isEmpty()||old.getDestroySpeed(level,pos)<0||!old.isCollisionShapeFullBlock(level,pos)||!level.getBlockState(pos.above()).getCollisionShape(level,pos.above()).isEmpty())continue;
			data.pending.add(new Pending(pos,old,MagicTerrain.SCORCHED_SURFACE.defaultBlockState(),level.getGameTime()+duration,owner.getUUID().toString()));count++;data.setDirty();
		}
	}

	/** Freeze loaded exposed water cells in a sphere, preserving exact source/flow state. */
	public static void freezeSurface(ServerPlayer owner,net.minecraft.world.phys.Vec3 center,int radius,int maximum,int duration){
		var level=(ServerLevel)owner.level();var data=level.getDataStorage().computeIfAbsent(TYPE);var occupied=new HashSet<BlockPos>();for(var lease:data.leases)occupied.add(lease.pos);for(var job:data.pending)occupied.add(job.pos);int count=0;var base=BlockPos.containing(center);
		for(int x=-radius;x<=radius;x++)for(int z=-radius;z<=radius;z++)for(int y=radius;y>=-radius;y--){if(x*x+y*y+z*z>radius*radius||count>=maximum)continue;var pos=base.offset(x,y,z);if(occupied.contains(pos)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos)||!level.hasChunkAt(pos.above()))continue;var old=level.getBlockState(pos);if(!old.is(net.minecraft.world.level.block.Blocks.WATER)||level.getFluidState(pos.above()).is(net.minecraft.tags.FluidTags.WATER)||!level.getBlockState(pos.above()).getCollisionShape(level,pos.above()).isEmpty())continue;data.pending.add(new Pending(pos,old,MagicTerrain.EXPANSION_ICE.defaultBlockState(),level.getGameTime()+duration,owner.getUUID().toString()));occupied.add(pos);count++;data.setDirty();}
	}

	/** Wavefront ring, three water layers, one shared per-cast quota and common expiry. */
	public static int freezeOcean(ServerPlayer owner,net.minecraft.world.phys.Vec3 center,int inner,int outer,int maximum,long expiry) {
		if (maximum <= 0 || outer < 0 || expiry <= owner.level().getGameTime()) return 0;
		var level=(ServerLevel)owner.level();
		var data=level.getDataStorage().computeIfAbsent(TYPE);
		var occupied=new HashSet<BlockPos>();
		for(var lease:data.leases)occupied.add(lease.pos);
		for(var job:data.pending)occupied.add(job.pos);
		int count=0;
		var surface=BlockPos.containing(center).below();
		for(int x=-outer;x<=outer;x++)for(int z=-outer;z<=outer;z++) {
			int squared=x*x+z*z;
			if(squared>outer*outer || inner>=0 && squared<=inner*inner || count>=maximum)continue;
			var top=surface.offset(x,0,z);
			if(!level.hasChunkAt(top)||!level.getBlockState(top).is(net.minecraft.world.level.block.Blocks.WATER)
					||!level.getFluidState(top.above()).isEmpty()||!level.getBlockState(top.above()).getCollisionShape(level,top.above()).isEmpty())continue;
			for(int depth=0;depth<3 && count<maximum;depth++) {
				var pos=top.below(depth);
				if(!level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.WATER))break;
				if(occupied.contains(pos)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos))continue;
				var old=level.getBlockState(pos);
				data.pending.add(new Pending(pos,old,MagicTerrain.EXPANSION_ICE.defaultBlockState(),expiry,owner.getUUID().toString()));
				occupied.add(pos); count++;
			}
		}
		if(count>0)data.setDirty();
		return count;
	}


	/** Optional permanent surface work uses the same bounded persistent queue. No drops or block entities. */
	public static void excavate(ServerPlayer owner,net.minecraft.world.phys.Vec3 center,int radius,int depth,int maximum) {
		if(!MagicConfig.permanentTerrain())return;
		var level=(ServerLevel)owner.level();var data=level.getDataStorage().computeIfAbsent(TYPE);
		var occupied=new HashSet<BlockPos>();for(var lease:data.leases)occupied.add(lease.pos);for(var job:data.pending)occupied.add(job.pos);
		int count=0;var base=BlockPos.containing(center.add(0,-.05,0));
		for(int ring=0;ring<=radius&&count<maximum;ring++)for(int x=-ring;x<=ring&&count<maximum;x++)for(int z=-ring;z<=ring&&count<maximum;z++){
			if(x*x+z*z>radius*radius||Math.max(Math.abs(x),Math.abs(z))!=ring)continue;
			BlockPos top=null;
			for(int y=2;y>=-2;y--){var q=base.offset(x,y,z);if(!level.hasChunkAt(q))continue;var state=level.getBlockState(q);if(state.isCollisionShapeFullBlock(level,q)&&level.getBlockState(q.above()).getCollisionShape(level,q.above()).isEmpty()){top=q;break;}}
			if(top==null)continue;
			for(int down=0;down<depth&&count<maximum;down++){
				var q=top.below(down);if(occupied.contains(q)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,q))continue;
				var old=level.getBlockState(q);
				if(old.isAir()||old.hasBlockEntity()||!old.getFluidState().isEmpty()||old.getDestroySpeed(level,q)<0
					||old.is(MagicTerrain.EXPANSION_DIRT)||old.is(MagicTerrain.EXPANSION_ICE)||old.is(MagicTerrain.SCORCHED_SURFACE)
					||old.is(MagicTerrain.TEMPORARY_DIRT)||old.is(MagicTerrain.TEMPORARY_ICE))continue;
				data.pending.add(new Pending(q,old,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),Long.MAX_VALUE,owner.getUUID().toString()));
				occupied.add(q);count++;
			}
		}
		if(count>0)data.setDirty();
	}

	private static final ThreadLocal<Boolean> MUTATING=ThreadLocal.withInitial(()->false);
	private static boolean managedSet(ServerLevel level,BlockPos pos,BlockState state){
		boolean previous=MUTATING.get();MUTATING.set(true);try{return level.setBlockAndUpdate(pos,state);}finally{MUTATING.set(previous);}
	}
	public static void externalChange(ServerLevel level,BlockPos pos,BlockState before,BlockState after){
		if(MUTATING.get()||before==null||before.equals(after))return;
		var data=level.getDataStorage().computeIfAbsent(TYPE);
		boolean changed=data.leases.removeIf(l->l.pos.equals(pos));changed|=data.pending.removeIf(j->j.pos.equals(pos));if(changed)data.setDirty();
	}
	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var level : server.getAllLevels()) {
				var data = level.getDataStorage().computeIfAbsent(TYPE);
				TerrainLeaseScheduler.tick(level.getGameTime(), data.quota, data.leases, data.pending,
					new TerrainLeaseScheduler.Restoration<Lease>() {
						public boolean dueAndLoaded(Lease lease, long now) { return now >= lease.expiry && level.hasChunkAt(lease.pos); }
						public void restoreIfUnchanged(Lease lease) {
							if (level.getBlockState(lease.pos).equals(lease.placed)) managedSet(level, lease.pos, lease.oldState);
						}
					}, new TerrainLeaseScheduler.Assembly<Pending>() {
						public boolean expired(Pending job, long now) { return now >= job.expiry; }
						public boolean ready(Pending job) {
							if (!level.hasChunkAt(job.pos)) return false;
							var owner = server.getPlayerList().getPlayer(UUID.fromString(job.owner));
							return owner != null && owner.level() == level;
						}
						public boolean placeIfValid(Pending job) {
							var owner = server.getPlayerList().getPlayer(UUID.fromString(job.owner));
							if (!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner, job.pos) || !level.getBlockState(job.pos).equals(job.expected)
								|| !level.getEntities((net.minecraft.world.entity.Entity)null, new AABB(job.pos), e -> !e.isSpectator() && e.isAlive()).isEmpty()) return false;
							if (!managedSet(level, job.pos, job.placed)) return false;
							formation(level, job.pos, job.placed);
							if (!job.placed.isAir()) data.leases.add(new Lease(job.pos, job.expected, job.placed, job.expiry));
							data.setDirty();
							return true;
						}
					}, data::setDirty);
			}
		});
	}

	/** Sparse debris follows each newly assembled voxel instead of one central puff. */
	private static void formation(ServerLevel level,BlockPos pos,BlockState state){
		level.sendParticles(new net.minecraft.core.particles.BlockParticleOption(net.minecraft.core.particles.ParticleTypes.BLOCK,state),pos.getX()+.5,pos.getY()+.25,pos.getZ()+.5,7,.55,.24,.55,.065);
		level.sendParticles(SpellParticles.haze(state.is(MagicTerrain.SCORCHED_SURFACE)?SpellDefinition.MAGMA_GUSH:SpellDefinition.EARTH_FORTRESS),pos.getX()+.5,pos.getY()+.1,pos.getZ()+.5,2,.65,.12,.65,.015);
	}
	private TerrainLeaseRepository(){}
}
