package com.example.examplemod.magic.spell.water.water_ball;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Original ballistic water spell: no age or range cap. */
public final class WaterBallSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("water_ball"),1,0,1.5F,1F);
	public static final float DAMAGE=1;
	public static final double GRAVITY=.03;
	private WaterBallSpec(){}
}
