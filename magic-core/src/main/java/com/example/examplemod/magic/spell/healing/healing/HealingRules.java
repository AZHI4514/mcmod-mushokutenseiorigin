package com.example.examplemod.magic.spell.healing.healing;
public final class HealingRules {
	public static float amount(float health,float maximum,float healing){if(!Float.isFinite(health)||!Float.isFinite(maximum)||health<=0)return 0;return Math.min(healing,Math.max(0,maximum-health));}
	private HealingRules() { }
}
