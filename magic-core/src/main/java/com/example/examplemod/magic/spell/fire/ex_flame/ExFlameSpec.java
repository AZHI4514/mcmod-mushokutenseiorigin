package com.example.examplemod.magic.spell.fire.ex_flame;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class ExFlameSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("ex_flame"),12,10,2F,0F);
	public static final int PREPARATION_TICKS=160,FLIGHT_TICKS=120,IGNITION_TICKS=200,TERRAIN_WIDTH=2;
	public static final float DAMAGE=8F;
	public static final double BODY_SIZE=2;
	private ExFlameSpec() { }
}
