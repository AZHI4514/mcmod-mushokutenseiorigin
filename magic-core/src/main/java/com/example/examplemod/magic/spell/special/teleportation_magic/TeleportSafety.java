package com.example.examplemod.magic.spell.special.teleportation_magic;
public final class TeleportSafety {
	public interface Spot {boolean loaded();boolean border();boolean collisionFree();boolean liquid();boolean reserved();boolean hazard();boolean safeFloor();}
	public static boolean safe(Spot s){return s.loaded()&&s.border()&&s.collisionFree()&&!s.liquid()&&!s.reserved()&&!s.hazard()&&s.safeFloor();}
	private TeleportSafety() { }
}
