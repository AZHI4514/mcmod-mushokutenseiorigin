package com.example.examplemod.magic.mechanic.projectile;

import java.util.ArrayList;

/** Resolve hits before placing terrain that could obstruct later targets. */
public final class DeferredAreaImpact {
	public interface World<T> {
		Iterable<T> targets();
		boolean affects(T target);
		boolean wet(T target);
		boolean hurt(T target);
		boolean applyEffects(T target, boolean wasWet);
		void afterHit(T target);
		void encase(T target);
		void burst();
	}

	public static <T> void apply(World<T> world) {
		var encased = new ArrayList<T>();
		for (var target : world.targets()) {
			if (!world.affects(target)) continue;
			boolean wet = world.wet(target);
			if (!world.hurt(target)) continue;
			if (world.applyEffects(target, wet)) encased.add(target);
			world.afterHit(target);
		}
		encased.forEach(world::encase);
		world.burst();
	}

	private DeferredAreaImpact() { }
}
