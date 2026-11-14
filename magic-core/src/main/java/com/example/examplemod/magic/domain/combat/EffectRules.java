package com.example.examplemod.magic.domain.combat;

/** Pure numeric rules; retain historical saturation and IEEE float behavior. */
public final class EffectRules {
	public static int flightTicks(int rank) { return Math.clamp(rank, 1, 7) * 60; }
	public static int statusTicks(int rank) { return Math.clamp(rank, 1, 7) * 100; }
	public static int extend(int remaining, int duration) {
		return (int) Math.min(Integer.MAX_VALUE, (long) Math.max(0, remaining) + duration);
	}
	public static float shieldAfter(float remaining, float incoming) {
		return Math.max(0, remaining - Math.max(0, incoming));
	}
	public static int freezeTicks(float roll,float chance,int duration,boolean canFreeze,boolean warm) {
		return roll < chance && canFreeze && !warm ? duration : 0;
	}
	private EffectRules() { }
}
