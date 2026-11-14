package com.example.examplemod.magic.spell.water.blizzard_storm;
public final class BlizzardStormTick {
	public interface World {
		boolean ownerPresent();boolean alive();boolean removed();boolean sameLevel();int pulse();
		void discard();void follow();boolean adopt();void hitTargets();void advance();
	}
	public static void tick(World world){
		if(!world.ownerPresent()||!world.alive()||world.removed()||!world.sameLevel()||world.pulse()>=80){world.discard();return;}
		world.follow();if(!world.adopt()){world.discard();return;}
		if(BlizzardStormRules.pulse(world.pulse()))world.hitTargets();
		world.advance();
	}
	private BlizzardStormTick() { }
}
