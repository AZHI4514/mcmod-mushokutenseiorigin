package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.barrier.*;
import com.example.examplemod.magic.spell.barrier.dual_shield.DualShieldRules;
import com.example.examplemod.magic.spell.barrier.spirit_sealing_barrier.SpiritSealingBarrierRules;
import com.example.examplemod.magic.spell.barrier.confinement_barrier.ConfinementBarrierRules;

import java.util.WeakHashMap;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Finite barriers intercept crossings, not every attack near a decorative sphere. */
public final class FiniteBarrierRuntimeAdapter {
	private static final java.util.Map<SpellDefinition,FiniteBarrierEffect> EFFECTS=java.util.Map.of(
		SpellDefinition.DUAL_SHIELD,new DualShieldEffect(),SpellDefinition.CONFINEMENT_BARRIER,new ConfinementBarrierEffect(),SpellDefinition.SPIRIT_SEALING_BARRIER,new SpiritSealingBarrierEffect());
	private static FiniteBarrierEffect effect(SpellDefinition spell){return EFFECTS.get(spell);}
	static final class State {
		long expiry;
		BarrierCapacity capacity=new BarrierCapacity(0,24,24);
		int mode;
		java.util.UUID target;
		Vec3 normal=Vec3.ZERO;
	}
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,State>> INDEX =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("finite_barrier_runtime_adapter_runtime"),
				builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,State>()));
	}
	private static java.util.Map<MagicAreaEntity,State> states(net.minecraft.world.level.Level level) {
		return level.getAttachedOrCreate(Runtime.INDEX);
	}
	public static boolean handles(SpellDefinition s){return s!=null&&s.execution()==SpellDefinition.Execution.EXPANSION_BARRIER;}
	public static void initialize(){
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((target,source,amount)->{
			if(source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))return true;
			if(source.getEntity() instanceof LivingEntity attacker&&attacker.hasEffect(MagicEffects.SEALED))return false;
			if(PoweredBarrierRuntimeAdapter.blockDamage(target,source,amount))return false;
			var from=source.getSourcePosition();if(from==null||!(target.level() instanceof ServerLevel))return true;
			boolean magic=source.getDirectEntity() instanceof Projectile projectile&&AreaRuntimeAdapter.isMagic(projectile)
				|| source.typeHolder().is(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DAMAGE_TYPE,com.example.examplemod.ExampleMod.id("magic_holy")))
				|| source.typeHolder().is(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.DAMAGE_TYPE,com.example.examplemod.ExampleMod.id("magic_thunder")));
			var end=target.getBoundingBox().getCenter();
			for(var entry:states(target.level()).entrySet()){
				var area=entry.getKey();var s=entry.getValue();
				boolean dual=effect(area.spell()).plane();
				if(BarrierDamage.intercept(new BarrierDamage.Field(){
					public boolean active(){return !area.isRemoved()&&area.level()==target.level()&&s.expiry>target.level().getGameTime();}
					public boolean acceptsKind(){return effect(area.spell()).acceptsDamage(s.mode,magic);}
					public boolean crosses(){return FiniteBarrierRuntimeAdapter.crosses(area,s,from,end);}
					public boolean blocksDamage(){return effect(area.spell()).blocksDamage();}
					public boolean absorb(){return effect(area.spell()).absorb(s.capacity,magic,amount);}
					public void discard(){area.discard();}public void visual(){FiniteBarrierRuntimeAdapter.visual(area,s,true);}
				}))return false;
			}
			return true;
		});
	}
	public static Vec3 target(ServerPlayer owner,SpellDefinition spell,MagicAim aim){return aim.valid()?effect(spell).target(owner,spell,aim):null;}
	public static boolean cast(ServerPlayer owner,SpellDefinition spell,MagicAim aim){
		var at=target(owner,spell,aim);if(at==null)return false;
		LivingEntity victim=effect(spell).victim(owner,aim);
		if(victim!=null&&MagicControl.immune(victim)){victim.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.SLOWNESS,100,0),owner);return true;}
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,spell,aim);area.setPos(at);
		var s=new State();s.expiry=owner.level().getGameTime()+(ConfinementBarrierRules.lifetime(victim instanceof Player,spell.lifetimeTicks()));s.capacity=new BarrierCapacity(effect(spell).capacity(),24,24);s.mode=MaintainedFieldRuntimeAdapter.mode(owner,spell);s.normal=aim.direction();s.target=victim==null?null:victim.getUUID();
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area))return false;states(area.level()).put(area,s);visual(area,s,false);return true;
	}
	public static double radius(SpellDefinition spell){return effect(spell).radius();}
	public static AABB bounds(MagicAreaEntity area){
		if(effect(area.spell()).plane())return new SpellSweep(area.wallDirection(),4,4,.2).bounds(area.position());
		return new AABB(area.position(),area.position()).inflate(radius(area.spell()));
	}
	private static boolean inside(MagicAreaEntity area,Vec3 point){double r=radius(area.spell());return point.distanceToSqr(area.position())<r*r;}
	private static boolean crosses(MagicAreaEntity area,State state,Vec3 from,Vec3 to){
		if(effect(area.spell()).plane()){
			var v=to.subtract(from);double length=v.length();if(length<1E-8)return false;
			return new SpellSweep(state.normal,4,4,.2).contact(new AABB(from,from),area.position(),v.normalize().scale(-1),length)<=length;
		}
		return Double.isFinite(BarrierGeometry.sphere(area.position(),radius(area.spell()),false,from,to.subtract(from)));
	}
	public static Vec3 clipMovement(Entity entity,Vec3 movement){
		if(movement.lengthSqr()<1E-12||entity.isSpectator())return movement;
		for(var entry:states(entity.level()).entrySet()){
			var area=entry.getKey();var s=entry.getValue();
			double fraction=BarrierMovement.clip(new BarrierMovement.Field(){
				Vec3 center;
				public boolean active(){return !area.isRemoved()&&area.level()==entity.level()&&!effect(area.spell()).plane();}
				public boolean holdsBody(){return effect(area.spell()).holdsBody(entity);}
				public boolean crosses(){center=entity.getBoundingBox().getCenter();return FiniteBarrierRuntimeAdapter.crosses(area,s,center,center.add(movement));}
				public double crossing(){return BarrierGeometry.sphere(area.position(),radius(area.spell()),false,center,movement);}
				public void stopLiving(){if(entity instanceof LivingEntity living){living.setDeltaMovement(Vec3.ZERO);living.resetFallDistance();}}
			});
			if(!Double.isNaN(fraction))return movement.scale(fraction);
		}
		return movement;
	}
	public static boolean silenced(ServerPlayer caster){
		for(var entry:states(caster.level()).entrySet()){
			var area=entry.getKey();if(!area.isRemoved()&&area.level()==caster.level()&&effect(area.spell()).silences()
					&&inside(area,caster.getBoundingBox().getCenter()))return true;
		}
		return false;
	}
	public static boolean intercept(Projectile projectile){
		if(projectile instanceof MagicAreaEntity||projectile.isRemoved())return false;
		var from=projectile.position();var to=from.add(projectile.getDeltaMovement());
		for(var entry:states(projectile.level()).entrySet()){
			var area=entry.getKey();var s=entry.getValue();
			if(BarrierProjectile.intercept(new BarrierProjectile.Field(){
				boolean dual,magic;
				public boolean active(){return !area.isRemoved()&&area.level()==projectile.level();}
				public boolean crosses(){return FiniteBarrierRuntimeAdapter.crosses(area,s,from,to);}
				public boolean accepts(){dual=effect(area.spell()).plane();magic=AreaRuntimeAdapter.isMagic(projectile);return effect(area.spell()).acceptsProjectile(s.mode,magic);}
				public boolean absorb(){float incoming=MagicProjectileDamage.incoming(projectile,area);return effect(area.spell()).absorb(s.capacity,magic,incoming);}
				public boolean trident(){return projectile instanceof net.minecraft.world.entity.projectile.arrow.ThrownTrident;}
				public void reflect(){projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-.1));}public void discardProjectile(){projectile.discard();}public void discardField(){area.discard();}public void visual(){FiniteBarrierRuntimeAdapter.visual(area,s,true);}
			}))return true;
		}
		return false;
	}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area.level()).get(area);
		if(s==null||s.expiry<=level.getGameTime()||!(area.getOwner() instanceof ServerPlayer owner)||!owner.isAlive()||owner.level()!=level||!AreaRuntimeAdapter.adoptExpansion(owner,area)){states(area.level()).remove(area);area.discard();return;}
		effect(area.spell()).update(level,area,s);
		if(level.getGameTime()%4==0)visual(area,s,false);
	}
	private static void visual(MagicAreaEntity area,State s,boolean hit){
		if(!(area.level() instanceof ServerLevel level))return;
		if(level.players().stream().noneMatch(p->p.distanceToSqr(area.position())<96*96))return;
		boolean plane=effect(area.spell()).plane();
		var random=level.getRandom();
		for(int i=0;i<(hit?80:40);i++){
			Vec3 offset;
			if(plane){var sweep=new SpellSweep(s.normal,4,4,.2);offset=sweep.side.scale((random.nextDouble()-.5)*4).add(sweep.up.scale((random.nextDouble()-.5)*4));}
			else {double y=random.nextDouble()*2-1,a=random.nextDouble()*Math.PI*2,r=radius(area.spell());offset=new Vec3(Math.sqrt(1-y*y)*Math.cos(a)*r,y*r,Math.sqrt(1-y*y)*Math.sin(a)*r);}
			var at=area.position().add(offset);
			ParticlePacketAdapter.send(level,hit?SpellParticles.burst(area.spell()):SpellParticles.accent(area.spell()),at.x,at.y,at.z,1,.03,.03,.03,.008);
		}
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area.level()).get(area);if(s==null)return;out.putLong("BarrierExpiry",s.expiry);out.putFloat("BarrierCapacity",s.capacity.shell());out.putFloat("BarrierPhysical",s.capacity.physical());out.putFloat("BarrierMagical",s.capacity.magical());out.putInt("BarrierMode",s.mode);out.store("BarrierNormal",Vec3.CODEC,s.normal);if(s.target!=null)out.putString("BarrierTarget",s.target.toString());}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new State();s.expiry=in.getLongOr("BarrierExpiry",0);s.capacity=new BarrierCapacity(in.getFloatOr("BarrierCapacity",0),in.getFloatOr("BarrierPhysical",0),in.getFloatOr("BarrierMagical",0));s.mode=in.getIntOr("BarrierMode",0);s.normal=in.read("BarrierNormal",Vec3.CODEC).orElse(new Vec3(0,0,1));states(area.level()).put(area,s);}
	private FiniteBarrierRuntimeAdapter(){}
}
