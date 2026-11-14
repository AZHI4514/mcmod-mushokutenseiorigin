package com.example.examplemod.magic.mechanic.field;
public final class WindFieldTick {
	public interface World {boolean statePresent();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();void removeState();void discard();boolean move();boolean pulse();boolean suppressed();void contacts(boolean pulse);void interact(boolean pulse);}
	public static void tick(World w){if(!w.statePresent()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}if(!w.move()){w.discard();return;}boolean pulse=w.pulse();if(w.suppressed())return;w.contacts(pulse);w.interact(pulse);}
	private WindFieldTick() { }
}
