package com.example.examplemod.magic.mechanic.field;
/** The flying sand branch cannot also apply stationary fog during its impact tick. */
public final class SmokeFieldTick {
	public interface World {boolean statePresent();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();void removeState();void discard();boolean flying();void projectile();boolean pulseDue();void schedule();void contacts(boolean pulse);void visual();}
	public static void tick(World w){
		if(!w.statePresent()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}
		if(w.flying()){w.projectile();return;}
		boolean pulse=w.pulseDue();if(pulse)w.schedule();w.contacts(pulse);w.visual();
	}
	private SmokeFieldTick() { }
}
