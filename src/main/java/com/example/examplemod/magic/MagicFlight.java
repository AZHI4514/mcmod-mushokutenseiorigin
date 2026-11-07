package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.*;

/** Loaded-chunk-only collision helpers. No chunk tickets or extra lifetime limits. */
public final class MagicFlight {
	public static double fluidHit(Entity projectile, SpellSweep volume, Vec3 from, Vec3 motion) {
		double distance=motion.length(); if(distance<1.0E-8) return Double.POSITIVE_INFINITY;
		var direction=motion.normalize();var bounds=volume.bounds(from).minmax(volume.bounds(from.add(motion)));
		double first=Double.POSITIVE_INFINITY;
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			if(!projectile.level().hasChunkAt(pos)) continue;
			var fluid=projectile.level().getFluidState(pos); if(fluid.isEmpty()) continue;
			var box=new AABB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+fluid.getHeight(projectile.level(),pos),pos.getZ()+1);
			first=Math.min(first,volume.contact(box,from,direction,distance));
		}
		return first;
	}
	public static void saveHits(net.minecraft.world.level.storage.ValueOutput output,java.util.Set<java.util.UUID> hit) {
		output.store("MagicVictims",com.mojang.serialization.Codec.STRING.listOf(),hit.stream().map(java.util.UUID::toString).toList());
	}
	public static void loadHits(net.minecraft.world.level.storage.ValueInput input,java.util.Set<java.util.UUID> hit) {
		hit.clear();for(var id:input.read("MagicVictims",com.mojang.serialization.Codec.STRING.listOf()).orElse(java.util.List.of())) { try {hit.add(java.util.UUID.fromString(id));} catch(IllegalArgumentException ignored) { } }
	}
	private MagicFlight() { }
}
