package com.example.examplemod.magic.spell.wind.sonic_boom;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class SonicBoomSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("sonic_boom"),8,10,2F,0F);
	public static final int PREPARATION_TICKS=160,FLIGHT_TICKS=120;
	public static final float DAMAGE=5F;
	public static final double WIDTH=3,HEIGHT=1,DEPTH=.25,KNOCKBACK=1;
	private SonicBoomSpec() { }
}
