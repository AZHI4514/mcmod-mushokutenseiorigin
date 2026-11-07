package com.example.examplemod.magic;

/** 独立可测的等级时长、续期和护盾数值规则；均以服务端tick为单位。 */
public final class MagicRules {
	public static int flightTicks(int rank) { return Math.clamp(rank, 1, 7) * 60; }
	public static int statusTicks(int rank) { return Math.clamp(rank, 1, 7) * 100; }
	public static int extend(int remaining, int duration) {
		return (int) Math.min(Integer.MAX_VALUE, (long) Math.max(0, remaining) + duration);
	}
	public static float shieldAfter(float remaining, float incoming) { return Math.max(0, remaining - Math.max(0, incoming)); }
	/** 包围盒到环心的最近/最远水平距离区间必须与火环相交。 */
	public static boolean touchesRing(net.minecraft.world.phys.AABB box, net.minecraft.world.phys.Vec3 center, double radius) {
		if (box.maxY < center.y || box.minY > center.y + 0.65) return false;
		double nearX = Math.clamp(center.x, box.minX, box.maxX) - center.x;
		double nearZ = Math.clamp(center.z, box.minZ, box.maxZ) - center.z;
		double farX = Math.max(Math.abs(box.minX-center.x), Math.abs(box.maxX-center.x));
		double farZ = Math.max(Math.abs(box.minZ-center.z), Math.abs(box.maxZ-center.z));
		double inner = Math.max(0, radius-0.15), outer = radius+0.15;
		return nearX*nearX+nearZ*nearZ <= outer*outer && farX*farX+farZ*farZ >= inner*inner;
	}
	private MagicRules() { }
}
