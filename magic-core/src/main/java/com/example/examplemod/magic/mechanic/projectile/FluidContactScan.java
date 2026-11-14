package com.example.examplemod.magic.mechanic.projectile;

/** Loaded-only fluid scan; no early exit even after zero contact. */
public final class FluidContactScan {
	public interface World<P,F> {
		boolean loaded(P position);
		F fluid(P position);
		boolean empty(F fluid);
		boolean ignoresWater();
		boolean water(F fluid);
		double contact(P position,F fluid);
	}
	public static <P,F> double first(Iterable<P> positions,World<P,F> world) {
		double first=Double.POSITIVE_INFINITY;
		for(P position:positions) {
			if(!world.loaded(position))continue;
			F fluid=world.fluid(position);
			if(world.empty(fluid)||world.ignoresWater()&&world.water(fluid))continue;
			first=Math.min(first,world.contact(position,fluid));
		}
		return first;
	}
	private FluidContactScan() { }
}
