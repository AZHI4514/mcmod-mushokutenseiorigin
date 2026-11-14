package com.example.examplemod.magic.domain.model;

/** Axis-aligned immutable finite box, preserving the source box ordering semantics. */
public final class Box3d {
	public final double minX, minY, minZ, maxX, maxY, maxZ;
	public Box3d(double x1, double y1, double z1, double x2, double y2, double z2) {
		if (!Double.isFinite(x1) || !Double.isFinite(y1) || !Double.isFinite(z1)
				|| !Double.isFinite(x2) || !Double.isFinite(y2) || !Double.isFinite(z2)) throw new IllegalArgumentException("Non-finite box");
		minX = Math.min(x1, x2); minY = Math.min(y1, y2); minZ = Math.min(z1, z2);
		maxX = Math.max(x1, x2); maxY = Math.max(y1, y2); maxZ = Math.max(z1, z2);
	}
	public Vec3d getCenter() { return new Vec3d((minX + maxX) * .5, (minY + maxY) * .5, (minZ + maxZ) * .5); }
	public Box3d inflate(double value) { return new Box3d(minX - value, minY - value, minZ - value, maxX + value, maxY + value, maxZ + value); }
	public boolean intersects(Box3d other) {
		return minX < other.maxX && maxX > other.minX && minY < other.maxY && maxY > other.minY && minZ < other.maxZ && maxZ > other.minZ;
	}
}
