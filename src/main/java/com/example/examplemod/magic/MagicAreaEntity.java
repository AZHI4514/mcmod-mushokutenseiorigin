package com.example.examplemod.magic;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** 短期法术实体不跨退出/区块重载复活；剩余时间与盾值只由服务端维护。 */
public final class MagicAreaEntity extends Projectile {
	private static final EntityDataAccessor<String> SPELL = SynchedEntityData.defineId(MagicAreaEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Float> PITCH=SynchedEntityData.defineId(MagicAreaEntity.class,EntityDataSerializers.FLOAT);
	private int remaining;
	private float capacity = 8;
	private Vec3 anchor = Vec3.ZERO;
	private int pulse;
	private final java.util.Map<java.util.UUID, Integer> fireContacts = new java.util.HashMap<>();
	private final java.util.Map<java.util.UUID, Integer> wetContacts = new java.util.HashMap<>();
	private boolean renewable = true;
	public MagicAreaEntity(EntityType<? extends MagicAreaEntity> type, Level level) { super(type, level); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { builder.define(PITCH,0F);builder.define(SPELL, "water_shield"); }
	public SpellDefinition spell() { return SpellDefinition.find(entityData.get(SPELL)); }
	public void configure(ServerPlayer owner, SpellDefinition spell, MagicAim aim) {
		setOwner(owner); entityData.set(SPELL, spell.id()); anchor = owner.position(); remaining = spell.lifetimeTicks();
		setYRot(aim.yaw());entityData.set(PITCH,aim.pitch());
		Vec3 offset = Vec3.directionFromRotation(0, aim.yaw()).scale(1.5);
		setPos(spell == SpellDefinition.WATER_SHIELD ? owner.getEyePosition().add(aim.direction()) : anchor);
	}
	public void breakMaintenance() { renewable = false; }
	public boolean canRenew(ServerPlayer owner) {
		return renewable && (!stationary() || owner.position().distanceToSqr(anchor) < 1.0E-6);
	}
	private boolean stationary() { return spell() == SpellDefinition.WATER_SHIELD || spell() == SpellDefinition.HEAT_ISLAND; }
	public void renew() { remaining = MagicRules.extend(remaining, spell().lifetimeTicks()); capacity = 8; }
	public void absorb(float damage) {
		capacity = MagicRules.shieldAfter(capacity, damage);
		if(level() instanceof ServerLevel server) SpellVisuals.burst(server,spell(),position(),wallDirection());
		if (capacity <= 0) discard();
	}
	public AABB areaBounds() {
		if (spell() == SpellDefinition.WATER_SHIELD) {
			return wallVolume().bounds(position());
		}
		if (spell() == SpellDefinition.BURNING_PLACE && getOwner() instanceof LivingEntity owner) return owner.getBoundingBox().inflate(.12);
		double r = spell() == SpellDefinition.HEAT_ISLAND ? 3 : spell() == SpellDefinition.BURNING_PLACE ? spell().range()+0.5 : 2;
		return new AABB(getX()-r,getY()-0.5,getZ()-r,getX()+r,getY()+2.5,getZ()+r);
	}
	public float wallPitch(){return entityData.get(PITCH);}
	public Vec3 wallDirection(){return Vec3.directionFromRotation(wallPitch(),getYRot());}
	public SpellSweep wallVolume(){return new SpellSweep(wallDirection(),3,2,0.3);}
	public boolean touchesWall(AABB box) {
		return wallVolume().intersects(box,position());
	}

	@Override public void tick() {
		super.tick();
		if (!(level() instanceof ServerLevel level)) {
			if(tickCount%4==0) {
				boolean warm=spell()==SpellDefinition.HEAT_ISLAND;
				double radius=warm?3:spell()==SpellDefinition.BURNING_PLACE?.45:0.85;
				if(spell()!=SpellDefinition.WATER_SHIELD) for(int i=0;i<2;i++) {
					double angle=tickCount*0.08+i*Math.PI;
					var particle=SpellParticles.trail(spell());
					level().addParticle(particle,getX()+Math.cos(angle)*radius,getY()+(warm?0.08:spell()==SpellDefinition.BURNING_PLACE?.2+(tickCount%20)/12.0:0.2),getZ()+Math.sin(angle)*radius,0,0.015,0);
				}
			}
			return;
		}
		if (!(getOwner() instanceof ServerPlayer owner) || owner.isRemoved() || !owner.isAlive()
				|| owner.level() != level || --remaining <= 0 || !level.hasChunkAt(blockPosition())) { discard(); return; }
		if (stationary() && owner.position().distanceToSqr(anchor) >= 1.0E-6) renewable = false;
		if (!stationary()) setPos(owner.position());
		++pulse;
		fireContacts.values().removeIf(last -> pulse-last >= 20);
		wetContacts.values().removeIf(last -> pulse-last >= 20);
		for (var entity : level.getEntities(this, areaBounds(), e -> e instanceof LivingEntity && e.isAlive() && !e.isSpectator())) {
			var target = (LivingEntity) entity;
			if (spell() == SpellDefinition.WATER_SHIELD && touchesWall(target.getBoundingBox())) {
				// 水幕无真实流体和气泡条；仅接触时施加水式移动阻力。
				var motion = target.getDeltaMovement(); target.setDeltaMovement(motion.multiply(0.8, 0.8, 0.8));
				target.hurtMarked = true;
				if (!wetContacts.containsKey(target.getUUID())) {
					wetContacts.put(target.getUUID(), pulse); MagicEffects.apply(owner, target, spell());
				}
			} else if (spell() == SpellDefinition.HEAT_ISLAND && insideRadius(target, 3)) {
				target.removeEffect(MagicEffects.WET); MagicEffects.warm(target, spell().statusTicks());
			} else if (spell() == SpellDefinition.BURNING_PLACE && !fireContacts.containsKey(target.getUUID()) && touchesFireRing(target)
					&& MagicEffects.allowed(owner, target) && visible(level, target)) {
				fireContacts.put(target.getUUID(), pulse);
				if (target.hurtServer(level, MagicDamage.source(this, spell()), spell().damage())) {
					MagicEffects.apply(owner, target, spell()); SpellHooks.afterHit.accept(this, target);
				}
			}
		}
	}
	private boolean touchesFireRing(LivingEntity target) {
		return getOwner() instanceof LivingEntity owner && MagicRules.touchesBody(owner.getBoundingBox(),target.getBoundingBox());
	}
	private boolean insideRadius(LivingEntity target, double radius) {
		double dx=target.getX()-getX(), dz=target.getZ()-getZ(); return dx*dx+dz*dz <= radius*radius;
	}
	private boolean visible(ServerLevel level, LivingEntity target) {
		return level.clip(new ClipContext(position().add(0,1,0), target.getBoundingBox().getCenter(),
				ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
	}
	@Override protected void addAdditionalSaveData(ValueOutput output) { super.addAdditionalSaveData(output); }
	@Override protected void readAdditionalSaveData(ValueInput input) { super.readAdditionalSaveData(input); remaining = 0; }
}
