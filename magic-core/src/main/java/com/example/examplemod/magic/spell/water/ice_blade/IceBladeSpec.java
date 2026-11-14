package com.example.examplemod.magic.spell.water.ice_blade;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Ballistic blade; requested-aim launch is exact, flight remains vanilla ballistic. */
public final class IceBladeSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("ice_blade"),5,0,2.5F,0F);
	public static final float DAMAGE = 5F;
	public static final double GRAVITY = .02;
	public static final float STATUS_CHANCE = .8F;
	public static final int STATUS_TICKS = 100;
	private IceBladeSpec() { }
}
