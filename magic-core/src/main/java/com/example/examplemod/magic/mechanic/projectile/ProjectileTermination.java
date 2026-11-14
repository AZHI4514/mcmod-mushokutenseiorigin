package com.example.examplemod.magic.mechanic.projectile;

/** Final sweep boundaries; fluid wins ties with solid contact. */
public final class ProjectileTermination {
	public static boolean fluid(double contact,double distance,double wall) {
		return contact<=distance&&contact<=wall;
	}
	public static boolean wall(double contact,double distance,boolean hasBlock) {
		return contact<=distance&&hasBlock;
	}
	public interface World {
		void position(double contact);
		void wallEffect();
		void particles();
		void discard();
	}
	/** Returns true only after a terminal branch finishes; partial failures propagate. */
	public static boolean apply(double fluid,double wall,double distance,boolean hasBlock,World world) {
		if (fluid(fluid,distance,wall)) {
			world.position(fluid);
			world.particles();
			world.discard();
			return true;
		}
		if (wall(wall,distance,hasBlock)) {
			world.position(wall);
			world.wallEffect();
			world.particles();
			world.discard();
			return true;
		}
		return false;
	}
	private ProjectileTermination() { }
}
