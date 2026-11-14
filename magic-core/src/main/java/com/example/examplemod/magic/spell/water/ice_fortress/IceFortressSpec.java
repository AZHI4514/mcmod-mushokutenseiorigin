package com.example.examplemod.magic.spell.water.ice_fortress;
import java.util.List;
import java.util.ArrayList;
/** Pure ordered wall plan; the world boundary supplies the vanilla-computed base and direction. */
public final class IceFortressSpec {
	public static final int MANA=14, RECOVERY=10, PREPARATION_TICKS=160, TEMPORARY_TICKS=200;
	public record Cell(int x,int y,int z) { }
	public static List<Cell> plan(int x,int y,int z,double directionX,double directionZ) {
		boolean east=Math.abs(directionZ)>=Math.abs(directionX);var cells=new ArrayList<Cell>();
		for(int height=0;height<3;height++)for(int width=-2;width<=2;width++)cells.add(new Cell(x+(east?width:0),y+height,z+(east?0:width)));
		return List.copyOf(cells);
	}
	private IceFortressSpec() { }
}
