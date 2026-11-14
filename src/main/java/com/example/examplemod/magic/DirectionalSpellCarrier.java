package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;

import java.util.HashSet;
import java.util.UUID;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;

/** Synced visual identity; only the server owns sweep hits, durations and shield capacity. */
public final class DirectionalSpellCarrier extends Projectile {
	private static final EntityDataAccessor<String> SPELL=SynchedEntityData.defineId(DirectionalSpellCarrier.class,EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Float> YAW=SynchedEntityData.defineId(DirectionalSpellCarrier.class,EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PITCH=SynchedEntityData.defineId(DirectionalSpellCarrier.class,EntityDataSerializers.FLOAT);
	private com.example.examplemod.magic.mechanic.projectile.ImpactHistory hit=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory();
	private int age, remaining;
	private double traveled;
	private Vec3 anchor=Vec3.ZERO;
	private boolean renewable=true;
	private float capacity=com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRules.CAPACITY;
	public DirectionalSpellCarrier(EntityType<? extends DirectionalSpellCarrier> type,Level level) { super(type,level); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { builder.define(SPELL,"wind_slice"); builder.define(YAW,0F); builder.define(PITCH,0F); }
	public SpellDefinition spell() { return SpellDefinition.find(entityData.get(SPELL)); }
	public Vec3 direction() { return Vec3.directionFromRotation(entityData.get(PITCH),entityData.get(YAW)); }
	public void configure(ServerPlayer owner,SpellDefinition spell,Vec3 pos,Vec3 direction) {
		setOwner(owner); entityData.set(SPELL,spell.id()); remaining=spell.motion()==SpellDefinition.Motion.CONSTANT?spell.flightTicks():spell.lifetimeTicks(); anchor=owner.position(); setDirection(direction); setPos(pos);
		if(!stationary()) setDeltaMovement(direction.scale(speed())); updateBounds();
	}
	public void setDirection(Vec3 direction) {
		entityData.set(YAW,(float)Math.toDegrees(Math.atan2(-direction.x,direction.z)));
		entityData.set(PITCH,(float)-Math.toDegrees(Math.atan2(direction.y,direction.horizontalDistance())));
	}
	public void breakMaintenance() { renewable=false; }
	public boolean renew(ServerPlayer owner) {
		if(!com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRules.canRenew(isRemoved(),renewable,level()==owner.level(),owner.position().distanceToSqr(anchor))) return false;
		remaining=com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRules.renewedDuration(remaining); capacity=com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRules.CAPACITY; return true;
	}
	public void absorb(float amount) {
		com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldAbsorb.apply(amount,new com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldAbsorb.Shield(){
			public float capacity(){return capacity;}
			public void capacity(float value){capacity=value;}
			public void show(){if(level() instanceof ServerLevel server)SpellVisuals.burst(server,spell(),center(),direction());}
			public void discard(){DirectionalSpellCarrier.this.discard();}
		});
	}
	public boolean ray() { return false; }
	private com.example.examplemod.magic.mechanic.projectile.EffectShape shape() {
		return com.example.examplemod.magic.mechanic.projectile.EffectShapeCatalog.of(spell().id());
	}
	public boolean stationary() { return shape().stationary(); }
	public boolean shield() { return spell()==SpellDefinition.PHYSICAL_SHIELD; }
	public double width() { return shape().width(); }
	public double height() { return shape().height(); }
	public double depth() { return shape().depth(); }
	private double speed() { return fallingRock()?com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarFall.SPEED:spell().speed(); }
	public boolean fallingRock() { return spell()==SpellDefinition.EARTH_PILLAR; }
	public SpellSweep volume() {
		return new SpellSweep(stationary()&&!shield()?Vec3.directionFromRotation(0,entityData.get(YAW)):direction(),width(),height(),depth());
	}
	public Vec3 center() { return (stationary()&&!shield())||(spell()==SpellDefinition.EARTH_PILLAR&&!ray())?position().add(0,height()/2,0):position(); }
	public void updateBounds() { setBoundingBox(volume().bounds(center())); }
	@Override public boolean canBeCollidedWith(Entity other) { return false; }
	@Override public boolean isPickable() { return shield()&&!isRemoved(); }
	@Override public boolean isAttackable() { return shield()&&!isRemoved(); }
	@Override public float getPickRadius() { return 0; }
	@Override public boolean hurtServer(ServerLevel level,net.minecraft.world.damagesource.DamageSource source,float amount) {
		if(!shield()||isRemoved()||!Float.isFinite(amount)||amount<=0)return false;
		var direct=source.getDirectEntity();
		if(direct instanceof Projectile projectile&&AreaRuntimeAdapter.isMagic(projectile))return false;
		if(!(direct instanceof LivingEntity)&&!(direct instanceof Projectile))return false;
		absorb(amount);return true;
	}
	@Override public boolean isPushable() { return false; }
	@Override public boolean shouldBeSaved() { return !shield(); }
	@Override protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);output.putString("Spell",spell().id());output.putInt("MagicAge",age);output.putInt("Remaining",remaining);output.putFloat("AimYaw",entityData.get(YAW));output.putFloat("AimPitch",entityData.get(PITCH));MagicFlight.saveHits(output,hit.snapshot());
	}
	@Override protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);var id=input.getStringOr("Spell","wind_slice");var definition=SpellDefinition.find(id);
		if(definition==null||definition.execution()!=SpellDefinition.Execution.PHASE_TWO)id="wind_slice";
		entityData.set(SPELL,id);entityData.set(YAW,input.getFloatOr("AimYaw",0));entityData.set(PITCH,input.getFloatOr("AimPitch",0));
		age=com.example.examplemod.magic.mechanic.projectile.SavedProjectileCounters.age(input.getIntOr("MagicAge",0));remaining=com.example.examplemod.magic.mechanic.projectile.SavedProjectileCounters.age(input.getIntOr("Remaining",spell().motion()==SpellDefinition.Motion.CONSTANT?spell().flightTicks():spell().lifetimeTicks()));var restoredHits=new HashSet<UUID>();MagicFlight.loadHits(input,restoredHits);hit=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory(restoredHits);
	}

	@Override public void tick() {
		super.tick(); updateBounds();
		if(!(level() instanceof ServerLevel level)) {
			if(!stationary()) {
				var motion=getDeltaMovement(); var point=position().subtract(motion.scale(0.35));
				var trail=SpellParticles.trail(spell());
				level().addParticle(trail,point.x,point.y,point.z,-motion.x*0.02,-motion.y*0.02,-motion.z*0.02);
				setPos(position().add(motion));
			} else if(spell()==SpellDefinition.FLAME_PILLAR && tickCount%3==0) {
				level().addParticle(SpellParticles.trail(spell()),getX(),getY()+tickCount%4*0.2,getZ(),0,0.04,0);
			} else if(spell()==SpellDefinition.WATERFALL && tickCount%3==0) {
				level().addParticle(SpellParticles.trail(spell()),getX(),getY()+Math.random()*3,getZ(),0,-0.04,0);
			}
			updateBounds(); return;
		}
		com.example.examplemod.magic.mechanic.projectile.CarrierServerTick.apply(
				new com.example.examplemod.infrastructure.fabric.magic.CarrierTickAdapter(this,level,age,remaining,anchor,
						this::speed,()->++age,()->renewable=false,
						owner->stationaryTick(owner,level),owner->fall(owner,level),owner->fly(owner,level)));
	}

	private void stationaryTick(ServerPlayer owner,ServerLevel level) {
		com.example.examplemod.infrastructure.fabric.magic.StationaryCarrierComposition.apply(this,owner,level,entityData.get(YAW),age);
	}
	private void fall(ServerPlayer owner,ServerLevel level) {
		new com.example.examplemod.infrastructure.fabric.magic.EarthPillarFallAdapter(this,owner,level,hit,this::speed,entityData.get(YAW)).apply();
	}
	public void keepAlive() { remaining=age+12; }
	private void fly(ServerPlayer owner,ServerLevel level) {
		new com.example.examplemod.infrastructure.fabric.magic.CarrierFlightAdapter(this,owner,level,hit,this::speed,entityData.get(YAW),value->traveled+=value).apply();
	}
}
