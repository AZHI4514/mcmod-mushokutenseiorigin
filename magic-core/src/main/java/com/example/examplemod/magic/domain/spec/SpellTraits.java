package com.example.examplemod.magic.domain.spec;
import java.util.Set;
import java.util.Map;
/** Explicit identity traits; technical carrier categories do not define official spell rank. */
public final class SpellTraits {
	public enum Motion {BALLISTIC,CONSTANT,FALLING,INSTANT}
	private static final Set<String> CONSTANT=Set.of("tornado_impact","exodus_flame","flame_slice","water_cannon","ex_flame","sonic_boom","stone_cannon","icicle_break","icicle_edge","flood_flush","wind_slice","explosion");
	private static final Set<String> AREA=Set.of("exodus_flame","flame_slice","burning_place","water_cannon","water_splash","icicle_field","ex_flame","ice_pillar","ice_lance","explosion","flamethrower","wind_slice","earth_pillar");
	private static final Set<String> ICY=Set.of("ocean_freeze","absolute_zero","frost_nova","blizzard_storm","ice_smash","ice_blade","icicle_field","ice_pillar","ice_lance","icicle_break","icicle_edge","ice_fortress");
	public static Motion motion(String id,boolean ballistic){if(id.equals("earth_pillar"))return Motion.FALLING;if(ballistic)return Motion.BALLISTIC;return CONSTANT.contains(id)?Motion.CONSTANT:Motion.INSTANT;}
	public static boolean areaDamage(String id){return AREA.contains(id);}
	public static boolean icy(String id){return ICY.contains(id);}
	private static final Map<String,Integer> COLORS=Map.of("fire",0xFF5030,"wind",0xFFFFFF,"earth",0xA87946,"healing",0x55FF77);
	public static int glowColor(String id,String element){return id.equals("physical_shield")?0xBA70FF:COLORS.getOrDefault(element,0x43BFFF);}
	private static final Map<String,String> CATEGORY=Map.of("nuclear_explosion","MIXED","flame_tornado","MIXED","blast","MIXED","bora_blast","MIXED","frost_nova","MIXED","detoxification","DETOX","antidote_ii","DETOX","antidote_iii","DETOX");
	private static final Map<String,String> ELEMENT_CATEGORY=Map.of("summon","SUMMON","special","SPECIAL","holy","HOLY","mixed","MIXED","fire","FIRE","water","WATER","wind","WIND","earth","EARTH","healing","HEALING");
	public static String category(String id,String element){return CATEGORY.getOrDefault(id,ELEMENT_CATEGORY.getOrDefault(element,"BARRIER"));}
	public static int statusTicks(String id,int rank){return id.equals("water_splash")?2000:com.example.examplemod.magic.domain.combat.EffectRules.statusTicks(rank);}
	private SpellTraits(){}
}
