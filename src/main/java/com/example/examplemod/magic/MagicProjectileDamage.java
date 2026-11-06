package com.example.examplemod.magic;

import com.example.examplemod.mixin.MagicArrowAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/** 水幕以命中盾主时的原始伤害扣盾，不套用护甲减伤。 */
public final class MagicProjectileDamage {
	public static float incoming(Projectile projectile, MagicAreaEntity area) {
		if (projectile instanceof WaterBallEntity ball) return ball.spell().damage();
		if (projectile instanceof FlameSliceEntity) return SpellDefinition.FLAME_SLICE.damage();
		if (projectile instanceof AbstractArrow arrow) {
			boolean trident = arrow instanceof ThrownTrident;
			float base = trident ? 8 : (float) ((MagicArrowAccessor) arrow).mushoku$baseDamage();
			var source = trident ? projectile.damageSources().trident(projectile, projectile.getOwner())
					: projectile.damageSources().arrow(arrow, projectile.getOwner() == null ? projectile : projectile.getOwner());
			if (arrow.getWeaponItem() != null && area.getOwner() != null)
				base = EnchantmentHelper.modifyDamage((ServerLevel) projectile.level(), arrow.getWeaponItem(), area.getOwner(), source, base);
			if (trident) return base;
			int amount = (int) Math.ceil(Math.clamp(projectile.getDeltaMovement().length() * base, 0, Integer.MAX_VALUE));
			if (arrow.isCritArrow()) amount = (int) Math.min(Integer.MAX_VALUE, (long) amount + arrow.getRandom().nextInt(amount / 2 + 2));
			return amount;
		}
		if (projectile instanceof net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball) return 5;
		if (projectile instanceof net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball) return 6;
		if (projectile instanceof net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull) return 8;
		if (projectile instanceof net.minecraft.world.entity.projectile.ShulkerBullet) return 4;
		if (projectile instanceof net.minecraft.world.entity.projectile.LlamaSpit) return 1;
		return 0; // 雪球、鸡蛋、药水等无直接生命伤害，仍挡下其接触效果。
	}
	private MagicProjectileDamage() { }
}
