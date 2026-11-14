package com.example.examplemod.magic.spell.water.water_arrow;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Basic ballistic projectile, without a time or distance cap. */
public final class WaterArrowSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("water_arrow"), 2, 0, 2.5F, 1F);
	public static final float DAMAGE = 2F;
	public static final double GRAVITY = .03;
	public static final int STATUS_TICKS = 100;
	public static final float STATUS_CHANCE = .6F;
	private WaterArrowSpec() { }
}
