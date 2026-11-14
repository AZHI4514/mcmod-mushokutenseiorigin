package com.example.examplemod.magic.spell.barrier.spirit_sealing_barrier;
public final class SpiritSealingBarrierRules {
	public static final double RADIUS=8;
	public static boolean strikesShell(double distance){return Math.abs(distance-8)<1;}
	public static float strikeCost(double attribute){return Math.max(1,(float)attribute);}
	private SpiritSealingBarrierRules() { }
}
