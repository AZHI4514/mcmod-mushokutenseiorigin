package com.example.examplemod.magic.application.port;

import com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan;

public interface ProjectileSpawner {
	boolean spawn(ProjectileSpawnPlan plan);
}
