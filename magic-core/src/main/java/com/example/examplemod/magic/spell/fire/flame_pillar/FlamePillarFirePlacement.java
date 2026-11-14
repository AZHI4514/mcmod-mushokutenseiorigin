package com.example.examplemod.magic.spell.fire.flame_pillar;
import java.util.List;
/** Tries the selected face then the top, preserving first-success semantics. */
public final class FlamePillarFirePlacement {
	public interface World<C> {
		boolean mayChange(C cell);
		boolean existingFire(C cell);
		boolean air(C cell);
		boolean placeSurvivingFire(C cell);
	}
	public static <C> boolean apply(List<C> cells,World<C> world) {
		for(var cell:cells) {
			if(!world.mayChange(cell))continue;
			if(world.existingFire(cell))return true;
			if(!world.air(cell))continue;
			if(world.placeSurvivingFire(cell))return true;
		}
		return false;
	}
	private FlamePillarFirePlacement() { }
}
