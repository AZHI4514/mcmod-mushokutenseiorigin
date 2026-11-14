package com.example.examplemod.magic;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.List;

public final class PlayerSaveCompatibilityTest {
	public static void main(String[] args) throws Exception {
		try (var in = PlayerSaveCompatibilityTest.class.getResourceAsStream("/architecture/player-save-v0.json")) {
			var json = JsonParser.parseString(new String(in.readAllBytes(), StandardCharsets.UTF_8));
			var state = MagicState.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
			if (state.mana()!=7 || state.maximum()!=100 || state.onlineTicks()!=23999 || state.cooldown()!=5
					|| !state.learned().equals(List.of("water_ball")) || !state.loaded().equals(List.of("water_ball")) || !state.selected().equals("water_ball"))
				throw new AssertionError("Legacy normalization changed");
			var encoded = MagicState.CODEC.encodeStart(JsonOps.INSTANCE, state).getOrThrow();
			if (!encoded.getAsJsonObject().keySet().equals(Set.of("mana","maximum","online_ticks","cooldown","learned","loaded","selected")))
				throw new AssertionError("Save field contract changed");
			if (!state.equals(MagicState.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow())) throw new AssertionError("Save roundtrip changed");
		}
		System.out.println("Player save: legacy seven-field fixture, unknown ID filtering and roundtrip verified.");
	}
}
