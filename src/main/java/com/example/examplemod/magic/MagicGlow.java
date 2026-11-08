package com.example.examplemod.magic;

/** Only called after authoritative successful release/payment. */
public final class MagicGlow {
	public static void success(net.minecraft.server.level.ServerPlayer player,SpellDefinition spell) {
		player.setAttached(MagicAttachments.VISUAL_SPELL,spell.id());
		player.setAttached(MagicAttachments.CAST_COLOR,spell.glowColor());
		player.setAttached(MagicAttachments.CAST_TICKS,2);
		if (spell.element().equals("healing") || spell==SpellDefinition.WIND_BLAST || spell==SpellDefinition.HEAT_HAND)
			SpellVisuals.burst((net.minecraft.server.level.ServerLevel)player.level(),spell,player.position().add(0,0.1,0),player.getLookAngle());
	}
	private MagicGlow() { }
}
