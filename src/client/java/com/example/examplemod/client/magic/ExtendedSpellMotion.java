package com.example.examplemod.client.magic;

import java.util.Set;

/** Authored motion for the remaining 34 spells; no server or live entity state is read here. */
final class ExtendedSpellMotion {
	private static final Set<String> IDS = Set.of(
			"wind_ball", "water_arrow", "flame_slice", "healing", "ice_blade", "heat_hand", "heat_island",
			"burning_place", "detoxification", "magic_shield", "water_cannon", "water_splash", "icicle_field",
			"ex_flame", "sonic_boom", "stone_cannon", "ice_pillar", "waterfall", "ice_lance", "icicle_break",
			"icicle_edge", "flood_flush", "ice_fortress", "explosion", "flame_pillar", "flamethrower",
			"wind_slice", "wind_bind", "wind_blast", "earth_lance", "earth_pillar", "ex_healing", "antidote_ii", "physical_shield");
	static boolean handles(String id) { return IDS.contains(id); }
	static boolean persistent(String id) {
		return switch (id) {
			case "magic_shield", "physical_shield", "burning_place", "heat_island", "flamethrower", "wind_bind" -> true;
			default -> false;
		};
	}
	static boolean shaded(String id) {
		return switch (id) {
			case "ice_blade", "icicle_field", "ice_pillar", "ice_lance", "icicle_break", "icicle_edge", "ice_fortress", "stone_cannon", "earth_lance", "earth_pillar" -> true;
			default -> false;
		};
	}
	private static String family(String id) {
		return switch (id) {
			case "water_arrow", "water_cannon", "water_splash", "waterfall", "flood_flush" -> "water";
			case "flame_slice", "burning_place", "ex_flame", "explosion", "flame_pillar", "flamethrower" -> "fire";
			case "wind_ball", "sonic_boom", "wind_slice", "wind_bind", "wind_blast" -> "wind";
			case "healing", "ex_healing", "detoxification", "antidote_ii" -> "life";
			case "heat_hand", "heat_island" -> "warm";
			case "magic_shield", "physical_shield" -> "ward";
			default -> "solid";
		};
	}
	static int lifetime(String id) {
		return switch (id) {
			case "healing", "ex_healing", "detoxification", "antidote_ii" -> 32;
			case "water_splash", "icicle_field" -> 28;
			case "explosion", "ex_flame" -> 18;
			default -> 20;
		};
	}
	static float alpha(String id, String role, int index, float age, int stage) {
		if (stage != ReferenceSpellModels.IMPACT) {
			if (role.equals("fracture")) return 0;
			if (role.equals("droplet")) return .3F + .7F * sinUnit(age * .24F + index);
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
	static float[] deform(String id, String role, int index, float cx, float cy, float cz, float[] v, float t, int stage) {
		float x = v[0], y = v[1], z = v[2], phase = t * .27F + index * 1.73F;
		boolean impact = stage == ReferenceSpellModels.IMPACT, form = stage == ReferenceSpellModels.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = (float) Math.sqrt(progress);
		String family = family(id);
		if (family.equals("water")) {
			if (id.equals("water_splash")) {
				float expand = .1F + .9F * Math.clamp(t / 12, 0, 1);
				x *= expand; z *= expand;
				if (role.equals("crown")) y = -.15F + (y + .15F) * (float) Math.sin(Math.min(1, t / 25) * Math.PI);
				if (role.equals("droplet")) { y += .65F * progress - 1.25F * progress * progress; x *= 1 + progress * .3F; z *= 1 + progress * .3F; }
				if (role.equals("ripple")) { x *= 1 + progress * .25F; z *= 1 + progress * .25F; }
			} else if (id.equals("waterfall")) {
				if (role.equals("droplet")) { float cycle = fraction(t * .065F + index * .097F); y = .46F - cycle; }
				else { x += .008F * (float) Math.sin(y * 14 + t * .35F + index); z += .022F * (float) Math.sin(y * 16 + x * 9 - t * .4F); }
				float grow = Math.clamp(t / 5, .01F, 1); y = .5F - (.5F - y) * grow;
			} else if (impact) {
				float a = index * 2.39996F, radius = spread * (role.equals("droplet") ? 1.6F : .95F);
				x = (x - cx) * (1 + spread) + (float) Math.cos(a) * radius;
				y = y - cy + (float) Math.sin(a) * radius - progress * progress * .8F;
				z = (z - cz) * .4F;
			} else if (id.equals("flood_flush")) {
				if (role.equals("droplet")) { float c = fraction(t * .08F + index * .107F); y += c * .2F - c * c * .3F; z -= c * .3F; }
				else { float crest = Math.clamp(y + .5F, 0, 1); z += .035F * (float) Math.sin(x * 12 + t * .3F) * crest; y += .02F * (float) Math.sin(x * 18 - t * .3F) * crest; }
			} else {
				if (role.equals("droplet")) {
					float cycle = fraction(t * .075F + index * .137F);
					if (form) { float a = phase; x = x - cx + (float) Math.cos(a) * (.4F + cycle * .3F); y = y - cy + (float) Math.sin(a) * (.4F + cycle * .3F); z -= cz; }
					else z -= cycle * .8F;
				} else {
					float wave = .025F * (float) Math.sin(z * 11 - t * .4F + index); x *= 1 + wave; y *= 1 - wave;
				}
			}
		} else if (family.equals("fire")) {
			if (id.equals("explosion") && impact) {
				float grow = .18F + .82F * (1 - (float) Math.pow(1 - Math.clamp(t / 7, 0, 1), 2));
				x *= grow; z *= grow; y *= grow;
				if (role.equals("firebloom")) { y += progress * .35F; x += cx * progress * .5F; z += cz * progress * .5F; }
				if (role.equals("blastwave")) { x *= 1 + progress * .5F; z *= 1 + progress * .5F; y = -.3F; }
				if (role.equals("ember")) { x += cx * progress; z += cz * progress; y += progress * .6F; }
			} else if (impact && !id.equals("flame_pillar")) {
				float a = index * 2.39996F;
				if (role.equals("hotcore")) { float grow = 1 + t * .24F; x *= grow; y *= grow; z *= grow; }
				else { x += (float) Math.cos(a) * spread; y += (float) Math.sin(a) * spread + progress * .5F; z *= .5F; }
			} else if (id.equals("burning_place") || id.equals("flame_pillar")) {
				if (role.equals("ember")) { float c = fraction(t * .065F + index * .127F); y = -.35F + c * .9F; x += .018F * (float) Math.sin(phase); }
				else if (!role.equals("emberbed")) {
					float h = y + .5F;
					x += .025F * (float) Math.sin(h * 9 - t * .5F + index) * h;
					z += .025F * (float) Math.cos(h * 8 - t * .43F + index) * h;
					y = -.5F + h * (1 + .11F * (float) Math.sin(phase));
				}
			} else if (id.equals("flamethrower")) {
				float dist = z + .5F; float angle = .14F * (float) Math.sin(t * .6F - z * 14 + index);
				float oldX = x; x = x * (float) Math.cos(angle) - y * (float) Math.sin(angle); y = oldX * (float) Math.sin(angle) + y * (float) Math.cos(angle);
				x += .03F * dist * (float) Math.sin(t * .5F - z * 17 + index); y += .03F * dist * (float) Math.cos(t * .43F - z * 13 + index);
			} else {
				float tail = Math.clamp(.2F - z, 0, 1.5F), wave = (float) Math.sin(t * .6F - z * 11 + index * 2.4F);
				x += tail * wave * .025F; y += tail * (float) Math.cos(phase - z * 7) * .04F;
				if (role.equals("firetongue")) z *= 1 + wave * .15F;
				if (role.equals("firewake")) z -= .05F + index * .05F;
				// Keep the trailing flame facing back along the aim.
			}
		} else if (family.equals("wind")) {
			if (id.equals("wind_bind")) {
				float a = t * .13F, oldX = x; x = x * (float) Math.cos(a) + z * (float) Math.sin(a); z = -oldX * (float) Math.sin(a) + z * (float) Math.cos(a);
				y += .015F * (float) Math.sin(phase + y * 8);
			} else {
				if (id.equals("wind_ball")) { float a = t * .16F, oldX = x; x = x * (float) Math.cos(a) - y * (float) Math.sin(a); y = oldX * (float) Math.sin(a) + y * (float) Math.cos(a); }
				else if (role.equals("gust")) { float a = t * .06F - z * .3F, oldX = x; x = x * (float) Math.cos(a) - y * (float) Math.sin(a); y = oldX * (float) Math.sin(a) + y * (float) Math.cos(a); }
				else z -= .04F * (float) Math.sin(phase);
				if (impact) { float r = 1 + spread * 1.5F; x *= r; y *= r; z *= .4F; }
			}
		} else if (family.equals("life")) {
			if (role.equals("lifemote") || role.equals("lightseed")) {
				float rise = fraction(t * (id.equals("antidote_ii") ? .025F : .018F) + index * .079F);
				y = y - cy - .45F + rise * .95F;
				float a = index * 2.39996F + t * .035F, r = .3F + .09F * (float) Math.sin(phase);
				x = x - cx + (float) Math.cos(a) * r; z = z - cz + (float) Math.sin(a) * r;
			} else {
				float a = t * .025F, oldX = x; x = x * (float) Math.cos(a) + z * (float) Math.sin(a); z = -oldX * (float) Math.sin(a) + z * (float) Math.cos(a);
				float pulse = .92F + .08F * sinUnit(phase); x *= pulse; z *= pulse;
			}
		} else if (family.equals("warm")) {
			if (role.equals("thermal")) {
				float rise = fraction(t * .035F + index * .13F); y += rise * .18F;
				x += .015F * (float) Math.sin(y * 11 + t * .3F + index); z += .012F * (float) Math.sin(y * 13 - t * .23F);
			} else { y += .035F * (float) Math.sin(phase); float r = 1 + .025F * (float) Math.sin(phase); x *= r; z *= r; }
		} else if (family.equals("ward")) {
			float pulse = 1 + .008F * (float) Math.sin(t * .2F);
			x *= pulse; y *= pulse; z *= pulse;
			// Keep engraved directions stable, unlike a spinning physical shield.
			if (role.equals("wardmote")) { float a = t * .035F; float oldX = x; x = x * (float) Math.cos(a) + z * (float) Math.sin(a); z = -oldX * (float) Math.sin(a) + z * (float) Math.cos(a); }
			if (role.equals("wardface") || role.equals("barrierveil")) z += .002F * (float) Math.sin(x * 14 + y * 9 - t * .3F);
		} else {
			boolean grows = role.equals("growth") || role.equals("groundspike") || role.equals("wallice");
			if (grows) {
				float grow = Math.clamp((t - index * .12F) / (id.equals("icicle_field") ? 6 : 4), .01F, 1);
				if (stage == ReferenceSpellModels.FLIGHT && id.equals("ice_lance")) grow = Math.clamp(t / 5, .01F, 1);
				y = -.5F + (y + .5F) * grow;
			} else if (impact) {
				float length = (float) Math.sqrt(cx * cx + cy * cy + cz * cz);
				if (length > .03F) {
					float a = t * (.07F + index % 3 * .035F), dx = x - cx, dy = y - cy;
					x = dx * (float) Math.cos(a) - dy * (float) Math.sin(a) + cx * (1 + spread / length);
					y = dx * (float) Math.sin(a) + dy * (float) Math.cos(a) + cy * (1 + spread / length) - progress * progress;
					z += cz * spread / length;
				}
			} else if (id.equals("stone_cannon") || id.equals("icicle_break")) {
				float a = t * (id.equals("stone_cannon") ? .36F : .18F), oldX = x;
				x = x * (float) Math.cos(a) - y * (float) Math.sin(a); y = oldX * (float) Math.sin(a) + y * (float) Math.cos(a);
				if (role.equals("rubble")) z -= fraction(t * .09F + index * .13F) * .45F;
			} else if (form) { float grow = Math.clamp((t - index * .12F) / 8, .01F, 1); x *= grow; y *= grow; z *= grow; }
		}
		return new float[]{x, y, z};
	}
	private static float sinUnit(float value) { return .5F + .5F * (float) Math.sin(value); }
	private static float fraction(float value) { return value - (float) Math.floor(value); }
	private ExtendedSpellMotion() { }
}
