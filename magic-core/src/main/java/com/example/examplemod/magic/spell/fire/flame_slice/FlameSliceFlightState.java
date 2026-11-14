package com.example.examplemod.magic.spell.fire.flame_slice;
/** Immutable travelled-distance budget; storage encoding is a game-adapter concern. */
public record FlameSliceFlightState(double travelled) {
	public FlameSliceFlightState {
		travelled = Double.isFinite(travelled) ? Math.max(0,travelled) : FlameSliceSpec.RANGE;
	}
	public FlameSliceFlightState advance(double distance) { return new FlameSliceFlightState(travelled + Math.max(0,distance)); }
	public double remaining() { return Math.max(0,FlameSliceSpec.RANGE-travelled); }
	public boolean exhausted() { return remaining() <= 1E-6; }
}
