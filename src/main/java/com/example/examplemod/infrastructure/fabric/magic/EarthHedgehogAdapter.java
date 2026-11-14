package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.earth.earth_hedgehog.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

/** Twelve staggered, persistent ground spires; explicit damage instead of suffocation. */
public record EarthHedgehogAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,EarthHedgehogState>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("earth_hedgehog_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,EarthHedgehogState>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,EarthHedgehogState> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static Vec3 offset(int index){var p=EarthHedgehogRules.offset(index);return new Vec3(p.x(),0,p.z());}
	public static boolean active(long age,int index){return EarthHedgehogRules.active(age,index);}
	public static boolean mayHit(int count){return EarthHedgehogRules.mayHit(count);}
	public static Vec3 target(ServerPlayer owner,MagicAim aim){
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();
		for(int i=0;i<32;i++){var a=from.add(aim.direction().scale(i));var b=from.add(aim.direction().scale(i+1));if(!SpellRaycastAdapter.loaded(level,new AABB(a,b)))return null;var hit=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));if(hit.getType()==HitResult.Type.BLOCK)return hit.getDirection()==net.minecraft.core.Direction.UP?hit.getLocation():null;}return null;
	}
	public static boolean cast(ServerPlayer owner,MagicAim aim){
		var at=target(owner,aim);if(at==null)return false;var level=(ServerLevel)owner.level();var bases=new ArrayList<BlockPos>();
		for(int i=0;i<12;i++){var candidate=BlockPos.containing(at.add(offset(i)));BlockPos base=null;
			for(int dy=3;dy>=-3;dy--){var p=candidate.offset(0,dy,0);if(!level.hasChunkAt(p)||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,p)||!level.getBlockState(p.below()).isFaceSturdy(level,p.below(),net.minecraft.core.Direction.UP))continue;boolean free=true;for(int y=0;y<3;y++)if(!level.getBlockState(p.above(y)).getCollisionShape(level,p.above(y)).isEmpty())free=false;if(free){base=p;break;}}
			if(base==null)return false;bases.add(base);
		}
		if(new HashSet<>(bases).size()!=12)return false;
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);area.configure(owner,SpellDefinition.EARTH_HEDGEHOG,aim);area.setPos(at);area.setAttached(MagicAttachments.SPIKE_BASES,List.copyOf(bases));var state=new EarthHedgehogState(level.getGameTime(),-1,Map.of());area.setAttached(MagicAttachments.SPIKES_STARTED,state.start());states(area).put(area,state);if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){states(area).remove(area);return false;}tick(area);return true;
	}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var state=states(area).get(area);
		EarthHedgehogTick.tick(new EarthHedgehogTick.World(){
			ServerPlayer owner;long age;
			public boolean statePresent(){return state!=null;}
			public boolean ownerPresent(){if(area.getOwner() instanceof ServerPlayer p){owner=p;return true;}return false;}
			public boolean alive(){return owner.isAlive();}
			public boolean sameLevel(){return owner.level()==level;}
			public boolean expired(){return level.getGameTime()-state.start()>=400;}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){states(area).remove(area);}
			public void discard(){area.discard();}
			public boolean begin(){age=level.getGameTime()-state.start();return state.begin(age);}
			public void erupt(){var bases=area.getAttachedOrCreate(MagicAttachments.SPIKE_BASES);
		for(int i=0;i<bases.size();i++){var pos=bases.get(i);if(!EarthHedgehogRules.erupt(age,i)||!level.hasChunkAt(pos))continue;var at=Vec3.atBottomCenterOf(pos);
			for(var e:level.getEntities(area,new AABB(pos).expandTowards(0,2,0),e->e instanceof LivingEntity&&e.isAlive())){var target=(LivingEntity)e;var key=e.getUUID().toString();int count=state.hits().getOrDefault(key,0);if(!mayHit(count)||(target!=owner&&!MagicEffects.allowed(owner,target))||!SpellVisibilityAdapter.visible(level,at.add(0,.3,0),target,area))continue;state.claim(key,count);if(target.hurtServer(level,MagicDamage.source(area,SpellDefinition.EARTH_HEDGEHOG),6))SpellHooks.afterHit(area,target);}
			SpellVisuals.burst(level,SpellDefinition.EARTH_HEDGEHOG,at,new Vec3(0,1,0));level.sendParticles(new net.minecraft.core.particles.BlockParticleOption(net.minecraft.core.particles.ParticleTypes.BLOCK,net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState()),at.x,at.y+.1,at.z,18,.5,.18,.5,.09);
		}
			}
		});
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("SpikesStart",s.start());out.putLong("SpikesProcessed",s.processed());out.store("SpikesBases",BlockPos.CODEC.listOf(),area.getAttachedOrCreate(MagicAttachments.SPIKE_BASES));out.store("SpikesHits",Codec.unboundedMap(Codec.STRING,Codec.INT),s.hits());}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new EarthHedgehogState(in.getLongOr("SpikesStart",Long.MIN_VALUE/2),in.getLongOr("SpikesProcessed",0),in.read("SpikesHits",Codec.unboundedMap(Codec.STRING,Codec.INT)).orElse(Map.of()));area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());area.setAttached(MagicAttachments.SPIKE_BASES,in.read("SpikesBases",BlockPos.CODEC.listOf()).orElse(List.of()));states(area).put(area,s);}
}
