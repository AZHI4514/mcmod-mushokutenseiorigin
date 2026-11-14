package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.*;

/** Loaded-chunk-only collision helpers. No chunk tickets or extra lifetime limits. */
public final class MagicFlight {
	public static boolean ignoresWater(Entity entity) {
		SpellDefinition spell = entity instanceof SweptSpellProjectile p ? p.spell() : entity instanceof DirectionalSpellCarrier p ? p.spell() : entity instanceof FlameSliceEntity ? SpellDefinition.FLAME_SLICE : null;
		return MagicRules.passesWater(spell, true);
	}
	public static boolean passesWaterBlock(Entity entity, net.minecraft.core.BlockPos pos) {
		var state=entity.level().getBlockState(pos);
		return com.example.examplemod.magic.mechanic.projectile.WaterPassage.passes(ignoresWater(entity), state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED), state.getFluidState().is(net.minecraft.tags.FluidTags.WATER));
	}
	public static double fluidHit(Entity projectile, SpellSweep volume, Vec3 from, Vec3 motion) {
		double distance=motion.length(); if(distance<1.0E-8) return Double.POSITIVE_INFINITY;
		var direction=motion.normalize();var bounds=volume.bounds(from).minmax(volume.bounds(from.add(motion)));
		return com.example.examplemod.magic.mechanic.projectile.FluidContactScan.first(
				BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ)),
				new com.example.examplemod.magic.mechanic.projectile.FluidContactScan.World<BlockPos,net.minecraft.world.level.material.FluidState>() {
					public boolean loaded(BlockPos pos) {return projectile.level().hasChunkAt(pos);}
					public net.minecraft.world.level.material.FluidState fluid(BlockPos pos) {return projectile.level().getFluidState(pos);}
					public boolean empty(net.minecraft.world.level.material.FluidState fluid) {return fluid.isEmpty();}
					public boolean ignoresWater() {return MagicFlight.ignoresWater(projectile);}
					public boolean water(net.minecraft.world.level.material.FluidState fluid) {return fluid.is(net.minecraft.tags.FluidTags.WATER);}
					public double contact(BlockPos pos,net.minecraft.world.level.material.FluidState fluid) {
						var box=new AABB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+fluid.getHeight(projectile.level(),pos),pos.getZ()+1);
						return volume.contact(box,from,direction,distance);
					}
				});
	}
	public static void saveHits(net.minecraft.world.level.storage.ValueOutput output,java.util.Set<java.util.UUID> hit) {
		output.store("MagicVictims",com.mojang.serialization.Codec.STRING.listOf(),com.example.examplemod.magic.mechanic.projectile.HitHistoryFormat.save(hit));
	}
	public static void loadHits(net.minecraft.world.level.storage.ValueInput input,java.util.Set<java.util.UUID> hit) {
		com.example.examplemod.magic.mechanic.projectile.HitHistoryFormat.restore(hit,()->input.read("MagicVictims",com.mojang.serialization.Codec.STRING.listOf()).orElse(java.util.List.of()));
	}
	private MagicFlight() { }
}
