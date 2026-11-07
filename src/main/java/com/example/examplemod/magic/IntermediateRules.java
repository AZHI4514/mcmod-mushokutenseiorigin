package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

/** 与世界无关的可测数值和几何，3×3×3指边长而非半径。 */
public final class IntermediateRules {
	public static int chargeDisplay(int paid) { return Math.max(0,paid); }
	/** Integral of 10 + 10t mana/second; whole mana only, t measured in server ticks. */
	public static long chargeExtraAfterTicks(int ticks) {
		long t=Math.max(0,ticks);
		return (40*t+t*t)/80;
	}
	public static float stoneDamage(int paid) { return 10+Math.max(0,paid-10)/10; }
	public static AABB impactBounds(Vec3 center) { return new AABB(center.x-1.5,center.y-1.5,center.z-1.5,center.x+1.5,center.y+1.5,center.z+1.5); }
	public static List<BlockPos> blastBlocks(BlockPos anchor,int width,Vec3 hit) {
		var result=new ArrayList<BlockPos>();
		int sx=width==1?0:hit.x<anchor.getX()+0.5?-1:0;
		int sy=width==1?0:hit.y<anchor.getY()+0.5?-1:0;
		int sz=width==1?0:hit.z<anchor.getZ()+0.5?-1:0;
		for(int x=0;x<width;x++) for(int y=0;y<width;y++) for(int z=0;z<width;z++) result.add(anchor.offset(sx+x,sy+y,sz+z));
		return result;
	}
	private IntermediateRules() { }
}
