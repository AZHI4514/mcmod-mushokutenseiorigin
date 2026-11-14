package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.cumulonimbus.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

/** Loaded local storm, with persisted expiry, strike cadence and victim cooldowns. */
public record CumulonimbusAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,CumulonimbusState>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("cumulonimbus_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,CumulonimbusState>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,CumulonimbusState> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static Vec3 target(ServerPlayer owner, MagicAim aim) {
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();var direction=aim.direction();
		for(int i=0;i<64;i++){
			var a=from.add(direction.scale(i));var b=from.add(direction.scale(i+1));
			if(!SpellRaycastAdapter.loaded(level,new net.minecraft.world.phys.AABB(a,b)))return null;
			var hit=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
			if(hit.getType()==net.minecraft.world.phys.HitResult.Type.BLOCK)return hit.getLocation().subtract(direction.scale(.02));
		}
		return from.add(direction.scale(64));
	}
	public static boolean cast(ServerPlayer owner,MagicAim aim){
		var at=target(owner,aim);if(at==null)return false;var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());
		area.configure(owner,SpellDefinition.CUMULONIMBUS,aim);area.setPos(at);var state=new CumulonimbusState(owner.level().getGameTime()+1200,owner.level().getGameTime(),Map.of());states(area).put(area,state);
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){states(area).remove(area);return false;}tick(area);return true;
	}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var state=states(area).get(area);long now=level.getGameTime();
		CumulonimbusTick.tick(new CumulonimbusTick.World(){
			ServerPlayer owner;final ArrayList<LivingEntity> exposed=new ArrayList<>();Vec3 strike;
			public boolean statePresent(){return state!=null;}
			public boolean expired(){return now>=state.expiry();}
			public boolean ownerPresent(){if(area.getOwner() instanceof ServerPlayer player){owner=player;return true;}return false;}
			public boolean alive(){return owner.isAlive();}
			public boolean sameLevel(){return owner.level()==level;}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){states(area).remove(area);}
			public void discard(){area.discard();}
			public boolean refreshNeeded(){return CumulonimbusRules.refresh(area.tickCount,now,state.next());}
			public void refreshExposure(){
		for(var e:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
			var target=(LivingEntity)e;double dx=e.getX()-area.getX(),dz=e.getZ()-area.getZ();
			if(!CumulonimbusRules.inside(dx,dz)||!level.hasChunkAt(target.blockPosition())||!level.canSeeSky(BlockPos.containing(target.getEyePosition())))continue;
			
			target.clearFire();target.addEffect(new MobEffectInstance(MagicEffects.WET,400,0),owner);exposed.add(target);
		}
			}
			public boolean strikeReady(){return CumulonimbusRules.strike(now,state.next(),state.expiry());}
			public void beginStrike(){state.beginStrike(now);}
			public boolean selectStrike(){
		var enemies=exposed.stream().filter(e->e instanceof net.minecraft.world.entity.monster.Enemy&&!state.victims().containsKey(e.getUUID().toString())).toList();strike=null;
		if(!enemies.isEmpty())strike=enemies.get(level.getRandom().nextInt(enemies.size())).position();
		else for(int i=0;i<16;i++){
			double a=level.getRandom().nextDouble()*Math.PI*2,r=24*Math.sqrt(level.getRandom().nextDouble());int x=(int)Math.floor(area.getX()+Math.cos(a)*r),z=(int)Math.floor(area.getZ()+Math.sin(a)*r);var column=new BlockPos(x,area.getBlockY(),z);if(!level.hasChunkAt(column))continue;
			int y=level.getHeight(Heightmap.Types.MOTION_BLOCKING,x,z);var point=new Vec3(x+.5,y+.05,z+.5);if(y<area.getY()||y>area.getY()+48||!level.canSeeSky(BlockPos.containing(point)))continue;
			if(!level.getEntities(area,new AABB(point,point).inflate(2),e->e instanceof LivingEntity&&e.isAlive()).isEmpty())continue;strike=point;break;
		}
				return strike!=null;
			}
			public void hitTargets(){
		for(var e:level.getEntities(area,new AABB(strike,strike).inflate(2),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
			var target=(LivingEntity)e;if(state.victims().containsKey(e.getUUID().toString())||target.getBoundingBox().getCenter().distanceToSqr(strike)>4||!level.canSeeSky(BlockPos.containing(target.getEyePosition())))continue;
			if(target!=owner&&!MagicEffects.allowed(owner,target)||!SpellVisibilityAdapter.visible(level,strike.add(0,.25,0),target,area))continue;
			state.claim(e.getUUID().toString(),now);if(MagicFieldCadence.claim(target,"water",80)&&target.hurtServer(level,MagicDamage.thunder(area,owner),8))SpellHooks.afterHit(area,target);
		}
			}
			public void burst(){SpellVisuals.burst(level,SpellDefinition.CUMULONIMBUS,strike,new Vec3(0,1,0));}
		});
	}
	public static void shorten(MagicAreaEntity area,int ticks){var s=states(area).get(area);if(s!=null)s.shorten(area.level().getGameTime(),ticks);}
	public static void save(MagicAreaEntity area,ValueOutput output){var s=states(area).get(area);if(s==null)return;output.putLong("StormExpiry",s.expiry());output.putLong("StormNext",s.next());output.store("StormVictims",com.mojang.serialization.Codec.unboundedMap(com.mojang.serialization.Codec.STRING,com.mojang.serialization.Codec.LONG),s.victims());}
	public static void load(MagicAreaEntity area,ValueInput input){var s=new CumulonimbusState(input.getLongOr("StormExpiry",0),input.getLongOr("StormNext",Long.MAX_VALUE),input.read("StormVictims",com.mojang.serialization.Codec.unboundedMap(com.mojang.serialization.Codec.STRING,com.mojang.serialization.Codec.LONG)).orElse(Map.of()));states(area).put(area,s);}
}
