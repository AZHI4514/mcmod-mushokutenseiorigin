package com.example.examplemod.magic.mechanic.projectile;

public final class CarrierTermination {
	public interface World {
		void discard();
		void explosionPosition(double distance);
		void explode();
		void wallBurst(double distance);
	}
	public static boolean apply(boolean explosion,double distance,double wall,double fluid,boolean hasBlock,World world) {
		if (explosion) {
			var stop=com.example.examplemod.magic.spell.fire.explosion.ExplosionImpact.stop(distance,wall,fluid,hasBlock);
			if(stop==com.example.examplemod.magic.spell.fire.explosion.ExplosionImpact.Stop.FLUID){world.discard();return true;}
			if(stop==com.example.examplemod.magic.spell.fire.explosion.ExplosionImpact.Stop.SOLID){world.explosionPosition(Math.max(0,wall-0.02));world.explode();return true;}
		}
		if(fluid<=distance&&fluid<=wall){world.discard();return true;}
		if(wall<=distance&&hasBlock){world.wallBurst(wall);world.discard();return true;}
		return false;
	}
	private CarrierTermination() { }
}
