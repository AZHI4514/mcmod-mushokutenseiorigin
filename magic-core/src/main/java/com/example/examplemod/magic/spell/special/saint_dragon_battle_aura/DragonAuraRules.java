package com.example.examplemod.magic.spell.special.saint_dragon_battle_aura;
public final class DragonAuraRules {
	public static float reducedDamage(float amount){return amount*.65F;}
	public static boolean canCast(long now,long ready,long end){return now>=ready&&end<=now;}
	private DragonAuraRules() { }
}
