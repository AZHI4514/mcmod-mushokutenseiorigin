package com.example.examplemod.magic.spell.earth.earth_hedgehog;
public final class EarthHedgehogTick {
	public interface World {boolean statePresent();boolean ownerPresent();boolean alive();boolean sameLevel();boolean expired();boolean adopt();void removeState();void discard();boolean begin();void erupt();}
	public static void tick(World w){if(!w.statePresent()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||w.expired()||!w.adopt()){w.removeState();w.discard();return;}if(w.begin())w.erupt();}
	private EarthHedgehogTick() { }
}
