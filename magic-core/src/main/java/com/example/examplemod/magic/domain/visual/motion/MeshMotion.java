package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;

import java.util.Set;

/** Authored motion for the remaining 34 spells; no server or live entity state is read here. */
public final class MeshMotion {
	private static final Set<String> IDS = Set.of(
			"wind_ball", "water_arrow", "flame_slice", "healing", "ice_blade", "heat_hand", "heat_island",
			"burning_place", "detoxification", "magic_shield", "water_cannon", "water_splash", "icicle_field",
			"ex_flame", "sonic_boom", "stone_cannon", "ice_pillar", "waterfall", "ice_lance", "icicle_break",
			"icicle_edge", "flood_flush", "ice_fortress", "explosion", "flame_pillar", "flamethrower",
			"wind_slice", "wind_bind", "wind_blast", "earth_lance", "earth_pillar", "ex_healing", "antidote_ii", "physical_shield");
	public static boolean handles(String id) { return IDS.contains(id); }
	public static boolean persistent(String id) {
		return switch (id) {
			case "magic_shield", "physical_shield", "burning_place", "heat_island", "flamethrower", "wind_bind" -> true;
			default -> false;
		};
	}
	public static boolean shaded(String id) {
		return switch (id) {
			case "ice_blade", "icicle_field", "ice_pillar", "ice_lance", "icicle_break", "icicle_edge", "ice_fortress", "stone_cannon", "earth_lance", "earth_pillar" -> true;
			default -> false;
		};
	}
	private static final java.util.Map<String,String> FAMILIES=java.util.Map.ofEntries(
		java.util.Map.entry("water_arrow","water"),
		java.util.Map.entry("water_cannon","water"),
		java.util.Map.entry("water_splash","water"),
		java.util.Map.entry("waterfall","water"),
		java.util.Map.entry("flood_flush","water"),
		java.util.Map.entry("flame_slice","fire"),
		java.util.Map.entry("burning_place","fire"),
		java.util.Map.entry("ex_flame","fire"),
		java.util.Map.entry("explosion","fire"),
		java.util.Map.entry("flame_pillar","fire"),
		java.util.Map.entry("flamethrower","fire"),
		java.util.Map.entry("wind_ball","wind"),
		java.util.Map.entry("sonic_boom","wind"),
		java.util.Map.entry("wind_slice","wind"),
		java.util.Map.entry("wind_bind","wind"),
		java.util.Map.entry("wind_blast","wind"),
		java.util.Map.entry("healing","life"),
		java.util.Map.entry("ex_healing","life"),
		java.util.Map.entry("detoxification","life"),
		java.util.Map.entry("antidote_ii","life"),
		java.util.Map.entry("heat_hand","warm"),
		java.util.Map.entry("heat_island","warm"),
		java.util.Map.entry("magic_shield","ward"),
		java.util.Map.entry("physical_shield","ward"));
	private static String family(String id){return FAMILIES.getOrDefault(id,"solid");}

	public static int lifetime(String id) {
		return switch (id) {
			case "healing", "ex_healing", "detoxification", "antidote_ii" -> 32;
			case "water_splash", "icicle_field" -> 28;
			case "explosion", "ex_flame" -> 18;
			default -> 20;
		};
	}
	public static float alpha(String id, String role, int index, float age, int stage) {
		if (stage != MeshStage.IMPACT) {
			if (role.equals("fracture")) return 0;
			float rate = id.equals("waterfall") ? .065F : id.equals("flood_flush") ? .08F : .075F;
			float offset = id.equals("waterfall") ? .097F : id.equals("flood_flush") ? .107F : .137F;
			if (role.equals("droplet")) return cycleFade(age * rate + index * offset);
			if (role.equals("ember") && (id.equals("burning_place") || id.equals("flame_pillar"))) return cycleFade(age * .065F + index * .127F);
			if (role.equals("thermal")) return cycleFade(age * .035F + index * .13F);
			if (role.equals("lifemote") || role.equals("lightseed")) return cycleFade(age * (id.equals("antidote_ii") ? .025F : .018F) + index * .079F);
			if (role.equals("lifemote") || role.equals("wardmote")) return .35F + .65F * sinUnit(age * .2F + index * 2.4F);
			return 1;
		}
		float progress = Math.clamp(age / lifetime(id), 0, 1);
		float fade = (1 - progress) * (1 - progress);
		return switch (family(id)) {
			case "life" -> (float) Math.sin(progress * Math.PI) * (role.equals("lifelight") ? .75F : 1);
			case "warm" -> (float) Math.sin(progress * Math.PI);
			case "water" -> {
				if (id.equals("waterfall")) yield Math.min(1, age / 2) * Math.clamp((20 - age) / 4, 0, 1);
				if (id.equals("water_splash")) yield Math.min(1, age / 2) * Math.clamp((28 - age) / 12, 0, 1);
				if (role.equals("liquidshaft") || role.equals("breaker")) yield Math.clamp(1 - age / 4, 0, 1);
				yield fade;
			}
			case "fire" -> {
				if (id.equals("flame_pillar")) yield Math.min(1, age / 2) * Math.clamp((20 - age) / 7, 0, 1);
				if (role.equals("flash") || role.equals("hotcore")) yield Math.clamp(1 - age / 5, 0, 1);
				if (id.equals("explosion")) yield Math.min(1, age / 2) * Math.clamp((18 - age) / 9, 0, 1);
				yield fade;
			}
			case "wind" -> Math.min(1, age / 1.5F) * fade;
			case "ward" -> fade;
			default -> {
				if (id.equals("icicle_field")) yield Math.min(1, age / 2) * Math.clamp((28 - age) / 8, 0, 1);
				if (id.equals("ice_pillar") || id.equals("earth_lance") || id.equals("ice_fortress")) yield Math.min(1, age / 1.5F) * Math.clamp((20 - age) / 6, 0, 1);
				if (role.equals("rifledstone") || role.equals("fallingmass")) yield Math.clamp(1 - age / 3, 0, 1);
				yield fade;
			}
		};
	}
	private static final java.util.Map<String,MeshDeformation> DEFORMATIONS=java.util.Map.of(
		"water",new WaterMeshDeformation(),
		"fire",new FireMeshDeformation(),
		"wind",new WindMeshDeformation(),
		"life",new LifeMeshDeformation(),
		"warm",new WarmMeshDeformation(),
		"ward",new WardMeshDeformation(),
		"solid",new SolidMeshDeformation());
	public static float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		return DEFORMATIONS.get(family(id)).deform(id,role,index,cx,cy,cz,v,t,stage);
	}
	private static float cycleFade(float value) { float t = fraction(value); return SpellAtmosphereProfile.smooth(t / .16F) * SpellAtmosphereProfile.smooth((1 - t) / .22F); }
	static float sinUnit(float value) { return .5F + .35F * (float) Math.sin(value) + .15F * (float) Math.sin(value * 1.73F + .8F); }
	static float fraction(float value) { return value - (float) Math.floor(value); }
	private MeshMotion() { }
}
