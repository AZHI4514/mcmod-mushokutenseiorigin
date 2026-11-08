package com.example.examplemod.magic;

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
public final class PhaseTwoEntity extends Projectile {
	private static final EntityDataAccessor<String> SPELL=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Float> YAW=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PITCH=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.FLOAT);
	private final HashSet<UUID> hit=new HashSet<>();
	private int age, remaining;
	private double traveled;
	private Vec3 anchor=Vec3.ZERO;
	private boolean renewable=true;
	private float capacity=16;
	public PhaseTwoEntity(EntityType<? extends PhaseTwoEntity> type,Level level) { super(type,level); }
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
		if(isRemoved()||!renewable||level()!=owner.level()||owner.position().distanceToSqr(anchor)>1.0E-6) return false;
		remaining=MagicRules.extend(remaining,160); capacity=16; return true;
	}
	public void absorb(float amount) {
		capacity=MagicRules.shieldAfter(capacity,amount);
		if(level() instanceof ServerLevel server) SpellVisuals.burst(server,spell(),center(),direction());
		if(capacity<=0) discard();
	}
	public boolean ray() { return false; }
	public boolean stationary() { return switch(spell().id()) { case "ice_lance","flame_pillar","waterfall","physical_shield" -> true; default -> false; }; }
	public boolean shield() { return spell()==SpellDefinition.PHYSICAL_SHIELD; }
	public double width() { return switch(spell().id()) { case "icicle_edge","flood_flush","physical_shield","waterfall","earth_pillar" -> 3; case "flame_pillar" -> 2; case "ice_lance" -> 1; default -> 0.25; }; }
	public double height() { return switch(spell().id()) { case "waterfall","flame_pillar" -> 4; case "physical_shield","earth_pillar","ice_lance" -> 3; case "icicle_edge","flood_flush" -> 1; default -> 0.25; }; }
	public double depth() { return switch(spell().id()) { case "earth_pillar" -> 3; case "flame_pillar" -> 2; case "ice_lance","flood_flush" -> 1; case "icicle_edge" -> 1; case "physical_shield","waterfall" -> 0.2; default -> 0.5; }; }
	private double speed() { return fallingRock()?0.5:spell().speed(); }
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
		if(direct instanceof Projectile projectile&&MagicAreas.isMagic(projectile))return false;
		if(!(direct instanceof LivingEntity)&&!(direct instanceof Projectile))return false;
		absorb(amount);return true;
	}
	@Override public boolean isPushable() { return false; }
	@Override public boolean shouldBeSaved() { return !shield(); }
	@Override protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);output.putString("Spell",spell().id());output.putInt("MagicAge",age);output.putInt("Remaining",remaining);output.putFloat("AimYaw",entityData.get(YAW));output.putFloat("AimPitch",entityData.get(PITCH));MagicFlight.saveHits(output,hit);
	}
	@Override protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);var id=input.getStringOr("Spell","wind_slice");var definition=SpellDefinition.find(id);
		if(definition==null||definition.execution()!=SpellDefinition.Execution.PHASE_TWO)id="wind_slice";
		entityData.set(SPELL,id);entityData.set(YAW,input.getFloatOr("AimYaw",0));entityData.set(PITCH,input.getFloatOr("AimPitch",0));
		age=Math.max(0,input.getIntOr("MagicAge",0));remaining=Math.max(0,input.getIntOr("Remaining",spell().motion()==SpellDefinition.Motion.CONSTANT?spell().flightTicks():spell().lifetimeTicks()));MagicFlight.loadHits(input,hit);
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
		if(getY()<level.getMinY()){discard();return;}
		if(!PhaseTwoSpells.loaded(level,getBoundingBox()))return;
		var owner=getOwner() instanceof ServerPlayer player?player:null;
		if(stationary()&&owner==null)return;
		if(!stationary()&&!PhaseTwoSpells.loaded(level,getBoundingBox().expandTowards(direction().scale(speed()))))return;
		if(!fallingRock()&&age>=remaining){discard();return;}
		++age;
		if(shield()) { if(owner.position().distanceToSqr(anchor)>1.0E-6) renewable=false; return; }
		if(stationary()) { stationaryTick(owner,level); return; }
		if(fallingRock()){fall(owner,level);return;}
		fly(owner,level);
	}
	private void stationaryTick(ServerPlayer owner,ServerLevel level) {
		if(spell()==SpellDefinition.WATERFALL) {
			for(var target:level.getEntities(this,getBoundingBox(),e->e!=owner&&!e.isSpectator())) {
				if(!volume().intersects(target.getBoundingBox(),center())) continue;
				if(target instanceof LivingEntity living&&!MagicEffects.allowed(owner,living)) continue;
				target.clearFire(); target.setDeltaMovement(target.getDeltaMovement().multiply(0.8,0.8,0.8)); target.hurtMarked=true;
				if(target instanceof LivingEntity living) living.addEffect(new net.minecraft.world.effect.MobEffectInstance(MagicEffects.WET,spell().statusTicks(),0),owner);
			}
			return;
		}
		if(spell()==SpellDefinition.ICE_LANCE&&age!=1) return;
		for(var entity:level.getEntities(this,getBoundingBox(),e->e instanceof LivingEntity)) {
			var living=(LivingEntity)entity;
			if(!volume().intersects(living.getBoundingBox(),center())||!IntermediateSpells.visible(level,position().add(0,0.1,0),living,this)) continue;
			// Fire pillar tracks individual contacts rather than a global one-tick pulse.
			if(spell()==SpellDefinition.FLAME_PILLAR) {
				if(!PhaseTwoRuntime.contactReady(getUUID(),living.getUUID(),level.getGameTime())) continue;
			}
			PhaseTwoSpells.damage(this,living);
		}
	}
	private void fall(ServerPlayer owner,ServerLevel level) {
		var from=center();var motion=new Vec3(0,-1,0);double distance=speed();
		var body=new SpellSweep(new Vec3(0,0,1),3,3,3);
		var bounds=body.bounds(from).minmax(body.bounds(from.add(0,-distance,0)));
		if(!PhaseTwoSpells.loaded(level,bounds))return;
		double stop=MagicFlight.fluidHit(this,body,from,motion.scale(distance));
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			var state=level.getBlockState(pos);
			if(!state.isAir()&&state.getFluidState().isEmpty())stop=Math.min(stop,body.contact(new AABB(pos),from,motion,distance));
		}
		for(var target:level.getEntities(this,bounds,e->e instanceof LivingEntity&&MagicEffects.allowed(getOwner(),(LivingEntity)e))) {
			double contact=body.contact(target.getBoundingBox(),from,motion,distance);
			if(contact<=Math.min(distance,stop)&&hit.add(target.getUUID()))PhaseTwoSpells.damage(this,(LivingEntity)target);
		}
		if(stop<=distance) {
			setPos(position().add(0,-Math.max(0,stop),0));updateBounds();
			if(owner!=null)PhaseTwoSpells.permanentStone(owner,BlockPos.containing(getX(),Math.ceil(getY()-1.0E-6),getZ()));
			discard();return;
		}
		setPos(position().add(0,-distance,0));updateBounds();
	}
	public void keepAlive() { remaining=age+12; }

	private void explode(){ explode(false); }
	private void explode(boolean keepFlying){SpellVisuals.burst((ServerLevel)level(),spell(),position(),direction());((ServerLevel)level()).explode(this,getX(),getY(),getZ(),PhaseTwoRules.TNT_POWER,true,Level.ExplosionInteraction.TNT);if(!keepFlying)discard();}
	private void fly(ServerPlayer owner,ServerLevel level) {
		var from=center(); var motion=direction(); double distance=speed();
		if(distance<=0) { discard(); return; }
		var volume=volume(); var to=from.add(motion.scale(distance)); var bounds=volume.bounds(from).minmax(volume.bounds(to));
		if(!PhaseTwoSpells.loaded(level,bounds))return;
		double wall=Double.POSITIVE_INFINITY; BlockPos block=null; var shapes=new java.util.ArrayList<AABB>();
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			if(MagicFlight.passesWaterBlock(this,pos)) continue;
			var state=level.getBlockState(pos);var collisions=state.getCollisionShape(level,pos).toAabbs();
			for(var shape:collisions) {
				shapes.add(shape.move(pos)); double contact=volume.contact(shape.move(pos),from,motion,distance);
				if(contact<wall) { wall=contact; block=pos.immutable(); }
			}
		}
		if(spell()==SpellDefinition.ICICLE_EDGE) wall=new FlameSliceVolume(motion,-entityData.get(YAW)).blockingDistance(from,distance,shapes);
		double fluid=MagicFlight.fluidHit(this,volume,from,motion.scale(distance));
		var candidates=new java.util.ArrayList<LivingEntity>();
		for(var entity:level.getEntities(this,bounds,e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e)))
			if(!hit.contains(entity.getUUID())&&volume.contact(entity.getBoundingBox(),from,motion,distance)<Math.min(wall,fluid)) candidates.add((LivingEntity)entity);
		candidates.sort(java.util.Comparator.comparingDouble(t->volume.contact(t.getBoundingBox(),from,motion,distance)));
		for(var target:candidates) {
			double contact=volume.contact(target.getBoundingBox(),from,motion,distance); if(contact>distance || hit.contains(target.getUUID())) continue;
			hit.add(target.getUUID());
			if(spell()==SpellDefinition.EXPLOSION){setPos(from.add(motion.scale(contact)));explode(spell().piercing());if(!spell().piercing())return;continue;}
			if(spell()==SpellDefinition.WIND_SLICE) {
				for(var e:level.getEntities(this,target.getBoundingBox().inflate(3),e->e instanceof LivingEntity&&e.getBoundingBox().getCenter().distanceToSqr(target.getBoundingBox().getCenter())<=9 && MagicEffects.allowed(owner,(LivingEntity)e))) if(e==target || hit.add(e.getUUID())) PhaseTwoSpells.damage(this,(LivingEntity)e);
			} else PhaseTwoSpells.damage(this,target);
			if(!spell().piercing()) { SpellVisuals.burst(level,spell(),from.add(motion.scale(contact)),motion); discard(); return; }
		}
		if(fluid<=distance&&fluid<=wall){discard();return;}
		if(wall<=distance&&block!=null) {
			if(spell()==SpellDefinition.EXPLOSION){setPos(from.add(motion.scale(Math.max(0,wall-0.02))));explode();return;}

			discard(); return;
		}
		var next=to;
		setPos(next); traveled+=distance; updateBounds();
	}
}
