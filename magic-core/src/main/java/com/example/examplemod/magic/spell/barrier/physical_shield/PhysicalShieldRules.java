package com.example.examplemod.magic.spell.barrier.physical_shield;
import com.example.examplemod.magic.domain.combat.EffectRules;
public final class PhysicalShieldRules {
	public static final int RENEW_TICKS=160;
	public static final float CAPACITY=16;
	public static boolean canRenew(boolean removed,boolean renewable,boolean sameLevel,double distanceSquared){return !(removed||!renewable||!sameLevel||distanceSquared>1.0E-6);}
	public static int renewedDuration(int remaining){return EffectRules.extend(remaining,RENEW_TICKS);}
	private PhysicalShieldRules() { }
}
