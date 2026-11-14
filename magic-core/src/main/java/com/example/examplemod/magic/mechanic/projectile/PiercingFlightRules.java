package com.example.examplemod.magic.mechanic.projectile;
/** Saved remaining budget is authoritative; do not reset it to a fresh 120 ticks on load. */
public final class PiercingFlightRules {
	public static boolean expired(int age,int savedLimit) { return age>=savedLimit; }
	public static boolean candidate(double contact,double wall,double fluid,boolean alreadyHit) {
		return !alreadyHit&&contact<Math.min(wall,fluid);
	}
	public static boolean contactDue(double contact,double distance,boolean alreadyHit) {
		return !(contact>distance||alreadyHit);
	}
	private PiercingFlightRules() { }
}
