package com.example.examplemod.infrastructure.fabric.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Loaded-only visibility queries; this adapter never requests chunk loads. */
public final class SpellVisibilityAdapter {
	public static boolean visible(ServerLevel level,Vec3 from,LivingEntity target,net.minecraft.world.entity.Entity source) {
		var to=target.getBoundingBox().getCenter(); var difference=to.subtract(from);
		int steps=Math.max(1,(int)Math.ceil(difference.length()));
		for(int i=0;i<=steps;i++)
			if(!level.hasChunkAt(BlockPos.containing(from.add(difference.scale(i/(double)steps))))) return false;
		return level.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,source)).getType()==HitResult.Type.MISS;
	}
	private SpellVisibilityAdapter() { }
}
