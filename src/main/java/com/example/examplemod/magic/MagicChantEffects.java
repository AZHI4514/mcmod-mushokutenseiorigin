package com.example.examplemod.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/** 原版附魔粒子环绕咏唱中的玩家。 */
public final class MagicChantEffects {
	public static void tick(ServerPlayer player) {
		ServerLevel level = (ServerLevel) player.level();
		for (int i = 0; i < 2; i++) {
			double angle = player.tickCount * 0.16 + i * Math.PI;
			double radius = 1.2 + i * 0.65;
			level.sendParticles(ParticleTypes.ENCHANT,
					player.getX() + Math.cos(angle) * radius, player.getY() + 0.7 + i * 0.45,
					player.getZ() + Math.sin(angle) * radius, 1, 0, 0.05, 0, 0.08);
		}
	}

	private MagicChantEffects() { }
}
