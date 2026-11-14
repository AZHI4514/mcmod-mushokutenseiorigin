package com.example.examplemod.magic.spell.earth.stone_cannon;
/** Path decisions only; collision geometry, permissions and hit UUID persistence remain adapters. */
public final class StoneCannonRules {
	public static int paidMana(int value) { return Math.max(10,value); }
	public static boolean expired(int age,double motionSquared) {
		return age>=StoneCannonSpec.FLIGHT_TICKS || motionSquared<1E-8;
	}
	public static boolean directContact(double contact,double wall,double fluid,boolean alreadyHit) {
		return !alreadyHit && contact<Math.min(wall,fluid);
	}
	private StoneCannonRules() { }
}
