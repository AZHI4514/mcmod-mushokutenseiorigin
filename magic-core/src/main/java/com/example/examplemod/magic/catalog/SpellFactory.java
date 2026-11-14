package com.example.examplemod.magic.catalog;

import com.example.examplemod.magic.mechanic.projectile.*;

/** GoF Creator: a concrete factory overrides product creation. */
public abstract class SpellFactory {
	public abstract ProjectileBehavior create(ProjectileSpec spec);
}
