package com.example.examplemod.magic.spell.earth.earth_pillar;
import java.util.List;
/** Full preflight before the first mutation; partial world exceptions are not rolled back. */
public final class EarthPillarStone {
	public interface Placement<T> { boolean available(T cell); void place(T cell); }
	public static <T> boolean place(List<T> cells,Placement<T> world) {
		for(var cell:cells)if(!world.available(cell))return false;
		for(var cell:cells)world.place(cell);
		return true;
	}
	private EarthPillarStone() { }
}
