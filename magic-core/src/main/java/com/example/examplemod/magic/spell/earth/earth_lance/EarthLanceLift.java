package com.example.examplemod.magic.spell.earth.earth_lance;
import java.util.List;
/** Self-support branch: launch every collected entity before scheduling permanent construction. */
public final class EarthLanceLift {
	public interface World<T> {
		void launch(T target);
		void growPermanent();
		void placePermanent();
		void show();
	}
	public static <T> void apply(List<T> targets,World<T> world) {
		for(var target:targets)world.launch(target);
		if(targets.isEmpty())world.placePermanent();else world.growPermanent();
		world.show();
	}
	private EarthLanceLift() { }
}
