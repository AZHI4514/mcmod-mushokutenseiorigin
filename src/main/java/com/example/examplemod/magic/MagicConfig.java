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

	public static int maximumMana() {
		return maximum;
	}

	public static void load() {
		var path = FabricLoader.getInstance().getConfigDir().resolve("mushokutenseiorigin-magic.json");
		try {
			if (!Files.exists(path)) Files.writeString(path, "{\n\t\"maximumMana\": 100\n}\n");
			JsonObject json = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
			maximum = Math.clamp(json.get("maximumMana").getAsInt(), 1, 1000000);
		} catch (IOException | RuntimeException exception) {
			ExampleMod.LOGGER.error("Cannot read magic config; keeping maximumMana={}", maximum, exception);
		}
	}

	private MagicConfig() {
	}
}
