package com.example.examplemod.magic.mechanic.projectile;

/** Processes the already ordered candidates without caching their mutable contact. */
public final class ContactTraversal {
	public interface World<T> {
		double contact(T target);
		boolean skip(T target,double contact);
		void claim(T target);
		void position(double contact);
		void effect(T target);
		void particles();
		void discard();
	}
	public static <T> boolean apply(Iterable<T> ordered,boolean piercing,World<T> world) {
		for (T target:ordered) {
			double contact=world.contact(target);
			if (world.skip(target,contact)) continue;
			world.claim(target);
			world.position(contact);
			world.effect(target);
			if (piercing) world.particles();
			if (!piercing) {
				world.position(contact);
				world.particles();
				world.discard();
				return true;
			}
		}
		return false;
	}
	private ContactTraversal() { }
}
