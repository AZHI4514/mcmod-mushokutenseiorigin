package com.example.examplemod.magic.mechanic.projectile;

import com.example.examplemod.magic.domain.model.SpellId;

public record ProjectileSpec(SpellId id, int mana, int recovery, float speed, float inaccuracy) {
	public ProjectileSpec {
		if (mana < 0 || recovery < 0 || !Float.isFinite(speed) || speed < 0 || !Float.isFinite(inaccuracy) || inaccuracy < 0)
			throw new IllegalArgumentException("Invalid projectile specification");
	}
}
