package com.example.examplemod.magic.mechanic.projectile;

import com.example.examplemod.magic.catalog.SpellFactory;

/** Ordinary projectiles share this concrete Creator; no per-ID empty factory. */
public final class ProjectileSpellFactory extends SpellFactory {
	@Override public ProjectileBehavior create(ProjectileSpec spec) {
		java.util.Objects.requireNonNull(spec);
		return (origin, yaw, pitch) -> {
			if (!Float.isFinite(yaw) || !Float.isFinite(pitch) || pitch < -90 || pitch > 90)
				throw new IllegalArgumentException("Invalid projectile angles");
			return new ProjectileSpawnPlan(spec, java.util.Objects.requireNonNull(origin), yaw, pitch);
		};
	}
}
