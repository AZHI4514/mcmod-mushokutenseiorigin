package com.example.examplemod.magic.spell.special.necromancy;
import java.util.Map;
public final class NecromancyRules {
	public static String slot(Map<String,String> slots){if(!slots.containsKey("necromancy_0"))return "necromancy_0";return !slots.containsKey("necromancy_1")?"necromancy_1":null;}
	public static int initialFee(long paidUntil,long now){return paidUntil>now?0:2;}
	public static long corpseExpiry(String value){try{return Long.parseLong(value.split(";")[3]);}catch(RuntimeException error){return 0;}}
	private NecromancyRules() { }
}
