package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.bootstrap.ProjectileHitComposition;

import com.example.examplemod.magic.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/** World-only impact presentation and compatibility block burst routing. */
public final class SweptProjectileImpactAdapter {
	public static void blockImpact(SweptSpellProjectile projectile,ServerPlayer owner,BlockPos block,Vec3 center) {
		var policy=com.example.examplemod.magic.mechanic.projectile.SweptProjectilePolicy.of(projectile.spell());
		if(policy.blockBurstWidth()<=0)throw new IllegalArgumentException("Not a block-burst projectile");
		BlockBurstImpactAdapter.blockImpact(projectile,owner,block,center,policy.blockBurstWidth(),policy.blockBurstFire());
	}

	public static void impactParticles(SweptSpellProjectile projectile) {
		var level=(ServerLevel)projectile.level();
		SpellVisuals.burst(level,projectile.spell(),projectile.position(),projectile.getDeltaMovement().normalize());
		if(projectile.getOwner() instanceof ServerPlayer owner) MagicSounds.listener.accept(owner,MagicSounds.Phase.IMPACT);
	}
	private SweptProjectileImpactAdapter() { }
}
