package com.example.examplemod.magic;

import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.domain.geometry.SphereCrossing;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;

/** Temporary compatibility facade; remove after all barrier callers use the core boundary. */
public final class BarrierGeometry {
	public static double sphere(Vec3 center, double radius, boolean hemisphere, Vec3 from, Vec3 movement) {
		return SphereCrossing.sphere(GeometryAdapter.vector(center), radius, hemisphere, GeometryAdapter.vector(from), GeometryAdapter.vector(movement));
	}
	private BarrierGeometry() { }
}
