package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;
import com.example.examplemod.ExampleMod;

/** Core-powered fortress and opt-in seal targets, with persistent core identity. */
public final class PoweredBarrierRuntimeAdapter {
	private static final java.util.Map<SpellDefinition,PoweredBarrierEffect> EFFECTS=java.util.Map.of(
		SpellDefinition.FORTRESS_BARRIER,new FortressBarrierEffect(),SpellDefinition.DEMON_GOD_SEALING_BARRIER,new DemonGodSealEffect());
	private static PoweredBarrierEffect effect(MagicAreaEntity area){return EFFECTS.get(area.spell());}
	static final class State {long expiry,next,dry;float capacity;List<UUID> cores=List.of();UUID target;}
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> INDEX =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("powered_barrier_runtime_adapter_runtime"),
				builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}
	private static java.util.Map<MagicAreaEntity,State> states(net.minecraft.world.level.Level level) {
		return level.getAttachedOrCreate(Runtime.INDEX);
	}
	private static final net.minecraft.tags.TagKey<EntityType<?>> SEALABLE=net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE,ExampleMod.id("sealable"));
	public static boolean handles(SpellDefinition s){return s!=null&&s.execution()==SpellDefinition.Execution.CORE_FIELD;}
	public static LivingEntity sealTarget(ServerPlayer owner,MagicAim aim){
		return SupportRayAdapter.trace(owner,aim,32,t->com.example.examplemod.magic.spell.barrier.demon_god_sealing_barrier.DemonGodSealRules.eligible(new com.example.examplemod.magic.spell.barrier.demon_god_sealing_barrier.DemonGodSealRules.Target(){public boolean player(){return t instanceof net.minecraft.world.entity.player.Player;}public float health(){return t.getHealth();}public float maxHealth(){return t.getMaxHealth();}public boolean tagged(){return t.getType().builtInRegistryHolder().is(SEALABLE);}}));
	}
	public static List<UUID> sealCores(ServerPlayer owner,MagicAim aim){
		var target=sealTarget(owner,aim);if(target==null)return List.of();
		var result=new ArrayList<UUID>();
		for(int i=0;i<5;i++){double a=i*Math.PI*2/5;var point=target.position().add(Math.cos(a)*12,0,Math.sin(a)*12);
			var core=MagicCores.nearest(owner,point,2);
			if(!MagicCores.valid(core)||MagicCores.energy(core)<400||result.contains(core.getUUID()))return List.of();result.add(core.getUUID());
		}
		return List.copyOf(result);
	}
	public static boolean canCast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		if(!aim.valid())return false;
		if(spell==SpellDefinition.SAINT_DRAGON_BATTLE_AURA)return com.example.examplemod.infrastructure.fabric.magic.DragonAuraAdapter.canCast(owner);
		if(spell==SpellDefinition.DEMON_GOD_SEALING_BARRIER)return sealCores(owner,aim).size()==5;
		return MagicCores.valid(MagicCores.nearest(owner,owner.position(),6));
	}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		if(!canCast(owner,spell,aim))return false;
		var level=(ServerLevel)owner.level();long now=level.getGameTime();
		if(spell==SpellDefinition.SAINT_DRAGON_BATTLE_AURA)return com.example.examplemod.infrastructure.fabric.magic.DragonAuraAdapter.cast(owner);
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);area.configure(owner,spell,aim);
		var s=new State();s.expiry=now+spell.lifetimeTicks();s.next=now+20;
		var effect=EFFECTS.get(spell);if(!effect.prepare(owner,aim,area,s))return false;
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area))return false;
		effect.committed(level,s);
		states(area.level()).put(area,s);tick(area);return true;
	}
	public static void tickOwner(ServerPlayer p){com.example.examplemod.infrastructure.fabric.magic.DragonAuraAdapter.tickOwner(p);}
	public static float auraDamage(LivingEntity t,net.minecraft.world.damagesource.DamageSource source,float amount){return com.example.examplemod.infrastructure.fabric.magic.DragonAuraAdapter.auraDamage(t,source,amount);}
	public static double radius(SpellDefinition spell){return EFFECTS.get(spell).radius();}
	public static AABB bounds(MagicAreaEntity area){double r=radius(area.spell());return new AABB(area.getX()-r,area.getY()-(effect(area).hemisphere()?0:r),area.getZ()-r,area.getX()+r,area.getY()+r,area.getZ()+r);}
	private static boolean inside(MagicAreaEntity area,Vec3 p){double r=radius(area.spell());return p.distanceToSqr(area.position())<r*r&&(!effect(area).hemisphere()||p.y>=area.getY());}
	public static boolean blockDamage(LivingEntity target,net.minecraft.world.damagesource.DamageSource source,float amount){
		var origin=source.getSourcePosition();if(origin==null||source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY))return false;
		for(var entry:states(target.level()).entrySet()){
			var area=entry.getKey();var s=entry.getValue();
			if(area.isRemoved()||area.level()!=target.level()||!Double.isFinite(BarrierGeometry.sphere(area.position(),radius(area.spell()),effect(area).hemisphere(),origin,target.getBoundingBox().getCenter().subtract(origin))))continue;
			if(area.spell()==SpellDefinition.DEMON_GOD_SEALING_BARRIER&&target.getUUID().equals(s.target))continue;
			s.capacity-=amount;if(s.capacity<=0)area.discard();return true;
		}return false;
	}
	public static Vec3 clipMovement(Entity entity,Vec3 movement){
		for(var entry:states(entity.level()).entrySet()){
			var area=entry.getKey();var s=entry.getValue();if(area.isRemoved()||area.level()!=entity.level()||entity.isSpectator())continue;
			if(effect(area).hemisphere()&&area.getOwner() instanceof ServerPlayer owner&&entity instanceof LivingEntity t&&SupportConsentAdapter.consented(owner,t))continue;
			var from=entity.getBoundingBox().getCenter();var to=from.add(movement);
			double low=BarrierGeometry.sphere(area.position(),radius(area.spell()),effect(area).hemisphere(),from,movement);if(!Double.isFinite(low))continue;
			return movement.scale(Math.max(0,low-.001));
		}return movement;
	}
	public static boolean intercept(net.minecraft.world.entity.projectile.Projectile projectile){
		if(projectile instanceof MagicAreaEntity)return false;
		for(var entry:states(projectile.level()).entrySet()){var area=entry.getKey();var s=entry.getValue();if(area.isRemoved()||area.level()!=projectile.level()||!Double.isFinite(BarrierGeometry.sphere(area.position(),radius(area.spell()),effect(area).hemisphere(),projectile.position(),projectile.getDeltaMovement())))continue;
			s.capacity-=MagicProjectileDamage.incoming(projectile,area);if(projectile instanceof net.minecraft.world.entity.projectile.arrow.ThrownTrident)projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-.1));else projectile.discard();if(s.capacity<=0)area.discard();return true;}return false;
	}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area.level()).get(area);long now=level.getGameTime();
		com.example.examplemod.magic.mechanic.barrier.PoweredBarrierTick.tick(new com.example.examplemod.magic.mechanic.barrier.PoweredBarrierTick.World(){
			public boolean statePresent(){return s!=null;}public boolean coreCountValid(){return s.cores.size()==effect(area).coreCount();}public boolean depleted(){return s.capacity<=0;}public boolean expired(){return now>=s.expiry;}
			public boolean ownerPresent(){return area.getOwner() instanceof ServerPlayer;}private ServerPlayer owner(){return (ServerPlayer)area.getOwner();}
			public boolean alive(){return owner().isAlive();}public boolean sameLevel(){return owner().level()==level;}public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner(),area);}
			public boolean coresValid(){for(var id:s.cores)if(!MagicCores.valid(level.getEntity(id)))return false;return true;}
			public boolean maintain(){return effect(area).maintain(level,area,s,owner(),now);}public void release(){PoweredBarrierRuntimeAdapter.release(area,s);}public void visual(){PoweredBarrierRuntimeAdapter.visual(level,area,now);}
		});
	}

	private static void visual(ServerLevel level,MagicAreaEntity area,long now){
		if(now%4==0){double r=radius(area.spell());var random=level.getRandom();for(int i=0;i<70;i++){
			double y=effect(area).hemisphere()?random.nextDouble():random.nextDouble()*2-1,a=random.nextDouble()*Math.PI*2;
			var p=area.position().add(Math.sqrt(1-y*y)*r*Math.cos(a),y*r,Math.sqrt(1-y*y)*r*Math.sin(a));
			ParticlePacketAdapter.send(level,SpellParticles.accent(area.spell()),p.x,p.y,p.z,1,.03,.03,.03,.004);
		}}
	}
	private static void release(MagicAreaEntity area,State s){
		if(s!=null&&s.target!=null&&area.level() instanceof ServerLevel level&&level.getEntity(s.target) instanceof LivingEntity target)target.removeEffect(MagicEffects.SEALED);
		states(area.level()).remove(area);area.discard();
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area.level()).get(area);if(s==null)return;out.putLong("CoreExpiry",s.expiry);out.putLong("CoreNext",s.next);out.putLong("CoreDry",s.dry);out.putFloat("CoreCapacity",s.capacity);out.putString("CoreIDs",String.join(",",s.cores.stream().map(UUID::toString).toList()));if(s.target!=null)out.putString("CoreTarget",s.target.toString());}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new State();s.expiry=in.getLongOr("CoreExpiry",0);s.next=in.getLongOr("CoreNext",Long.MAX_VALUE);s.dry=in.getLongOr("CoreDry",0);s.capacity=in.getFloatOr("CoreCapacity",0);var ids=new ArrayList<UUID>();for(var id:in.getStringOr("CoreIDs","").split(",")){try{ids.add(UUID.fromString(id));}catch(IllegalArgumentException ignored){}}s.cores=List.copyOf(ids);try{s.target=UUID.fromString(in.getStringOr("CoreTarget",""));}catch(IllegalArgumentException ignored){}states(area.level()).put(area,s);}
	public static void initialize(){}
	private PoweredBarrierRuntimeAdapter(){}
}
