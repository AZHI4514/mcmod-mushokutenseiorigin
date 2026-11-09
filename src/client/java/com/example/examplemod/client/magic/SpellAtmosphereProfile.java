package com.example.examplemod.client.magic;

/** Cosmetic profiles shared by mesh motion and bounded local particle emitters. */
final class SpellAtmosphereProfile {
	record Profile(String family, float size, int accents, int duration) {
		boolean buoyant() { return family.equals("fire") || family.equals("warm") || family.equals("life"); }
		boolean dusty() { return family.equals("fire") || family.equals("earth") || family.equals("ice") || family.equals("water"); }
	}
	static Profile of(String id) {
		return switch (id) {
			case "water_ball" -> new Profile("water", 1F, 7, 44);
			case "fireball" -> new Profile("fire", 1F, 7, 44);
			case "wind_ball" -> new Profile("wind", 1F, 7, 34);
			case "rock_bullet" -> new Profile("earth", 1F, 7, 44);
			case "water_arrow" -> new Profile("water", 0.7F, 7, 44);
			case "ice_smash" -> new Profile("ice", 1F, 7, 34);
			case "flame_slice" -> new Profile("fire", 1F, 7, 44);
			case "healing" -> new Profile("life", 1F, 7, 34);
			case "water_shield" -> new Profile("water", 1F, 7, 44);
			case "ice_blade" -> new Profile("ice", 0.7F, 7, 34);
			case "heat_hand" -> new Profile("warm", 0.7F, 7, 34);
			case "heat_island" -> new Profile("warm", 1F, 7, 34);
			case "burning_place" -> new Profile("fire", 1F, 7, 44);
			case "detoxification" -> new Profile("detox", 0.7F, 7, 34);
			case "magic_shield" -> new Profile("ward", 1F, 7, 34);
			case "water_cannon" -> new Profile("water", 1F, 7, 44);
			case "water_splash" -> new Profile("water", 1.35F, 12, 44);
			case "icicle_field" -> new Profile("ice", 1.35F, 12, 34);
			case "ex_flame" -> new Profile("fire", 1.35F, 12, 44);
			case "sonic_boom" -> new Profile("wind", 1F, 7, 34);
			case "stone_cannon" -> new Profile("earth", 1.35F, 12, 44);
			case "ice_pillar" -> new Profile("ice", 1F, 7, 34);
			case "waterfall" -> new Profile("water", 1.35F, 12, 44);
			case "ice_lance" -> new Profile("ice", 1F, 7, 34);
			case "icicle_break" -> new Profile("ice", 1F, 7, 34);
			case "icicle_edge" -> new Profile("ice", 1F, 7, 34);
			case "flood_flush" -> new Profile("water", 1.35F, 12, 44);
			case "ice_fortress" -> new Profile("ice", 1.35F, 12, 34);
			case "explosion" -> new Profile("fire", 1.35F, 12, 44);
			case "flame_pillar" -> new Profile("fire", 1.35F, 12, 44);
			case "flamethrower" -> new Profile("fire", 1F, 7, 44);
			case "wind_slice" -> new Profile("wind", 0.7F, 7, 34);
			case "wind_bind" -> new Profile("wind", 1F, 7, 34);
			case "wind_blast" -> new Profile("wind", 1F, 7, 34);
			case "earth_lance" -> new Profile("earth", 1F, 7, 44);
			case "earth_pillar" -> new Profile("earth", 1F, 7, 44);
			case "ex_healing" -> new Profile("life", 1.35F, 12, 34);
			case "antidote_ii" -> new Profile("detox", 1F, 7, 34);
			case "physical_shield" -> new Profile("ward", 1F, 7, 34);
			default -> throw new IllegalArgumentException("Unknown spell atmosphere: " + id);
		};
	}
	static float envelope(float t) {
		t = Math.clamp(t, 0, 1);
		float attack = Math.min(1, t * 8);
		return attack * attack * (3 - 2 * attack) * (1 - t) * (1 - t);
	}
	static float smooth(float t) { t = Math.clamp(t, 0, 1); return t * t * (3 - 2 * t); }
	private SpellAtmosphereProfile() { }
}
