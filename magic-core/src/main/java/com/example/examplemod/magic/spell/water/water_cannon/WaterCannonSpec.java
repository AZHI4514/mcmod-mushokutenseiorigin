package com.example.examplemod.magic.spell.water.water_cannon;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class WaterCannonSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("water_cannon"),8,10,2F,0F);
	public static final int PREPARATION_TICKS=160, FLIGHT_TICKS=120, WET_TICKS=200;
	public static final float DAMAGE=7F;
	public static final double KNOCKBACK=.8;
	private WaterCannonSpec() { }
}
