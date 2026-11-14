package com.example.examplemod.magic.mechanic.projectile;

/** One prepared server sweep. World operations remain synchronous and irreversible. */
public final class ProjectileServerStep {
	public interface World {
		boolean loaded();
		void unloaded();
		void advanceAge();
		void solidsAndTerrain();
		void fluids();
		boolean targets();
		boolean terminate();
		void move();
	}
	public static void apply(World world) {
		if(!world.loaded()) {world.unloaded();return;}
		world.advanceAge();
		world.solidsAndTerrain();
		world.fluids();
		if(world.targets())return;
		if(world.terminate())return;
		world.move();
	}
	private ProjectileServerStep() { }
}
