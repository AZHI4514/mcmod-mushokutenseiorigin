package com.example.examplemod.magic;

import net.minecraft.core.particles.ParticleTypes;
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
		if (!level().isClientSide()) {
			var next = position().add(getDeltaMovement());
			if (++age >= spell().lifetimeTicks() || !level().hasChunkAt(net.minecraft.core.BlockPos.containing(next))) {
				discard();
				return;
			}
		}
		if (spell() == SpellDefinition.ICE_BLADE) {
			setDeltaMovement(getDeltaMovement().normalize().scale(Math.min(spell().speed(), Math.max(0, spell().range()-distance))));
			distance += getDeltaMovement().length();
		}
		super.tick();
		if (spell() == SpellDefinition.ICE_BLADE && distance >= spell().range() && !level().isClientSide()) discard();
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
			var particles = spell().icy() ? ParticleTypes.SNOWFLAKE : switch (spell().element()) {
				case "fire" -> ParticleTypes.FLAME;
				case "wind" -> ParticleTypes.CLOUD;
				case "earth" -> ParticleTypes.CRIT;
				default -> ParticleTypes.SPLASH;
			};
			level.sendParticles(particles, getX(), getY(), getZ(), 6, 0.1, 0.1, 0.1, 0.05);
			if (getOwner() instanceof net.minecraft.server.level.ServerPlayer player)
				MagicSounds.listener.accept(player, MagicSounds.Phase.IMPACT);
			discard();
		}
	}
}
