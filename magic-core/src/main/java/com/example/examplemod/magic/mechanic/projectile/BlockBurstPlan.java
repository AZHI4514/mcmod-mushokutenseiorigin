package com.example.examplemod.magic.mechanic.projectile;
import java.util.ArrayList;
import java.util.List;
/** Pure block-cell selection preserves side-of-impact ordering and integer coordinates. */
public final class BlockBurstPlan {
	public record Cell(int x,int y,int z) {}
	public static List<Cell> cells(int x,int y,int z,int width,double hitX,double hitY,double hitZ) {
		var result=new ArrayList<Cell>();
		int sx=width==1?0:hitX<x+.5?-1:0;
		int sy=width==1?0:hitY<y+.5?-1:0;
		int sz=width==1?0:hitZ<z+.5?-1:0;
		for(int dx=0;dx<width;dx++)for(int dy=0;dy<width;dy++)for(int dz=0;dz<width;dz++)result.add(new Cell(x+sx+dx,y+sy+dy,z+sz+dz));
		return result;
	}
	private BlockBurstPlan() {}
}
