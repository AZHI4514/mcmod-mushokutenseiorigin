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
	private int remaining;
	private float capacity = 8;
	private Vec3 anchor = Vec3.ZERO;
	private int pulse;
	private final java.util.Map<java.util.UUID, Integer> fireContacts = new java.util.HashMap<>();
	private final java.util.Map<java.util.UUID, Integer> wetContacts = new java.util.HashMap<>();
	private boolean renewable = true;
	public MagicAreaEntity(EntityType<? extends MagicAreaEntity> type, Level level) { super(type, level); }
	@Override protected void defineSynchedData(SynchedEntityData.Builder builder) { builder.define(SPELL, "water_shield"); }
	public SpellDefinition spell() { return SpellDefinition.find(entityData.get(SPELL)); }
	public void configure(ServerPlayer owner, SpellDefinition spell, MagicAim aim) {
		setOwner(owner); entityData.set(SPELL, spell.id()); anchor = owner.position(); remaining = spell.lifetimeTicks();
		setYRot(aim.yaw());
		Vec3 offset = Vec3.directionFromRotation(0, aim.yaw()).scale(1.5);
		setPos(spell == SpellDefinition.WATER_SHIELD ? anchor.add(offset) : anchor);
	}
	public void breakMaintenance() { renewable = false; }
	public boolean canRenew(ServerPlayer owner) {
		return renewable && (!stationary() || owner.position().distanceToSqr(anchor) < 1.0E-6);
	}
	private boolean stationary() { return spell() == SpellDefinition.WATER_SHIELD || spell() == SpellDefinition.HEAT_ISLAND; }
	public void renew() { remaining = MagicRules.extend(remaining, spell().lifetimeTicks()); capacity = 8; }
	public void absorb(float damage) { capacity = MagicRules.shieldAfter(capacity, damage); if (capacity <= 0) discard(); }
	public AABB areaBounds() {
		if (spell() == SpellDefinition.WATER_SHIELD) {
			double yaw = Math.toRadians(getYRot());
			double x = Math.abs(Math.cos(yaw)) * 1.5 + Math.abs(Math.sin(yaw)) * 0.15;
			double z = Math.abs(Math.sin(yaw)) * 1.5 + Math.abs(Math.cos(yaw)) * 0.15;
			return new AABB(getX()-x,getY(),getZ()-z,getX()+x,getY()+2,getZ()+z);
		}
		double r = spell() == SpellDefinition.HEAT_ISLAND ? 3 : spell() == SpellDefinition.BURNING_PLACE ? spell().range()+0.5 : 2;
		return new AABB(getX()-r,getY()-0.5,getZ()-r,getX()+r,getY()+2.5,getZ()+r);
	}
	public boolean touchesWall(AABB box) {
		return new MagicWallVolume(Vec3.directionFromRotation(0,getYRot()),getYRot()).intersects(box,position().add(0,1,0));
	}

	@Override public void tick() {
		super.tick();
		if (!(level() instanceof ServerLevel level)) return;
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
		return MagicRules.touchesRing(target.getBoundingBox(), position(), spell().range());
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
