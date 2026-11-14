package com.example.examplemod.magic.spell.wind.wind_ball;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Ballistic spell values; collision, drag and resistance remain vanilla adapter responsibilities. */
public final class WindBallSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("wind_ball"), 2, 0, 1.5F, 1F);
	public static final float DAMAGE = 1F;
	public static final double GRAVITY = .01;
	public static final double KNOCKBACK = .6;
	private WindBallSpec() { }
}
