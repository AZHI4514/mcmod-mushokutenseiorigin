package com.example.examplemod.magic.spell.fire.flame_pillar;
/** Irreversible initial effects, preserving their original order before scheduling. */
public final class FlamePillarIgnition {
	public interface World {
		void ignite(int ticks);
		void damage();
		void placeFire();
		void burst();
		void schedule();
	}
	public static void apply(World world) {
		world.ignite(FlamePillarSpec.DURATION);
		world.damage();
		world.placeFire();
		world.burst();
		world.schedule();
	}
	private FlamePillarIgnition() { }
}
