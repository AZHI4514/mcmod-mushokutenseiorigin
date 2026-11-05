package com.example.examplemod.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** 雪球式无穿透弹体。原版伤害与 PVP 判定保持有效，不清空无敌帧。 */
public final class WaterBallEntity extends ThrowableProjectile {
	private int age;
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
			if (++age >= SpellDefinition.WATER_BALL.lifetimeTicks() || !level().hasChunkAt(net.minecraft.core.BlockPos.containing(next))) {
				discard();
				return;
			}
		}
		super.tick();
	}

	@Override
	protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("MagicAge", age);
	}
	@Override
	protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
		super.readAdditionalSaveData(input);
		age = Math.max(0, input.getIntOr("MagicAge", 0));
	}

	@Override
	protected void onHitEntity(EntityHitResult hit) {
		super.onHitEntity(hit);
		if (level() instanceof ServerLevel level) {
			if (getOwner() instanceof net.minecraft.server.level.ServerPlayer owner
					&& hit.getEntity() instanceof net.minecraft.world.entity.player.Player target
					&& !owner.canHarmPlayer(target)) return;
			hit.getEntity().hurtServer(level, damageSources().thrown(this, getOwner()), SpellDefinition.WATER_BALL.damage());
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
			level.sendParticles(ParticleTypes.SPLASH, getX(), getY(), getZ(), 6, 0.1, 0.1, 0.1, 0.05);
			if (getOwner() instanceof net.minecraft.server.level.ServerPlayer player)
				MagicSounds.listener.accept(player, MagicSounds.Phase.IMPACT);
			discard();
		}
	}
}
