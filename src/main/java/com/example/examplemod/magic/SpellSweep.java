package com.example.examplemod.magic;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.magic.domain.geometry.SweptVolume;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;

/** Temporary game-facing facade; sweep arithmetic has a single implementation in core. */
public final class SpellSweep {
	public final Vec3 forward, side, up;
	private final SweptVolume volume;
	public SpellSweep(Vec3 direction, double width, double height, double depth) {
		volume = new SweptVolume(GeometryAdapter.vector(direction), width, height, depth);
		forward = GeometryAdapter.vector(volume.forward);
		side = GeometryAdapter.vector(volume.side);
		up = GeometryAdapter.vector(volume.up);
	}
	public AABB bounds(Vec3 center) { return GeometryAdapter.box(volume.bounds(GeometryAdapter.vector(center))); }
	public double contact(AABB target, Vec3 from, Vec3 motion, double distance) {
		return volume.contact(GeometryAdapter.box(target), GeometryAdapter.vector(from), GeometryAdapter.vector(motion), distance);
	}
	public boolean intersects(AABB target, Vec3 center) { return volume.intersects(GeometryAdapter.box(target), GeometryAdapter.vector(center)); }
}
