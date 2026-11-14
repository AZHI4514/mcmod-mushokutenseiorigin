package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.ProjectileSpawner;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan;
/** Phase carriers require configure, not vanilla shoot: it sets synced identity, age budget and bounds. */
public record PhaseProjectileAdapter(ServerPlayer player,SpellDefinition spell) implements ProjectileSpawner {
	@Override public boolean spawn(ProjectileSpawnPlan plan) {
		if(!spell.id().equals(plan.spec().id().value()))throw new IllegalArgumentException("Mismatched phase projectile");
		var origin=plan.origin();var direction=new MagicAim(plan.yaw(),plan.pitch()).direction();
		var carrier=EffectCarrierSpawner.entity(player,spell,new Vec3(origin.x,origin.y,origin.z),direction);
		return player.level().addFreshEntity(carrier);
	}
}
