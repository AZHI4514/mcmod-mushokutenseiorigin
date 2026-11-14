package com.example.examplemod.magic.spell.healing.ex_healing;
public final class ExHealingRules {
	public static float amount(float health,float maximum){
		if(!Float.isFinite(health)||!Float.isFinite(maximum)||health<=0)return 0;
		return Math.min(8F,Math.max(0,maximum-health));
	}
	private ExHealingRules() { }
}
