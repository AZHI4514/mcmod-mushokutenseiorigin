package com.example.examplemod.magic.spell.water.water_shield;
import com.example.examplemod.magic.domain.combat.EffectRules;
public final class WaterShieldRules {
	public static final int MANA=6,DURATION=80,PREPARATION=100,RECOVERY=0;
	public static final float CAPACITY=8;
	public static boolean canRenew(boolean renewable,double displacementSquared){return renewable&&displacementSquared<1.0E-6;}
	public static int extend(int remaining){return EffectRules.extend(remaining,DURATION);}
	private WaterShieldRules() { }
}
