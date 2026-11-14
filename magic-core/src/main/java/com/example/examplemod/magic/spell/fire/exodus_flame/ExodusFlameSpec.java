package com.example.examplemod.magic.spell.fire.exodus_flame;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class ExodusFlameSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("exodus_flame"),44,20,2F,0F);
	public static final int PREPARATION_TICKS=240,FLIGHT_TICKS=180;
	public static final float DAMAGE=18;
	public static final double RANGE=360;
	private ExodusFlameSpec() { }
}
