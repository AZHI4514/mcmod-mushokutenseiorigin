package com.example.examplemod.magic.spell.fire.fireball;

import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;

/** Authoritative migrated fireball parameters; presentation and vanilla damage stay in adapters. */
public final class FireballSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("fireball"),3,0,1.5F,1F);
	public static final float DAMAGE=3F;
	public static final double GRAVITY=.01;
	public static final float IGNITION_CHANCE=.8F;
	public static final int IGNITION_TICKS=100;
	private FireballSpec() { }
}
