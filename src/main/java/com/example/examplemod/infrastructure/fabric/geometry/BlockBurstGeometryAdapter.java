package com.example.examplemod.infrastructure.fabric.geometry;
import com.example.examplemod.magic.mechanic.projectile.BlockBurstPlan;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.*;
import java.util.List;
/** Converts pure block plans, retaining vanilla AABB handling at the game boundary. */
public final class BlockBurstGeometryAdapter {
	public static AABB impactBounds(Vec3 center){return new AABB(center.x-1.5,center.y-1.5,center.z-1.5,center.x+1.5,center.y+1.5,center.z+1.5);}
	public static List<BlockPos> blastBlocks(BlockPos anchor,int width,Vec3 hit){
		var result=new java.util.ArrayList<BlockPos>();
		for(var p:BlockBurstPlan.cells(anchor.getX(),anchor.getY(),anchor.getZ(),width,hit.x,hit.y,hit.z))result.add(new BlockPos(p.x(),p.y(),p.z()));
		return result;
	}
	private BlockBurstGeometryAdapter() {}
}
