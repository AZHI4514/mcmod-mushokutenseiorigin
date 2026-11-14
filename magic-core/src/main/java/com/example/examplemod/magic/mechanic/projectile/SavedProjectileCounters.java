package com.example.examplemod.magic.mechanic.projectile;

/** Saved counter bounds only; no new lifetime restriction. */
public final class SavedProjectileCounters {
	public static int age(int saved) {return Math.max(0,saved);}
	public static int brokenBlocks(int saved) {return Math.clamp(saved,0,64);}
	private SavedProjectileCounters() { }
}
