package com.example.examplemod.magic.spell.water.squall;
public final class SquallTick {
	public interface World {
		boolean ownerPresent();boolean alive();boolean removed();boolean sameLevel();boolean expired();boolean adopt();void removeState();void discard();int age();void apply();
	}
	public static void tick(World world){if(!world.ownerPresent()||!world.alive()||world.removed()||!world.sameLevel()||world.expired()||!world.adopt()){world.removeState();world.discard();return;}if(SquallRules.pulse(world.age()))world.apply();}
	private SquallTick() { }
}
