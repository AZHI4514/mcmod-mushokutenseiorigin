package com.example.examplemod.magic.spell.earth.rock_bullet;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Ballistic spell values; collision, drag and resistance remain vanilla adapter responsibilities. */
public final class RockBulletSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("rock_bullet"), 3, 0, 1.5F, 1F);
	public static final float DAMAGE = 4F;
	public static final double GRAVITY = .05;
	public static final double KNOCKBACK = 0;
	private RockBulletSpec() { }
}
