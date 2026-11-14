package com.example.examplemod.magic.spell.water.water_shield;
import com.example.examplemod.magic.mechanic.projectile.ShieldAbsorption;
public final class WaterShieldAbsorb {
	public interface Shield extends ShieldAbsorption.Shield { }
	public static void apply(float incoming,Shield shield){ShieldAbsorption.apply(incoming,shield);}
	private WaterShieldAbsorb() { }
}
