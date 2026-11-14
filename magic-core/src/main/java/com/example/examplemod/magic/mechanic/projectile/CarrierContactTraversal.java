package com.example.examplemod.magic.mechanic.projectile;

/** Ordered contacts with separate explosion and ordinary presentation lifecycles. */
public final class CarrierContactTraversal {
	public interface World<T> {
		double contact(T target);
		boolean skip(T target, double contact);
		void claim(T target);
		boolean explosion();
		void position(double contact);
		boolean piercing();
		void explode(boolean keepFlying);
		void effect(T target);
		void burst(double contact);
		void discard();
	}
	public static <T> boolean apply(Iterable<T> targets, World<T> world) {
		for (T target : targets) {
			double contact=world.contact(target);
			if (world.skip(target,contact)) continue;
			world.claim(target);
			if (world.explosion()) {
				world.position(contact);
				world.explode(world.piercing());
				if (!world.piercing()) return true;
				continue;
			}
			world.effect(target);
			world.burst(contact);
			if (!world.piercing()) { world.discard(); return true; }
		}
		return false;
	}
	private CarrierContactTraversal() { }
}
