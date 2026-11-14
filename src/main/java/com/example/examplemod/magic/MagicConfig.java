package com.example.examplemod.magic;

import java.io.IOException;
import java.nio.file.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import com.example.examplemod.ExampleMod;

/** 服务端配置入口；以后可接入成长系统，不把最大魔力写死在 GUI。 */
public final class MagicConfig {
	private static int maximum = 100;
	private static boolean permanentTerrain;
	private static java.util.Set<String> teleportDimensions=java.util.Set.of("minecraft:overworld");
	public static boolean teleportDimensionAllowed(String id){return teleportDimensions.contains(id);}
	public static boolean permanentTerrain(){return permanentTerrain;}

	public static int maximumMana() {
		return maximum;
	}

	public static void load() {
		var path = FabricLoader.getInstance().getConfigDir().resolve("mushokutenseiorigin-magic.json");
		try {
			if (!Files.exists(path)) Files.writeString(path, "{\n\t\"maximumMana\": 100,\n\t\"expansionPermanentTerrain\": false\n}\n");
			JsonObject json = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
			maximum = Math.clamp(json.get("maximumMana").getAsInt(), 1, 1000000);
			if(json.has("teleportDimensionWhitelist")){var allowed=new java.util.HashSet<String>();for(var value:json.getAsJsonArray("teleportDimensionWhitelist"))allowed.add(value.getAsString());teleportDimensions=java.util.Set.copyOf(allowed);}
			permanentTerrain = json.has("expansionPermanentTerrain") && json.get("expansionPermanentTerrain").getAsBoolean();
		} catch (IOException | RuntimeException exception) {
			ExampleMod.LOGGER.error("Cannot read magic config; keeping maximumMana={}", maximum, exception);
		}
	}

	private MagicConfig() {
	}
}
