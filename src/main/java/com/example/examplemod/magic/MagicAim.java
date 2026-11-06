package com.example.examplemod.magic;

import net.minecraft.world.phys.Vec3;

/** 施法瞬间的准星角度；只传角度，不接受客户端提供位置、速度或伤害。 */
public record MagicAim(float yaw, float pitch) {
	public boolean valid() {
		return Float.isFinite(yaw) && Float.isFinite(pitch) && pitch >= -90 && pitch <= 90;
	}

	public Vec3 direction() {
		if (!valid()) throw new IllegalArgumentException("Invalid casting aim");
		return Vec3.directionFromRotation(pitch, yaw % 360).normalize();
	}
}
