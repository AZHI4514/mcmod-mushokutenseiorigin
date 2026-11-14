package com.example.examplemod.infrastructure.fabric.magic;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import com.example.examplemod.magic.application.port.ProjectileSpawner;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan;
/** Converts a pure spawn plan to the explicitly assembled vanilla entity and launch strategy. */
public record VanillaProjectileAdapter(ServerPlayer player,Supplier<? extends Projectile> entities,ProjectileLaunch launch) implements ProjectileSpawner {
	public boolean spawn(ProjectileSpawnPlan plan) {
		var projectile=entities.get();projectile.setOwner(player);var origin=plan.origin();
		projectile.setPos(origin.x,origin.y,origin.z);launch.shoot(projectile,player,plan);
		return player.level().addFreshEntity(projectile);
	}
}
