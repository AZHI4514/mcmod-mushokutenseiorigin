package com.example.examplemod.magic.spell.fire.explosion;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Vanilla TNT owns dynamic damage, knockback, fire and block drops. */
public final class ExplosionSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("explosion"),16,10,2F,0F);
	public static final int PREPARATION_TICKS=160;
	public static final float TNT_POWER=4F;
	private ExplosionSpec() { }
}
