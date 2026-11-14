package com.example.examplemod.magic.spell.barrier.physical_shield;

/** Scalar rules preserve the world's vector and IEEE arithmetic without finite-vector conversion. */
public final class PhysicalShieldMovement {
	public static boolean canEscape(double side, double normalMotion) {
		return Math.abs(side) > 1.0E-6 && side * normalMotion >= 0;
	}
	public static boolean clips(double firstContact, double distance) {
		return firstContact < distance;
	}
	public static double scale(double firstContact, double distance) {
		return Math.max(0, firstContact - 0.001) / distance;
	}
	private PhysicalShieldMovement() { }
}
