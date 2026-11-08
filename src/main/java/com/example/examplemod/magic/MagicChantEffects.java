package com.example.examplemod.magic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** One letter every four server ticks during preparation or lessons, emitted from the anatomical left eye. */
public final class MagicChantEffects {
	public static void tick(ServerPlayer player) {
		if (Math.floorMod(player.tickCount, 4) != 0) return;
		var look = player.getLookAngle();
		// Head yaw, not body yaw: +X is the player's left while facing +Z.
		var forward = Vec3.directionFromRotation(0, player.getYHeadRot());
		var left = new Vec3(forward.z, 0, -forward.x);
		var at = player.getEyePosition().add(left.scale(.105)).add(look.scale(.24));
		var velocity = left.scale(.023).add(forward.scale(.008)).add(0, .032, 0);
		// count=0 is exactly one particle with the supplied velocity, no random spread.
		((ServerLevel) player.level()).sendParticles(SpellParticles.CHANT,
				at.x, at.y, at.z, 0, velocity.x, velocity.y, velocity.z, 1);
	}
	private MagicChantEffects() { }
}
