package com.example.examplemod.magic.spell.water.cumulonimbus;
public final class CumulonimbusTick {
	public interface World {
		boolean statePresent();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();void removeState();void discard();
		boolean refreshNeeded();void refreshExposure();boolean strikeReady();void beginStrike();boolean selectStrike();void hitTargets();void burst();
	}
	public static void tick(World w){
		if(!w.statePresent()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}
		if(w.refreshNeeded())w.refreshExposure();if(!w.strikeReady())return;
		w.beginStrike();if(!w.selectStrike())return;w.hitTargets();w.burst();
	}
	private CumulonimbusTick() { }
}
