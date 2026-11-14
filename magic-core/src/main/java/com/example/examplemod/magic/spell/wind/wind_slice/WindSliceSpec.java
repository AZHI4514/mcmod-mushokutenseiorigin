package com.example.examplemod.magic.spell.wind.wind_slice;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class WindSliceSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("wind_slice"),8,10,2F,0F);
	public static final int PREPARATION_TICKS=160;
	public static final float DAMAGE=7F;
	public static final double RADIUS=3;
	private WindSliceSpec() { }
}
