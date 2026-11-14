package com.example.examplemod.magic.mechanic.field;
public final class GroundFieldTick {
	public interface World {
		boolean statePresent();boolean expired();boolean ownerPresent();boolean alive();boolean sameLevel();boolean adopt();void removeState();void discard();
		void move();int pulse();int lastPulse();void commit(int pulse);void terrain(int pulse);boolean continuousContacts();void contacts(int pulse,boolean attempt);boolean visualDue();void visual();
	}
	public static void tick(World w){
		if(!w.statePresent()||w.expired()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||!w.adopt()){w.removeState();w.discard();return;}
		w.move();int pulse=w.pulse();boolean attempt=pulse>=0&&pulse>w.lastPulse();
		if(attempt){w.commit(pulse);w.terrain(pulse);}
		if(attempt||w.continuousContacts())w.contacts(pulse,attempt);
		if(w.visualDue())w.visual();
	}
	private GroundFieldTick() { }
}
