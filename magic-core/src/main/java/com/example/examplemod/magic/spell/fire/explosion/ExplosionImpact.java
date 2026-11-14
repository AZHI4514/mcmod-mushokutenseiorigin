package com.example.examplemod.magic.spell.fire.explosion;
/** Explosion-specific coordination; vanilla remains responsible for all blast consequences. */
public final class ExplosionImpact {
	public interface World {
		void burst();
		void explode(float power);
		void discard();
	}
	public enum Stop { NONE, FLUID, SOLID }
	public static Stop stop(double distance,double wall,double fluid,boolean hasBlock) {
		if(fluid<=distance&&fluid<=wall)return Stop.FLUID;
		if(wall<=distance&&hasBlock)return Stop.SOLID;
		return Stop.NONE;
	}
	public static void apply(boolean keepFlying,World world) {
		world.burst();
		world.explode(ExplosionSpec.TNT_POWER);
		if(!keepFlying)world.discard();
	}
	private ExplosionImpact() { }
}
