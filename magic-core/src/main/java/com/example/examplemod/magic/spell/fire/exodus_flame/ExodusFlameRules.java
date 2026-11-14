package com.example.examplemod.magic.spell.fire.exodus_flame;

/** Preserve the legacy negated outside test, including NaN comparison behavior. */
public final class ExodusFlameRules {
	public static final double BODY_SIZE=3, BURST_RADIUS=4;
	public static boolean insideBurst(double distanceSquared) {
		return !(distanceSquared > BURST_RADIUS * BURST_RADIUS);
	}
	private ExodusFlameRules() { }
}
