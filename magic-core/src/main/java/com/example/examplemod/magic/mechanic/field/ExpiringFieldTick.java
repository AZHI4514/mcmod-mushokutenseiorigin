package com.example.examplemod.magic.mechanic.field;
public final class ExpiringFieldTick {
	public interface World {boolean statePresent();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();void removeState();void discard();void update();}
	public static void tick(World w){if(!w.statePresent()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}w.update();}
	private ExpiringFieldTick() { }
}
