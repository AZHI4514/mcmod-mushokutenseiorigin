package com.example.examplemod.magic.spell.barrier.fortress_barrier;
public final class FortressUpkeep {
	public interface World {boolean due();void schedule();boolean takeEnergy();void clearDry();void repair();long drySince();long now();void beginDry();}
	public static boolean maintain(World w){if(!w.due())return true;w.schedule();if(w.takeEnergy()){w.clearDry();w.repair();return true;}if(w.drySince()==0)w.beginDry();return w.now()-w.drySince()<100;}
	public static float repair(float capacity){return Math.min(600,capacity+8);}
	private FortressUpkeep() { }
}
