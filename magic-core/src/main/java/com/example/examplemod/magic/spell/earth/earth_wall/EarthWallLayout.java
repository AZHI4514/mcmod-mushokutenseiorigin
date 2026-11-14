package com.example.examplemod.magic.spell.earth.earth_wall;
import java.util.*;
public final class EarthWallLayout {
	public record Cell(int x,int y,int z) { }
	public static List<Cell> offsets(double directionX,double directionZ){
		boolean alongX=Math.abs(directionZ)>=Math.abs(directionX);var cells=new ArrayList<Cell>();
		for(int y=0;y<3;y++)for(int x=-2;x<=2;x++)cells.add(new Cell(alongX?x:0,y,alongX?0:x));return List.copyOf(cells);
	}
	private EarthWallLayout() { }
}
