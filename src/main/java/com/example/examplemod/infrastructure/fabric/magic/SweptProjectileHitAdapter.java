package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.SweptProjectileHit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public final class SweptProjectileHitAdapter {
	public static void apply(SweptSpellProjectile projectile, LivingEntity target, SpellDefinition spell, float damage) {
		var level = (ServerLevel) projectile.level();
		SweptProjectileHit.apply(new SweptProjectileHit.Target() {
			public boolean hurt() { return target.hurtServer(level, MagicDamage.source(projectile, spell), damage); }
			public boolean hasKnockback() { return spell.knockback() > 0; }
			public void knockback() { var motion = projectile.getDeltaMovement(); target.knockback(spell.knockback(), -motion.x, -motion.z); }
			public void effect() { MagicEffects.apply(projectile.getOwner(), target, spell); }
			public void afterHit() { SpellHooks.afterHit(projectile, target); }
		});
	}
	private SweptProjectileHitAdapter() { }
}
