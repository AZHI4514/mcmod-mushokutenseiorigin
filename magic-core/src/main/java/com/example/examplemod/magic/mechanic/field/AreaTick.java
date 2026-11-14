package com.example.examplemod.magic.mechanic.field;
/** Short-lived fields only. Specialized persistent fields retain their own lifecycle. */
public final class AreaTick {
	public interface World {
		boolean ownerPresent(); boolean ownerRemoved(); boolean ownerAlive(); boolean sameLevel();
		int decrementRemaining(); boolean loaded(); void discard();
		boolean stationary(); double displacementSquared(); void breakMaintenance(); void followOwner();
		void advancePulse(); void pruneContacts(); void applyContacts();
	}
	public static void tick(World world) {
		if(!world.ownerPresent()||world.ownerRemoved()||!world.ownerAlive()||!world.sameLevel()||world.decrementRemaining()<=0||!world.loaded()){world.discard();return;}
		if(world.stationary()&&world.displacementSquared()>=1.0E-6)world.breakMaintenance();
		if(!world.stationary())world.followOwner();
		world.advancePulse();world.pruneContacts();world.applyContacts();
	}
	private AreaTick() { }
}
