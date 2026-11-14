package com.example.examplemod.magic.mechanic.projectile;

import com.example.examplemod.magic.domain.model.Vec3d;

/** A dedicated spawn plan, not a general effect instruction language. */
public record ProjectileSpawnPlan(ProjectileSpec spec, Vec3d origin, float yaw, float pitch) { }
