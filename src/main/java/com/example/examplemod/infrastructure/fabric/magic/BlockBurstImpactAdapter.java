package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.bootstrap.ProjectileHitComposition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.BlockBurstRules;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
/** Block-only burst; direct hits never enter terrain modification. */
public final class BlockBurstImpactAdapter {
	public static void blockImpact(SweptSpellProjectile projectile,ServerPlayer owner,BlockPos block,Vec3 center,int terrainWidth,boolean ignite) {
		var level=(ServerLevel)projectile.level();
		var visibleFrom=center.subtract(projectile.getDeltaMovement().normalize().scale(.02));
		var bounds=GeometryAdapter.box(BlockBurstRules.bounds(GeometryAdapter.vector(center)));
		for(var entity:level.getEntities(projectile,bounds,e->e instanceof LivingEntity && MagicEffects.allowed(owner,(LivingEntity)e)))
			if(SpellVisibilityAdapter.visible(level,visibleFrom,(LivingEntity)entity,projectile))ProjectileHitComposition.hurt(projectile,(LivingEntity)entity,projectile.spell(),projectile.damage());
		com.example.examplemod.infrastructure.fabric.magic.BlastTerrainAdapter.blast(owner,block,center,terrainWidth,ignite);
	}
	private BlockBurstImpactAdapter() { }
}
