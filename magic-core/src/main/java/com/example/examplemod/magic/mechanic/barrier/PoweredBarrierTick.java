package com.example.examplemod.magic.mechanic.barrier;
public final class PoweredBarrierTick {
	public interface World {boolean statePresent();boolean coreCountValid();boolean depleted();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();boolean coresValid();boolean maintain();void release();void visual();}
	public static void tick(World w){if(!w.statePresent()||!w.coreCountValid()||w.depleted()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()||!w.coresValid()){w.release();return;}if(!w.maintain()){w.release();return;}w.visual();}
	private PoweredBarrierTick() { }
}
