package com.example.examplemod.magic.domain.model;

/** Small immutable finite vector; no world, entity or registry attached. */
public final class Vec3d {
	public static final Vec3d ZERO = new Vec3d(0, 0, 0);
	public final double x, y, z;
	public Vec3d(double x, double y, double z) {
		if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) throw new IllegalArgumentException("Non-finite vector");
		this.x = x; this.y = y; this.z = z;
	}
	public Vec3d add(Vec3d other) { return new Vec3d(x + other.x, y + other.y, z + other.z); }
	public Vec3d subtract(Vec3d other) { return new Vec3d(x - other.x, y - other.y, z - other.z); }
	public Vec3d scale(double value) { return new Vec3d(x * value, y * value, z * value); }
	public double dot(Vec3d other) { return x * other.x + y * other.y + z * other.z; }
	public Vec3d cross(Vec3d other) { return new Vec3d(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x); }
	public double lengthSqr() { return x * x + y * y + z * z; }
	public double length() { return Math.sqrt(lengthSqr()); }
	public double horizontalDistanceSqr() { return x * x + z * z; }
	public double horizontalDistance() { return Math.sqrt(horizontalDistanceSqr()); }
	public Vec3d normalize() {
		double length = length();
		return length < 1.0E-5 ? ZERO : new Vec3d(x / length, y / length, z / length);
	}
}
