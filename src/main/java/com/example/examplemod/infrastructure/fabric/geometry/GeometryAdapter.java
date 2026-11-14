package com.example.examplemod.infrastructure.fabric.geometry;

import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.model.Box3d;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

/** Boundary conversion only; all geometry rules live in magic-core. */
public final class GeometryAdapter {
	public static Vec3d vector(Vec3 v) { return new Vec3d(v.x, v.y, v.z); }
	public static Vec3 vector(Vec3d v) { return new Vec3(v.x, v.y, v.z); }
	public static Box3d box(AABB b) { return new Box3d(b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ); }
	public static AABB box(Box3d b) { return new AABB(b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ); }
	private GeometryAdapter() { }
}
