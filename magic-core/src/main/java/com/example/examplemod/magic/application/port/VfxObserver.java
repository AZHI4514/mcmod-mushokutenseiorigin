package com.example.examplemod.magic.application.port;

import com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan;

/** Notification only after successful commit; never authorizes damage or changes balance. */
public interface VfxObserver {
	void released(ProjectileSpawnPlan event);
}
