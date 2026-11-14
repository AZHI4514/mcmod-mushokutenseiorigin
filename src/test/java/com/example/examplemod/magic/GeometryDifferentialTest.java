package com.example.examplemod.magic;

import java.util.Random;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

/** Differential migration evidence against the actual frozen source, not copied expected answers. */
public final class GeometryDifferentialTest {
	private static void equal(double old, double actual) {
		if (Double.isNaN(old) || Double.isNaN(actual) || (Double.doubleToLongBits(old) != Double.doubleToLongBits(actual)
				&& (!Double.isFinite(old) || !Double.isFinite(actual) || Math.abs(old - actual) > 1E-10)))
			throw new AssertionError("Geometry drift: " + old + " vs " + actual);
	}
	private static Vec3 vector(Random r) { return new Vec3(r.nextDouble(-20, 20), r.nextDouble(-20, 20), r.nextDouble(-20, 20)); }
	public static void main(String[] args) {
		var r = new Random(1110);
		for (int i = 0; i < 10000; i++) {
			var direction = i % 20 == 0 ? Vec3.ZERO : vector(r);
			double w = r.nextDouble(.01, 8), h = r.nextDouble(.01, 8), d = r.nextDouble(.01, 8);
			var old = new FrozenSpellSweep(direction, w, h, d);
			var actual = new SpellSweep(direction, w, h, d);
			var a = vector(r); var b = vector(r); var from = vector(r); var motion = vector(r);
			var box = new AABB(a, b);
			double distance = r.nextDouble(0, 40);
			equal(old.contact(box, from, motion, distance), actual.contact(box, from, motion, distance));
			var boundsOld = old.bounds(from); var boundsNew = actual.bounds(from);
			equal(boundsOld.minX, boundsNew.minX); equal(boundsOld.minY, boundsNew.minY); equal(boundsOld.minZ, boundsNew.minZ);
			equal(boundsOld.maxX, boundsNew.maxX); equal(boundsOld.maxY, boundsNew.maxY); equal(boundsOld.maxZ, boundsNew.maxZ);
			double radius = r.nextDouble(.01, 10);
			for (boolean hemi : new boolean[]{false, true})
				equal(FrozenBarrierGeometry.sphere(a, radius, hemi, from, motion), BarrierGeometry.sphere(a, radius, hemi, from, motion));
		}
		System.out.println("Geometry: 10,000 frozen-old/new sweep scenarios and 20,000 sphere crossings matched.");
	}
}
