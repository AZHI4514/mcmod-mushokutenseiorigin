package com.example.examplemod.magic.spell.fire.exodus_flame;
/** Original strict candidate and inclusive stopping boundaries. */
public final class ExodusFlameFlight {
	public static boolean expired(int age,int flightTicks,double motionSquared) {
		return age>=flightTicks || motionSquared<1.0E-8;
	}
	public static boolean candidate(double contact,double wall,double fluid) {
		return contact<Math.min(wall,fluid);
	}
	public static boolean skipContact(double contact,double distance,boolean alreadyHit) {
		return contact>distance || alreadyHit;
	}
	private ExodusFlameFlight() { }
}
