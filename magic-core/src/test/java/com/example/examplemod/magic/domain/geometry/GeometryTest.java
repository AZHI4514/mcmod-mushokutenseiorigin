package com.example.examplemod.magic.domain.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.model.Box3d;

class GeometryTest {
	@Test void catchesFastOutsideToOutsideCrossing() {
		assertEquals(.35, SphereCrossing.sphere(Vec3d.ZERO, 3, false, new Vec3d(-10,0,0), new Vec3d(20,0,0)), 1E-10);
		assertEquals(Double.POSITIVE_INFINITY, SphereCrossing.sphere(Vec3d.ZERO, 3, false, new Vec3d(-10,4,0), new Vec3d(20,0,0)));
		assertEquals(.5, SphereCrossing.sphere(Vec3d.ZERO, 3, true, new Vec3d(0,-2,0), new Vec3d(0,4,0)), 1E-10);
	}
	@Test void volumeIncludesFlankInsteadOfUsingCenterRay() {
		var volume = new SweptVolume(new Vec3d(0,0,1), 3, 3, 3);
		assertTrue(Double.isFinite(volume.contact(new Box3d(1,-.2,5,1.2,.2,6), Vec3d.ZERO, new Vec3d(0,0,1), 10)));
		assertEquals(Double.POSITIVE_INFINITY, volume.contact(new Box3d(2,-.2,5,3,.2,6), Vec3d.ZERO, new Vec3d(0,0,1), 10));
	}
	@Test void vectorBoundaryRejectsNonFiniteInput() {
		assertThrows(IllegalArgumentException.class, () -> new Vec3d(Double.NaN, 0, 0));
		assertThrows(IllegalArgumentException.class, () -> new Box3d(0,0,0,Double.POSITIVE_INFINITY,1,1));
	}
}
