package com.example.examplemod.magic.spell.barrier.physical_shield;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/** Keeps damage-event iteration distinct from projectile nearest-contact selection. */
public final class PhysicalShieldSelection {
	public record Contact<T>(T shield, double distance) { }
	public static <T> T firstIntersecting(Iterable<T> shields, Predicate<T> eligible, ToDoubleFunction<T> contact) {
		for (T shield : shields) {
			if (eligible.test(shield) && Double.isFinite(contact.applyAsDouble(shield))) return shield;
		}
		return null;
	}
	public static <T> Contact<T> nearest(Iterable<T> shields, Predicate<T> eligible, ToDoubleFunction<T> contact) {
		double first = Double.POSITIVE_INFINITY;
		T selected = null;
		for (T shield : shields) {
			if (!eligible.test(shield)) continue;
			double distance = contact.applyAsDouble(shield);
			if (distance < first) { first = distance; selected = shield; }
		}
		return selected == null ? null : new Contact<>(selected, first);
	}
	private PhysicalShieldSelection() { }
}
