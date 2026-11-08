package com.example.examplemod.magic;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** 六种无穿透弹体的共用行为；类型决定定义，保留旧 water_ball 存档。原版伤害与 PVP 判定保持有效，不清空无敌帧。 */
public final class WaterBallEntity extends ThrowableProjectile {
	private int age;
	private double distance;

	public SpellDefinition spell() {
		if (getType() == MagicSystem.FIREBALL) return SpellDefinition.FIREBALL;
		if (getType() == MagicSystem.WIND_BALL) return SpellDefinition.WIND_BALL;
		if (getType() == MagicSystem.ROCK_BULLET) return SpellDefinition.ROCK_BULLET;
		if (getType() == MagicSystem.WATER_ARROW) return SpellDefinition.WATER_ARROW;
		if (getType() == MagicSystem.ICE_BLADE) return SpellDefinition.ICE_BLADE;
		if (getType() == MagicSystem.ICE_SMASH) return SpellDefinition.ICE_SMASH;
		return SpellDefinition.WATER_BALL;
	}

	@Override
	protected double getDefaultGravity() { return spell().gravity(); }
	public WaterBallEntity(EntityType<? extends WaterBallEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	public void tick() {
		if (level() instanceof ServerLevel level) {
			if (getY() < level.getMinY()) { discard(); return; }
			var sweep = getBoundingBox().expandTowards(getDeltaMovement());
			if (!PhaseTwoSpells.loaded(level, sweep)) return;
			var fluid = MagicFlight.fluidHit(this, new SpellSweep(getDeltaMovement().normalize(), 0.25, 0.25, 0.25), position(), getDeltaMovement());
			var block = level.clip(new net.minecraft.world.level.ClipContext(position(), position().add(getDeltaMovement()), net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, this));
			double before = block.getType() == HitResult.Type.MISS ? getDeltaMovement().length() : position().distanceTo(block.getLocation());
			var entity = net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(level, this, position(), position().add(getDeltaMovement()), sweep.inflate(1), this::canHitEntity);
			if (entity != null) before = Math.min(before, position().distanceTo(entity.getLocation()));
			if (Double.isFinite(fluid) && fluid <= before) { setPos(position().add(getDeltaMovement().normalize().scale(fluid))); onHit(new net.minecraft.world.phys.BlockHitResult(position(), net.minecraft.core.Direction.UP, blockPosition(), false)); return; }
		}
		super.tick();
		if (level().isClientSide() && !isRemoved()) {
			var velocity = getDeltaMovement();
			var trail = SpellParticles.trail(spell());
			int count = 1;
			for (int i = 0; i < count; i++) {
				var point = position().subtract(velocity.scale(i / 3.0));
				level().addParticle(trail, point.x, point.y + 0.1, point.z,
						-velocity.x * 0.04, 0.015, -velocity.z * 0.04);
			}
		}
	}

	@Override
	protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("MagicAge", age);
		output.putDouble("MagicDistance", distance);
	}
	@Override
	protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
		super.readAdditionalSaveData(input);
		age = Math.max(0, input.getIntOr("MagicAge", 0));
		distance = Math.clamp(input.getDoubleOr("MagicDistance", 0), 0, spell().range());
		if (!Double.isFinite(distance)) distance = spell().range();
	}

	@Override
	protected void onHitEntity(EntityHitResult hit) {
		super.onHitEntity(hit);
		if (level() instanceof ServerLevel level) {
			if (getOwner() instanceof net.minecraft.server.level.ServerPlayer owner
					&& hit.getEntity() instanceof net.minecraft.world.entity.player.Player target
					&& !owner.canHarmPlayer(target)) return;
			var damage = MagicDamage.source(this, spell());
			boolean damaged = hit.getEntity().hurtServer(level, damage, spell().damage());
			if (damaged && spell().knockback() > 0 && hit.getEntity() instanceof net.minecraft.world.entity.LivingEntity target) {
				var direction = getDeltaMovement();
				target.knockback(spell().knockback(), -direction.x, -direction.z);
			}
			if (damaged) { MagicEffects.apply(getOwner(), hit.getEntity(), spell()); SpellHooks.afterHit.accept(this, hit.getEntity()); }
		}
	}

	@Override
	protected void onHitBlock(net.minecraft.world.phys.BlockHitResult hit) {
		// 水弹不触发靶标、钟、按钮或方块破坏。
	}

	@Override
	protected void onHit(HitResult hit) {
		super.onHit(hit);
		if (level() instanceof ServerLevel level) {
			SpellVisuals.burst(level,spell(),position(),getDeltaMovement().normalize());
			if (getOwner() instanceof net.minecraft.server.level.ServerPlayer player)
				MagicSounds.listener.accept(player, MagicSounds.Phase.IMPACT);
			discard();
		}
	}
}
