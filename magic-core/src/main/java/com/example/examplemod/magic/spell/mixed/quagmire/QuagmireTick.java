package com.example.examplemod.magic.spell.mixed.quagmire;
public final class QuagmireTick {
	public interface World {boolean ownerPresent();boolean alive();boolean sameLevel();boolean expired();boolean adopt();void removeState();void discard();void contacts();}
	public static void tick(World w){if(!w.ownerPresent()||!w.alive()||!w.sameLevel()||w.expired()||!w.adopt()){w.removeState();w.discard();return;}w.contacts();}
	private QuagmireTick() { }
}
