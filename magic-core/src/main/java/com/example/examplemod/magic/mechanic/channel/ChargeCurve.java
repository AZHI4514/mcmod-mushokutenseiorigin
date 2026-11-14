package com.example.examplemod.magic.mechanic.channel;

/** Rock cannon whole-MP integral; input is elapsed server ticks, not seconds. */
public final class ChargeCurve {
	public static int display(int paid) { return Math.max(0, paid); }
	public static long extraAfterTicks(int ticks) {
		long t = Math.max(0, ticks);
		return (40 * t + t * t) / 80;
	}
	public static float stoneDamage(int paid) { return 10 + Math.max(0, paid - 10) / 10; }
	private ChargeCurve() { }
}
