package com.example.examplemod.magic.spell.water.ice_fortress;
import java.util.List;
/** World targets and terrain effects are supplied by the per-release adapter. */
public final class IceFortressPlacement {
	public interface World<T,C> {
		List<T> targets();
		void launch(T target);
		void grow(List<C> cells);
		void place(C cell,int ticks);
		void burst();
	}
	public static <T,C> void apply(List<C> cells,World<T,C> world) {
		var targets=world.targets();for(var target:targets)world.launch(target);
		if(!targets.isEmpty())world.grow(cells);
		else for(var cell:cells)world.place(cell,IceFortressSpec.TEMPORARY_TICKS);
		world.burst();
	}
	private IceFortressPlacement() { }
}
