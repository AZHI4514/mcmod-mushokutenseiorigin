package com.example.examplemod.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;

/** 仅服务端调用；MagicSystem 先校验并扣除，效果未产生则回滚魔力与冷却。 */
public final class SpellExecutors {
	public static boolean canExecute(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
		if (spell.execution() == SpellDefinition.Execution.PHASE_TWO) return PhaseTwoSpells.canExecute(player,spell,aim);
		if (spell.execution() == SpellDefinition.Execution.DETOX) return player.hasEffect(net.minecraft.world.effect.MobEffects.POISON);
		if (spell.execution() == SpellDefinition.Execution.HEAT_HAND) return HeatHand.canUse(player, aim);
		return spell.execution() != SpellDefinition.Execution.SELF_HEAL
				|| spell.healingAmount(player.getHealth(), player.getMaxHealth()) > 0;
	}

	public static boolean execute(ServerPlayer player, SpellDefinition spell, MagicAim castingAim) {
		if (spell.execution() == SpellDefinition.Execution.PHASE_TWO) return PhaseTwoSpells.execute(player,spell,castingAim);
		if (spell.execution() == SpellDefinition.Execution.ADVANCED_PROJECTILE || spell.execution() == SpellDefinition.Execution.TARGET_AREA)
			return IntermediateSpells.execute(player,spell,castingAim,10);
		if (spell.execution() == SpellDefinition.Execution.AREA) return MagicAreas.cast(player, spell, castingAim);
		if (spell.execution() == SpellDefinition.Execution.HEAT_HAND) return HeatHand.execute(player, castingAim);
		if (spell.execution() == SpellDefinition.Execution.DETOX) return player.removeEffect(net.minecraft.world.effect.MobEffects.POISON);
		if (spell.execution() == SpellDefinition.Execution.SELF_HEAL) {
			float health = player.getHealth();
			float amount = spell.healingAmount(health, player.getMaxHealth());
			if (amount <= 0) return false;
			player.heal(amount);
			if (player.getHealth() <= health) return false;
			((ServerLevel) player.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER,
					player.getX(), player.getY() + 1, player.getZ(), 8, 0.3, 0.4, 0.3, 0);
			return true;
		}
		Projectile projectile = spell.execution() == SpellDefinition.Execution.FLAME_SLICE
				? new FlameSliceEntity(MagicSystem.FLAME_SLICE, player.level())
				: new WaterBallEntity(MagicSystem.projectileType(spell), player.level());
		projectile.setOwner(player);
		projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
		if (spell.execution() == SpellDefinition.Execution.FLAME_SLICE || spell == SpellDefinition.ICE_BLADE) {
			// 从视线原点沿请求中的准星方向发射，避免服务端旧朝向及垂直偏移。
			projectile.setPos(player.getEyePosition());
			var aim = castingAim.direction();
			projectile.shoot(aim.x, aim.y, aim.z, spell.speed(), 0);
		} else projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, spell.speed(), 1.0F);
		return player.level().addFreshEntity(projectile);
	}

	private SpellExecutors() { }
}
