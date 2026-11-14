package com.example.examplemod.magic.domain.player;

/** Balance and recovery numeric bounds; no player, clock or save access. */
public final class ManaPolicy {
	public static final int RECOVERY_TICKS = 24000;
	public static int maximum(int value) { return Math.clamp(value, 1, 1000000); }
	public static int balance(int value, int maximum) { return Math.clamp(value, 0, maximum); }
	public static int restore(int mana, int maximum, int amount) {
		return (int) Math.min(maximum, mana + (long) Math.max(0, amount));
	}
	private ManaPolicy() { }
}
