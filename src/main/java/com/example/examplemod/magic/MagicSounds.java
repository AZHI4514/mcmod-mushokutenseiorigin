package com.example.examplemod.magic;

import java.util.function.BiConsumer;
import net.minecraft.server.level.ServerPlayer;

/** 空实现声音接口。未配置真实素材前，不播放占位声音。 */
public final class MagicSounds {
	public enum Phase { CHARGE, RELEASE, IMPACT }
	public static BiConsumer<ServerPlayer, Phase> listener = (player, phase) -> { };

	private MagicSounds() {
	}
}
