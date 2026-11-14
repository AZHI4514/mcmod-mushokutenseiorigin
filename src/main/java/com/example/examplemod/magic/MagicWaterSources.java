package com.example.examplemod.magic;

import java.util.*;
import com.example.examplemod.magic.spell.water.waterfall.ConnectedWater;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;

/** Real vanilla water; new sources are permanent, while legacy finite leases retain expiry handling. */
public final class MagicWaterSources extends SavedData {
	private record Lease(BlockPos pos,long expiry) {
		static final Codec<Lease> CODEC=RecordCodecBuilder.create(i->i.group(BlockPos.CODEC.fieldOf("pos").forGetter(Lease::pos),Codec.LONG.fieldOf("expiry").forGetter(Lease::expiry)).apply(i,Lease::new));
	}
	private final Map<BlockPos,Long> sources=new HashMap<>();
	private static final Codec<MagicWaterSources> CODEC=Lease.CODEC.listOf().xmap(list->{var data=new MagicWaterSources();for(var l:list)data.sources.put(l.pos,l.expiry);return data;},data->data.sources.entrySet().stream().map(e->new Lease(e.getKey(),e.getValue())).toList());
	private static final SavedDataType<MagicWaterSources> TYPE=new SavedDataType<>(ExampleMod.id("magic_water_sources"),MagicWaterSources::new,CODEC,null);
	private static MagicWaterSources data(ServerLevel level) { return level.getDataStorage().computeIfAbsent(TYPE); }
	public static void initialize() {
		ServerTickEvents.END_SERVER_TICK.register(server->{for(var level:server.getAllLevels()) {
			var data=data(level);var it=data.sources.entrySet().iterator();
			while(it.hasNext()) {var entry=it.next();if(entry.getValue()>level.getGameTime()||!level.hasChunkAt(entry.getKey()))continue;
				clear(level,entry.getKey());it.remove();data.setDirty();
			}
		}});
	}
	public static boolean place(ServerPlayer owner,BlockPos pos) {
		var level=(ServerLevel)owner.level();
		if(!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos)||!level.getBlockState(pos).isAir()||level.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.WATER_EVAPORATES,pos))return false;
		if(!level.setBlockAndUpdate(pos,Blocks.WATER.defaultBlockState()))return false;
		var data=data(level);data.sources.put(pos.immutable(),Long.MAX_VALUE);data.setDirty();return true;
	}
	public static boolean renew(ServerPlayer owner,BlockPos pos) {
		var level=(ServerLevel)owner.level();var data=data(level);
		if(!level.hasChunkAt(pos)||!level.getBlockState(pos).is(Blocks.WATER)||!level.getFluidState(pos).isSource())return false;
		data.sources.put(pos,Long.MAX_VALUE);data.setDirty();
		if(!ConnectedWater.refreshDue(level.getGameTime()))return true;
		var cells=ConnectedWater.find(new ConnectedWater.Cell(pos.getX(),pos.getY(),pos.getZ()),cell->{
			var at=new BlockPos(cell.x(),cell.y(),cell.z());
			return level.hasChunkAt(at)&&level.getBlockState(at).is(Blocks.WATER);
		});
		var connected=cells.stream().map(cell->new BlockPos(cell.x(),cell.y(),cell.z())).toList();
		for(var e:level.getEntities(owner,new AABB(pos).inflate(16),e->e instanceof LivingEntity&&e.isInWater()&&connected.stream().anyMatch(p->new AABB(p).intersects(e.getBoundingBox())))) {
			var living=(LivingEntity)e;if(MagicEffects.allowed(owner,living))living.addEffect(new MobEffectInstance(MagicEffects.WET,200,0),owner);
		}
		return true;
	}
	private static void clear(ServerLevel level,BlockPos pos) {
		if(level.getBlockState(pos).is(Blocks.WATER)&&level.getFluidState(pos).isSource())level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
	}
	public static void remove(ServerLevel level,BlockPos pos) {
		var data=data(level);if(!data.sources.containsKey(pos))return;
		if(level.hasChunkAt(pos)){clear(level,pos);data.sources.remove(pos);}else data.sources.put(pos,level.getGameTime());data.setDirty();
	}
}
