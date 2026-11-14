package com.example.examplemod.magic.spell.summon.monster_summon;
import java.util.Map;
public final class MonsterSummonRules {
	public static String slot(Map<String,String> slots,String id){return !slots.containsKey(id)?id:null;}
	private MonsterSummonRules() { }
}
