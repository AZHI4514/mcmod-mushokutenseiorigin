package com.example.examplemod.magic.spell.water.ice_smash;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Basic ballistic projectile, without a time or distance cap. */
public final class IceSmashSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("ice_smash"), 4, 0, 1.5F, 1F);
	public static final float DAMAGE = 5F;
	public static final double GRAVITY = .02;
	public static final int STATUS_TICKS = 100;
	public static final float STATUS_CHANCE = .8F;
	private IceSmashSpec() { }
}
