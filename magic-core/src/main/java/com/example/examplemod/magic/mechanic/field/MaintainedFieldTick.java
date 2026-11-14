package com.example.examplemod.magic.mechanic.field;
/** Commit the next upkeep time before attempting payment; failed upkeep only discards the entity. */
public final class MaintainedFieldTick {
	public interface World {boolean statePresent();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();void removeState();void discard();void follow();boolean pulseDue();void schedule();boolean payUpkeep();void update(boolean pulse);}
	public static void tick(World w){
		if(!w.statePresent()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}
		w.follow();boolean pulse=w.pulseDue();if(pulse){w.schedule();if(!w.payUpkeep()){w.discard();return;}}w.update(pulse);
	}
	private MaintainedFieldTick() { }
}
