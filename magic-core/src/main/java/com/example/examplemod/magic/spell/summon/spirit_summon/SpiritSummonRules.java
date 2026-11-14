package com.example.examplemod.magic.spell.summon.spirit_summon;
public final class SpiritSummonRules {
	public static boolean attacksThisPulse(long now){return now%40<20;}
	public static float protectedDamage(float amount){return Math.max(0,amount-4);}
	private SpiritSummonRules() { }
}
