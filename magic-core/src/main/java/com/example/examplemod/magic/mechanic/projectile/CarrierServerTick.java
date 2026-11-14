package com.example.examplemod.magic.mechanic.projectile;

/** Server preconditions and dispatch; world-dependent reads stay lazy and ordered. */
public final class CarrierServerTick {
	public interface World {
		boolean belowMinimum();
		void discard();
		boolean loadedCurrent();
		void resolveOwner();
		boolean stationary();
		boolean hasOwner();
		boolean loadedAhead();
		boolean expired();
		void advanceAge();
		boolean shield();
		void tickShield();
		void tickStationary();
		boolean fallingRock();
		void fall();
		void fly();
	}

	public static void apply(World world) {
		if (world.belowMinimum()) { world.discard(); return; }
		if (!world.loadedCurrent()) return;
		world.resolveOwner();
		if (world.stationary() && !world.hasOwner()) return;
		if (!world.stationary() && !world.loadedAhead()) return;
		if (world.expired()) { world.discard(); return; }
		world.advanceAge();
		if (world.shield()) { world.tickShield(); return; }
		if (world.stationary()) { world.tickStationary(); return; }
		if (world.fallingRock()) { world.fall(); return; }
		world.fly();
	}

	private CarrierServerTick() { }
}
