package com.example.examplemod.infrastructure.fabric.magic;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import com.example.examplemod.magic.MagicTerrain;
/** Shared world preflight for existing ice/earth constructions; no mutation or chunk tickets. */
public final class ColumnPlacementAdapter {
	public static boolean canPlace(ServerPlayer player,List<BlockPos> plan) {
		var level=(ServerLevel)player.level();
		for(var pos:plan) if(!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(player,pos)||!level.getBlockState(pos).isAir()) return false;
		var bounds=new AABB(plan.getFirst()).minmax(new AABB(plan.getLast()));
		for(var entity:level.getEntities(player,bounds,e->e instanceof LivingEntity)) {
			var box=entity.getBoundingBox(); double lift=bounds.maxY-box.minY+0.02;
			if(!level.noCollision(entity,box.expandTowards(0,lift,0))) return false;
		}
		if(player.getBoundingBox().intersects(bounds)&&!level.noCollision(player,player.getBoundingBox().expandTowards(0,bounds.maxY-player.getY()+0.02,0))) return false;
		return true;
	}
	private ColumnPlacementAdapter() { }
}
