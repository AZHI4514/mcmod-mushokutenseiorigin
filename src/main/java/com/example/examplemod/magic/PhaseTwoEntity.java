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
	private static final EntityDataAccessor<Boolean> RAY=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RESTING=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> SPELL=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Float> YAW=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> PITCH=SynchedEntityData.defineId(PhaseTwoEntity.class,EntityDataSerializers.FLOAT);
	private final HashSet<UUID> hit=new HashSet<>();
	private int age, remaining;
	private double traveled;
	private Vec3 anchor=Vec3.ZERO;
	private boolean renewable=true, areaHit;
	private float capacity=16;
	public PhaseTwoEntity(EntityType<? extends PhaseTwoEntity> type,Level level) { super(type,level); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { builder.define(RAY,false);builder.define(RESTING,false); builder.define(SPELL,"wind_slice"); builder.define(YAW,0F); builder.define(PITCH,0F); }
	public SpellDefinition spell() { return SpellDefinition.find(entityData.get(SPELL)); }
	public Vec3 direction() { return Vec3.directionFromRotation(entityData.get(PITCH),entityData.get(YAW)); }
	public void configure(ServerPlayer owner,SpellDefinition spell,Vec3 pos,Vec3 direction) {
		setOwner(owner); entityData.set(SPELL,spell.id()); remaining=spell.lifetimeTicks(); anchor=owner.position(); setDirection(direction); setPos(pos);
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
	public void absorb(float amount) { capacity=MagicRules.shieldAfter(capacity,amount); if(capacity<=0) discard(); }
	public boolean ray() {return entityData.get(RAY);}
	public void makeRay() {entityData.set(RAY,true);remaining=100;setDeltaMovement(direction().scale(2));updateBounds();}
	public boolean stationary() { if(ray())return false;return switch(spell().id()) { case "ice_lance","flame_pillar","waterfall","physical_shield" -> true; default -> false; }; }
	public boolean shield() { return spell()==SpellDefinition.PHYSICAL_SHIELD; }
	public double width() { if(ray())return 0.12;return switch(spell().id()) { case "icicle_edge","flood_flush","physical_shield","waterfall","earth_pillar" -> 3; case "flame_pillar" -> 2; case "ice_lance" -> 1; default -> 0.25; }; }
	public double height() { if(ray())return 0.12;return switch(spell().id()) { case "waterfall","flame_pillar" -> 4; case "physical_shield","earth_pillar","ice_lance" -> 3; case "icicle_edge","flood_flush" -> 1; default -> 0.25; }; }
	public double depth() { if(ray())return 0.12;return switch(spell().id()) { case "earth_pillar" -> 3; case "flame_pillar" -> 2; case "ice_lance","flood_flush" -> 1; case "icicle_edge" -> 1; case "physical_shield","waterfall" -> 0.2; default -> 0.5; }; }
	private double speed() { return switch(spell().id()) { case "wind_slice" -> 2; case "earth_pillar","flood_flush" -> 0.5; default -> 1.5; }; }
	public SpellSweep volume() {
		return new SpellSweep(stationary()&&!shield()?Vec3.directionFromRotation(0,entityData.get(YAW)):direction(),width(),height(),depth());
	}
	public Vec3 center() { return (stationary()&&!shield())||(spell()==SpellDefinition.EARTH_PILLAR&&!ray())?position().add(0,height()/2,0):position(); }
	public void updateBounds() { setBoundingBox(volume().bounds(center())); }
	@Override public boolean canBeCollidedWith(Entity other) { return spell()==SpellDefinition.EARTH_PILLAR&&entityData.get(RESTING)&&(other==null||!(other instanceof Projectile)&&!other.isSpectator()); }
	@Override public boolean canCollideWith(Entity other) { return canBeCollidedWith(other); }
	@Override public boolean isPushable() { return false; }
	@Override public boolean shouldBeSaved() { return entityData.get(RESTING); }
	@Override protected void addAdditionalSaveData(ValueOutput output) { super.addAdditionalSaveData(output); output.putBoolean("RestingRock",entityData.get(RESTING)); }
	@Override protected void readAdditionalSaveData(ValueInput input) { super.readAdditionalSaveData(input); remaining=0; if(input.getBooleanOr("RestingRock",false)){entityData.set(SPELL,"earth_pillar");entityData.set(RESTING,true);setDirection(new Vec3(0,-1,0));} }
	@Override public void tick() {
		super.tick(); updateBounds();
		if(entityData.get(RESTING)) {
			setDeltaMovement(Vec3.ZERO);
			if(level() instanceof ServerLevel server&&server.getGameTime()%20==0&&getOwner() instanceof ServerPlayer owner&&PhaseTwoSpells.loaded(server,getBoundingBox())&&PhaseTwoSpells.permanentStone(owner,BlockPos.containing(position()))) discard();
			return;
		}
		if(!(level() instanceof ServerLevel level)) { if(!stationary()) setPos(position().add(getDeltaMovement())); updateBounds(); return; }
		if(!(getOwner() instanceof ServerPlayer owner)||owner.isRemoved()||!owner.isAlive()||owner.level()!=level||!PhaseTwoSpells.loaded(level,getBoundingBox())) { discard(); return; }
		if(getY()<level.getMinY()-16) {discard();return;}
		if(ray()){rayTick(owner,level);return;}
		if(spell()!=SpellDefinition.EARTH_PILLAR&&++age>remaining) { discard(); return; }
		if(shield()) { if(owner.position().distanceToSqr(anchor)>1.0E-6) renewable=false; return; }
		if(stationary()) { stationaryTick(owner,level); return; }
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
	private void rest(Vec3 bottom) {entityData.set(RESTING,true);setDeltaMovement(Vec3.ZERO);setPos(bottom);updateBounds();}
	public void keepAlive() { remaining=age+12; }
	private void rayTick(ServerPlayer owner,ServerLevel level) {
		double distance=Math.min(2,spell().range()-traveled);if(distance<=0){discard();return;}
		var next=position().add(direction().scale(distance));if(!PhaseTwoSpells.loaded(level,new AABB(position(),next).inflate(0.1))){discard();return;}
		var contact=PhaseTwoSpells.traceFrom(owner,position(),direction(),distance);
		var end=contact==null?next:contact.point();var delta=end.subtract(position());
		for(double t=0;t<=delta.length();t+=0.2){var p=position().add(direction().scale(t));level.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,p.x,p.y,p.z,1,0,0,0,0);}
		if(contact!=null){PhaseTwoSpells.rayImpact(owner,this,contact);discard();return;}
		setPos(next);traveled+=distance;updateBounds();if(traveled>=spell().range())discard();
	}

	private void fly(ServerPlayer owner,ServerLevel level) {
		var from=center(); var motion=direction(); double distance=Math.min(speed(),spell()==SpellDefinition.EARTH_PILLAR?32:spell().range()-traveled);
		if(distance<=0) { discard(); return; }
		var volume=volume(); var to=from.add(motion.scale(distance)); var bounds=volume.bounds(from).minmax(volume.bounds(to));
		if(!PhaseTwoSpells.loaded(level,bounds)) { discard(); return; }
		double wall=Double.POSITIVE_INFINITY; BlockPos block=null; var shapes=new java.util.ArrayList<AABB>();
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			var state=level.getBlockState(pos);var collisions=spell()==SpellDefinition.EARTH_PILLAR&&!state.isAir()&&state.getFluidState().isEmpty()?java.util.List.of(new AABB(0,0,0,1,1,1)):state.getCollisionShape(level,pos).toAabbs();
			for(var shape:collisions) {
				shapes.add(shape.move(pos)); double contact=volume.contact(shape.move(pos),from,motion,distance);
				if(contact<wall) { wall=contact; block=pos.immutable(); }
			}
		}
		if(spell()==SpellDefinition.ICICLE_EDGE) wall=new FlameSliceVolume(motion,-entityData.get(YAW)).blockingDistance(from,distance,shapes);
		var candidates=new java.util.ArrayList<LivingEntity>();
		for(var entity:level.getEntities(this,bounds,e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e)))
			if(!hit.contains(entity.getUUID())&&volume.contact(entity.getBoundingBox(),from,motion,distance)<wall) candidates.add((LivingEntity)entity);
		candidates.sort(java.util.Comparator.comparingDouble(t->volume.contact(t.getBoundingBox(),from,motion,distance)));
		for(var target:candidates) {
			double contact=volume.contact(target.getBoundingBox(),from,motion,distance); if(contact>distance) continue;
			hit.add(target.getUUID());
			if(spell()==SpellDefinition.EARTH_PILLAR) { PhaseTwoSpells.damage(this,target); areaHit=true;
			}
			else if(spell()==SpellDefinition.WIND_SLICE) {
				for(var e:level.getEntities(this,target.getBoundingBox().inflate(3),e->e instanceof LivingEntity&&e.getBoundingBox().getCenter().distanceToSqr(target.getBoundingBox().getCenter())<=9)) PhaseTwoSpells.damage(this,(LivingEntity)e);
			} else PhaseTwoSpells.damage(this,target);
			if(spell()==SpellDefinition.WIND_SLICE||spell()==SpellDefinition.ICICLE_BREAK) { discard(); return; }
		}
		if(wall<=distance&&block!=null) {
			if(spell()==SpellDefinition.EARTH_PILLAR) {
				var impact=from.add(motion.scale(wall)); if(!areaHit) PhaseTwoSpells.areaDamage(this,impact,1.5);
				var landing=BlockPos.containing(impact.x,block.getY()+1,impact.z);
				if(!PhaseTwoSpells.permanentStone(owner,landing)) {rest(Vec3.atBottomCenterOf(landing));return;}
			}
			discard(); return;
		}
		var next=position().add(motion.scale(distance));
		setPos(next); traveled+=distance; updateBounds(); if(spell()!=SpellDefinition.EARTH_PILLAR&&traveled>=spell().range()) discard();
	}
}
