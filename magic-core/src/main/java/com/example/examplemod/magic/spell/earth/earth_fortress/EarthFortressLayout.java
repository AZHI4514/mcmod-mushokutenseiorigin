package com.example.examplemod.magic.spell.earth.earth_fortress;
import java.util.*;
public final class EarthFortressLayout {
	public record Cell(int x,int y,int z) { public int squaredDistance(){return x*x+y*y+z*z;} }
	public static List<Cell> shell(double lookX,double lookZ){
		var result=new ArrayList<Cell>();boolean alongZ=Math.abs(lookZ)>=Math.abs(lookX);int sign=(alongZ?lookZ:lookX)>=0?1:-1;
		for(int y=0;y<=4;y++)for(int x=-4;x<=4;x++)for(int z=-4;z<=4;z++){
			int radius=x*x+y*y+z*z;if(radius>16||radius<9)continue;
			boolean door=y<2&&(alongZ?x==0&&z*sign>=3:z==0&&x*sign>=3);if(!door)result.add(new Cell(x,y,z));
		}result.sort(Comparator.comparingInt(Cell::y).thenComparingDouble(Cell::squaredDistance));return List.copyOf(result);
	}
	private EarthFortressLayout() { }
}
