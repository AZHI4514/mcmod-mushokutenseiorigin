package com.example.examplemod.magic;

/** Only called after authoritative successful release/payment. */
public final class MagicGlow {
	public static void success(net.minecraft.server.level.ServerPlayer player,SpellDefinition spell) {
		player.setAttached(MagicAttachments.CAST_COLOR,spell.glowColor());
		player.setAttached(MagicAttachments.CAST_TICKS,2);
	}
	private MagicGlow() { }
}
