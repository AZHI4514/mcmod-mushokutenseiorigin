package com.example.examplemod.magic.mechanic.projectile;

import com.example.examplemod.magic.domain.model.Vec3d;

public interface ProjectileBehavior {
	ProjectileSpawnPlan prepare(Vec3d origin, float yaw, float pitch);
}
