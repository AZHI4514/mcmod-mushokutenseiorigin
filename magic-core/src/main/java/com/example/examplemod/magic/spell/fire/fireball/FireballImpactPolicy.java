package com.example.examplemod.magic.spell.fire.fireball;

/** Called only after successful damage and target eligibility; random draw is supplied by the adapter. */
public final class FireballImpactPolicy {
	public static int ignitionTicks(float roll,boolean fireImmune,boolean resistance) {
		return roll < FireballSpec.IGNITION_CHANCE && !fireImmune && !resistance ? FireballSpec.IGNITION_TICKS : 0;
	}
	private FireballImpactPolicy() { }
}
