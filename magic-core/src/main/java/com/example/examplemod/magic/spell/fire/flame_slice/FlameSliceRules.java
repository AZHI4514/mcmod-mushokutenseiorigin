package com.example.examplemod.magic.spell.fire.flame_slice;
import com.example.examplemod.magic.domain.model.Box3d;
public final class FlameSliceRules {
	public static boolean expired(int age, FlameSliceFlightState flight, double motionSquared) {
		return age >= FlameSliceSpec.FLIGHT_TICKS || flight.exhausted() || motionSquared < 1E-8;
	}
	public static boolean canTrigger(double contact, double wall, double distance, boolean alreadyHit) {
		return !(contact >= wall || contact > distance || alreadyHit);
	}
	public static Box3d impactArea(double x, double minY, double z, double maxY) {
		return new Box3d(x-1.5,minY,z-1.5,x+1.5,maxY,z+1.5);
	}
	public static int ignitionTicks(float roll, boolean fireImmune, boolean resistance) {
		return roll < FlameSliceSpec.IGNITION_CHANCE && !fireImmune && !resistance ? FlameSliceSpec.IGNITION_TICKS : 0;
	}
	private FlameSliceRules() { }
}
