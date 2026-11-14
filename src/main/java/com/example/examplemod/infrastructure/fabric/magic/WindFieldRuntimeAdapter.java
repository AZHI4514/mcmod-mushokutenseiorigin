package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.CumulonimbusAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SquallAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.field.*;

import java.util.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

/** Persistent bounded wind volumes; motion never carries blocks or dropped items. */
public final class WindFieldRuntimeAdapter {
	private record State(WindFieldState schedule,Vec3 direction) { }
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("wind_field_runtime_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,State> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static boolean handles(SpellDefinition s){return s!=null&&s.execution()==SpellDefinition.Execution.WIND_FIELD;}
	public static double radius(SpellDefinition s){return handles(s)?com.example.examplemod.bootstrap.WindFieldComposition.policy(s).radius():16;}
	public static double height(SpellDefinition s){return handles(s)?com.example.examplemod.bootstrap.WindFieldComposition.policy(s).height():16;}
	public static boolean contains(Vec3 p,double r,double h){return WindFieldRules.contains(p.x,p.y,p.z,r,h);}
	public static Vec3 target(ServerPlayer owner,SpellDefinition s,MagicAim aim){var level=(ServerLevel)owner.level();var from=owner.getEyePosition();for(int i=0;i<(int)s.range();i++){var a=from.add(aim.direction().scale(i));var b=from.add(aim.direction().scale(i+1));if(!SpellRaycastAdapter.loaded(level,new AABB(a,b)))return null;var hit=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));if(hit.getType()==HitResult.Type.BLOCK)return hit.getLocation().subtract(aim.direction().scale(.02));}return from.add(aim.direction().scale(s.range()));}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){var at=target(owner,spell,aim);if(at==null)return false;var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,spell,aim);area.setPos(at);long start=owner.level().getGameTime();var s=new State(new WindFieldState(start,start+spell.lifetimeTicks(),start),new Vec3(aim.direction().x,0,aim.direction().z).normalize());states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.schedule.start());if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){states(area).remove(area);return false;}tick(area);return true;}
	public static Vec3 impulse(Vec3 offset,Vec3 velocity,boolean tornado,boolean tempest,boolean player){var result=WindFieldRules.impulse(offset.x,offset.z,velocity.x,velocity.y,velocity.z,tornado,tempest,player);return new Vec3(result.x(),result.y(),result.z());}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area).get(area);long now=level.getGameTime();
		WindFieldTick.tick(new WindFieldTick.World(){
			ServerPlayer owner;boolean fire,tornado,tempest;WindFieldPolicy policy;
			public boolean statePresent(){return s!=null;}
			public boolean expired(){return now>=s.schedule.expiry();}
			public boolean ownerPresent(){if(area.getOwner() instanceof ServerPlayer p){owner=p;return true;}return false;}
			public boolean alive(){return owner.isAlive();}
			public boolean sameLevel(){return owner.level()==level;}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){states(area).remove(area);}
			public void discard(){area.discard();}
			public boolean move(){
				policy=com.example.examplemod.bootstrap.WindFieldComposition.policy(area.spell());fire=policy.flame();tornado=policy.moving();tempest=policy.tempest();
				if(tornado&&now>s.schedule.start()){var to=area.position().add(s.direction.scale(.1));if(!SpellRaycastAdapter.loaded(level,area.areaBounds().move(s.direction.scale(.1))))return false;area.setPos(to);}return true;
			}
			public boolean pulse(){return s.schedule.pulse(now);}
			public boolean suppressed(){return area.spell()==SpellDefinition.TORNADO&&level.getEntities(area,area.areaBounds(),e->e instanceof MagicAreaEntity other&&other.spell()==SpellDefinition.FLAME_TORNADO&&other.getOwner()==owner&&other.position().distanceToSqr(area.position())<=1).size()>0;}
			public void contacts(boolean pulse){
		for(var e:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
			var target=(LivingEntity)e;
			WindFieldContact.apply(pulse,new WindFieldContact.Target(){
				Vec3 offset;
				public boolean excluded(){return policy.excludesOwner()&&target==owner;}
				public boolean inside(){offset=target.getBoundingBox().getCenter().subtract(area.position());return contains(offset,policy.radius(),policy.height());}
				public boolean owner(){return target==owner;}
				public boolean allowed(){return MagicEffects.allowed(owner,target);}
				public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,Math.clamp(offset.y,.2,policy.height()),0),target,area);}
				public void pulse(){
					float damage=tempest?com.example.examplemod.magic.spell.wind.sky_tempest.SkyTempestPolicy.damage(offset.x,offset.z,area.spell().damage()):area.spell().damage();
					WindFieldPulse.apply(policy.flame(),policy.vacuum(),new WindFieldPulse.Target(){
						public boolean claim(){return MagicFieldCadence.claim(target,fire?"fire":"wind",20);}
						public boolean hurt(){return target.hurtServer(level,MagicDamage.source(area,area.spell()),damage);}
						public void fireEffects(){if(target==owner){if(target.getRandom().nextFloat()<.8F&&!target.fireImmune()&&!target.hasEffect(MobEffects.FIRE_RESISTANCE))target.igniteForTicks(500);}else MagicEffects.apply(owner,target,area.spell());}
						public void afterHit(){SpellHooks.afterHit(area,target);}
						public void slow(){target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,500,0),owner);}
					});
				}
				public boolean immune(){return MagicControl.immune(target);}
				public boolean moves(){return policy.movesTarget(pulse);}
				public void move(){var current=target.getDeltaMovement();var calculated=policy.impulse(offset.x,offset.z,current.x,current.y,current.z,target instanceof Player);var velocity=new Vec3(calculated.x(),calculated.y(),calculated.z());if(fire)velocity=new Vec3(velocity.x,com.example.examplemod.magic.spell.mixed.flame_tornado.FlameTornadoPolicy.vertical(target.getDeltaMovement().y,target instanceof Player),velocity.z);target.setDeltaMovement(velocity);target.hurtMarked=true;}
			});
		}
			}
			public void interact(boolean pulse){
		if(pulse&&policy.disperses())for(var e:level.getEntities(area,area.areaBounds(),e->e instanceof MagicAreaEntity other&&other.spell().rank()<7)){var other=(MagicAreaEntity)e;if(!area.areaBounds().intersects(other.areaBounds()))continue;if(policy.violent()&&SmokeFieldRuntimeAdapter.handles(other.spell()))SmokeFieldRuntimeAdapter.shorten(other,40);else if(policy.violent()&&com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.handles(other.spell())&&other.spell().element().equals("fire"))com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.shorten(other,40);else if(policy.violent()&&other.spell()==SpellDefinition.INFERNO)Inferno.shorten(other,40);else if(policy.violent()&&other.spell()==SpellDefinition.FLASH_OVER)FlashOver.shorten(other,40);else if(other.spell()==SpellDefinition.SQUALL)SquallAdapter.shorten(other,40);else if(other.spell()==SpellDefinition.CUMULONIMBUS)CumulonimbusAdapter.shorten(other,40);}
			}
		});
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("WindStart",s.schedule.start());out.putLong("WindExpiry",s.schedule.expiry());out.putLong("WindNext",s.schedule.next());out.store("WindDirection",Vec3.CODEC,s.direction);}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new State(new WindFieldState(in.getLongOr("WindStart",0),in.getLongOr("WindExpiry",0),in.getLongOr("WindNext",Long.MAX_VALUE)),in.read("WindDirection",Vec3.CODEC).orElse(Vec3.ZERO));states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.schedule.start());}
	private WindFieldRuntimeAdapter() { }
}
