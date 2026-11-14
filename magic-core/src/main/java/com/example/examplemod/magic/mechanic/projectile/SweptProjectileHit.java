package com.example.examplemod.magic.mechanic.projectile;

public final class SweptProjectileHit {
	public interface Target {
		boolean hurt();
		boolean hasKnockback();
		void knockback();
		void effect();
		void afterHit();
	}

	public static void apply(Target target) {
		if (!target.hurt()) return;
		if (target.hasKnockback()) target.knockback();
		target.effect();
		target.afterHit();
	}

	private SweptProjectileHit() { }
}
