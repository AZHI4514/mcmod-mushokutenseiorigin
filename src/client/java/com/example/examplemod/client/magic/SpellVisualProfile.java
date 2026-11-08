package com.example.examplemod.client.magic;

import com.example.examplemod.magic.SpellDefinition;

/** Cosmetic classification must not infer projectiles from obsolete speed fields. */
final class SpellVisualProfile {
	static boolean condenses(SpellDefinition spell) {
		return spell.motion() == SpellDefinition.Motion.BALLISTIC || spell.motion() == SpellDefinition.Motion.CONSTANT;
	}
	static float[] handSize(String id) {
		return switch (id) {
			case "water_ball", "fireball" -> new float[]{.32F, .32F, .32F};
			case "ice_smash" -> new float[]{.38F, .38F, .38F};
			case "rock_bullet" -> new float[]{.3F, .3F, .3F};
			case "water_arrow", "ice_blade" -> new float[]{.14F, .14F, .45F};
			case "flame_slice", "sonic_boom", "icicle_edge", "wind_slice" -> new float[]{.48F, .24F, .2F};
			case "flood_flush" -> new float[]{.5F, .25F, .25F};
			case "water_cannon" -> new float[]{.2F, .2F, .4F};
			default -> new float[]{.25F, .25F, .25F};
		};
	}
	private SpellVisualProfile() { }
}
