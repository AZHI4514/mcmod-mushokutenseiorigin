package com.example.examplemod.magic.spell.fire.heat_hand;
import java.util.*;
public final class HeatHandRules {
	public enum Axis {X,Y,Z}
	public record Cell(int x,int y,int z) { }
	public static List<Cell> targets(int x,int y,int z,Axis face){
		var result=new ArrayList<Cell>();
		for(int a=0;a<2;a++)for(int b=0;b<2;b++)result.add(new Cell(x+(face==Axis.Y?a:0)+(face==Axis.Z?b:0),y+(face==Axis.Y?0:a),z+(face==Axis.Z?0:b)));
		return result;
	}
	public static boolean becomesWater(boolean ice,boolean evaporates){return ice&&!evaporates;}
	private HeatHandRules() { }
}
