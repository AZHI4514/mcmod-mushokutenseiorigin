package com.example.examplemod.magic.spell.barrier.confinement_barrier;
public final class ConfinementBarrierRules {
	public static final double RADIUS=3;
	public static int lifetime(boolean player,int spellLifetime){return player?100:spellLifetime;}
	public static double clippedFraction(double crossing){return Math.max(0,crossing-.001);}
	private ConfinementBarrierRules() { }
}
