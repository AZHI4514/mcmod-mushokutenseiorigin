package com.example.examplemod.magic.domain.visual;

/** Cosmetic profiles shared by mesh motion and bounded local particle emitters. */
public final class SpellAtmosphereProfile {
	public record Profile(String family, float size, int accents, int duration) {
		public boolean buoyant() { return family.equals("fire") || family.equals("warm") || family.equals("life"); }
		public boolean dusty() { return family.equals("fire") || family.equals("earth") || family.equals("ice") || family.equals("water"); }
	}
	private static final java.util.Map<String,Profile> PROFILES=java.util.Map.ofEntries(
		java.util.Map.entry("lightning",new Profile("arc",1.5F,18,24)),
		java.util.Map.entry("electric",new Profile("arc",1.5F,18,24)),
		java.util.Map.entry("frost_nova",new Profile("ice",2F,18,30)),
		java.util.Map.entry("sonic_blast",new Profile("wind",2F,18,30)),
		java.util.Map.entry("flame_tornado",new Profile("fire",2F,24,44)),
		java.util.Map.entry("absolute_zero",new Profile("ice",2.3F,28,48)),
		java.util.Map.entry("inferno",new Profile("fire",2.3F,28,48)),
		java.util.Map.entry("flash_over",new Profile("fire",2F,24,44)),
		java.util.Map.entry("magma_gush",new Profile("fire",1.8F,20,44)),
		java.util.Map.entry("violent_storm",new Profile("wind",1.8F,20,40)),
		java.util.Map.entry("tornado",new Profile("wind",1.8F,20,40)),
		java.util.Map.entry("vacuum_storm",new Profile("wind",1.8F,20,40)),
		java.util.Map.entry("sky_tempest",new Profile("wind",1.8F,20,40)),
		java.util.Map.entry("blast",new Profile("warm",1.4F,12,30)),
		java.util.Map.entry("bora_blast",new Profile("ice",1.8F,16,30)),
		java.util.Map.entry("quagmire",new Profile("earth",.7F,8,30)),
		java.util.Map.entry("earth_hedgehog",new Profile("earth",2F,18,30)),
		java.util.Map.entry("earth_fortress",new Profile("earth",2F,18,40)),
		java.util.Map.entry("earth_wall",new Profile("earth",1.3F,12,30)),
		java.util.Map.entry("tornado_impact",new Profile("wind",1.6F,14,34)),
		java.util.Map.entry("exodus_flame",new Profile("fire",2F,16,44)),
		java.util.Map.entry("cumulonimbus",new Profile("rain",1.5F,16,24)),
		java.util.Map.entry("squall",new Profile("rain",1F,12,30)),
		java.util.Map.entry("blizzard_storm",new Profile("ice",3F,16,80)),
		java.util.Map.entry("water_ball",new Profile("water", 1F, 7, 44)),
		java.util.Map.entry("fireball",new Profile("fire", 1F, 7, 44)),
		java.util.Map.entry("wind_ball",new Profile("wind", 1F, 7, 34)),
		java.util.Map.entry("rock_bullet",new Profile("earth", 1F, 7, 44)),
		java.util.Map.entry("water_arrow",new Profile("water", 0.7F, 7, 44)),
		java.util.Map.entry("ice_smash",new Profile("ice", 1F, 7, 34)),
		java.util.Map.entry("flame_slice",new Profile("fire", 1F, 7, 44)),
		java.util.Map.entry("healing",new Profile("life", 1F, 7, 34)),
		java.util.Map.entry("water_shield",new Profile("water", 1F, 7, 44)),
		java.util.Map.entry("ice_blade",new Profile("ice", 0.7F, 7, 34)),
		java.util.Map.entry("heat_hand",new Profile("warm", 0.7F, 7, 34)),
		java.util.Map.entry("heat_island",new Profile("warm", 1F, 7, 34)),
		java.util.Map.entry("burning_place",new Profile("fire", 1F, 7, 44)),
		java.util.Map.entry("sand_cloud",new Profile("earth", 2.2F, 7, 34)),
		java.util.Map.entry("dust_storm",new Profile("earth", 3.8F, 7, 34)),
		java.util.Map.entry("sandstorm",new Profile("earth", 3.8F, 7, 34)),
		java.util.Map.entry("deep_mist",new Profile("water", 4.2F, 7, 34)),
		java.util.Map.entry("mud_rain",new Profile("earth", 1.1F, 7, 34)),
		java.util.Map.entry("flame_storm",new Profile("fire", 2.8F, 7, 34)),
		java.util.Map.entry("apocalypse_flame",new Profile("fire", 2.8F, 7, 34)),
		java.util.Map.entry("detoxification",new Profile("detox", 0.7F, 7, 34)),
		java.util.Map.entry("magic_shield",new Profile("ward", 1F, 7, 34)),
		java.util.Map.entry("water_cannon",new Profile("water", 1F, 7, 44)),
		java.util.Map.entry("water_splash",new Profile("water", 1.35F, 12, 44)),
		java.util.Map.entry("icicle_field",new Profile("ice", 1.35F, 12, 34)),
		java.util.Map.entry("ex_flame",new Profile("fire", 1.35F, 12, 44)),
		java.util.Map.entry("sonic_boom",new Profile("wind", 1F, 7, 34)),
		java.util.Map.entry("stone_cannon",new Profile("earth", 1.35F, 12, 44)),
		java.util.Map.entry("ice_pillar",new Profile("ice", 1F, 7, 34)),
		java.util.Map.entry("waterfall",new Profile("water", 1.35F, 12, 44)),
		java.util.Map.entry("ice_lance",new Profile("ice", 1F, 7, 34)),
		java.util.Map.entry("icicle_break",new Profile("ice", 1F, 7, 34)),
		java.util.Map.entry("icicle_edge",new Profile("ice", 1F, 7, 34)),
		java.util.Map.entry("flood_flush",new Profile("water", 1.35F, 12, 44)),
		java.util.Map.entry("ice_fortress",new Profile("ice", 1.35F, 12, 34)),
		java.util.Map.entry("explosion",new Profile("fire", 1.35F, 12, 44)),
		java.util.Map.entry("flame_pillar",new Profile("fire", 1.35F, 12, 44)),
		java.util.Map.entry("flamethrower",new Profile("fire", 1F, 7, 44)),
		java.util.Map.entry("wind_slice",new Profile("wind", 0.7F, 7, 34)),
		java.util.Map.entry("wind_bind",new Profile("wind", 1F, 7, 34)),
		java.util.Map.entry("wind_blast",new Profile("wind", 1F, 7, 34)),
		java.util.Map.entry("earth_lance",new Profile("earth", 1F, 7, 44)),
		java.util.Map.entry("earth_pillar",new Profile("earth", 1F, 7, 44)),
		java.util.Map.entry("ex_healing",new Profile("life", 1.35F, 12, 34)),
		java.util.Map.entry("antidote_ii",new Profile("detox", 1F, 7, 34)),
		java.util.Map.entry("physical_shield",new Profile("ward", 1F, 7, 34)));
	public static java.util.Set<String> ids() { return PROFILES.keySet(); }
	public static Profile of(String id) {
		var profile=PROFILES.get(id);
		if(profile==null)throw new IllegalArgumentException("Unknown spell atmosphere: "+id);
		return profile;
	}
	public static float envelope(float t) {
		t = Math.clamp(t, 0, 1);
		float attack = Math.min(1, t * 8);
		return attack * attack * (3 - 2 * attack) * (1 - t) * (1 - t);
	}
	public static float smooth(float t) { t = Math.clamp(t, 0, 1); return t * t * (3 - 2 * t); }
	private SpellAtmosphereProfile() { }
}
