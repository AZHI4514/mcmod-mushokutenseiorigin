package com.example.examplemod.magic;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** 3宽×2高×0.3厚水幕的连续分离轴碰撞。 */
public final class MagicWallVolume {
	private static final double EPS = 1.0E-8;
	private static final Vec3[] WORLD_AXES = {new Vec3(1, 0, 0), new Vec3(0, 1, 0), new Vec3(0, 0, 1)};
	public final Vec3 forward;
	public final Vec3 side;
	public final Vec3 up;
	private final List<Vec3> axes = new ArrayList<>();

	public MagicWallVolume(Vec3 direction, float yaw) {
		forward = direction.normalize();
		// 由原版实际方向向量求正交轴，避免三角函数查表误差。
		side = new Vec3(forward.z, 0, -forward.x).normalize();
		up = forward.cross(side).normalize();
		axes.addAll(List.of(WORLD_AXES));
		axes.addAll(List.of(side, up, forward));
		for (var local : List.of(side, up, forward)) {
			for (var world : WORLD_AXES) {
				var cross = local.cross(world);
				if (cross.lengthSqr() > EPS) axes.add(cross.normalize());
			}
		}
	}

	private double radius(Vec3 axis) {
		return Math.abs(side.dot(axis)) * 1.5 + Math.abs(up.dot(axis)) + Math.abs(forward.dot(axis)) * 0.15;
	}

	public AABB bounds(Vec3 center) {
		return new AABB(center.x - radius(WORLD_AXES[0]), center.y - radius(WORLD_AXES[1]), center.z - radius(WORLD_AXES[2]),
				center.x + radius(WORLD_AXES[0]), center.y + radius(WORLD_AXES[1]), center.z + radius(WORLD_AXES[2]));
	}

	/** 固定朝向长方体沿forward平移，与生物AABB的连续分离轴求交，不依赖中心射线。 */
	public double contact(AABB target, Vec3 from, Vec3 motion, double distance) {
		var relative = target.getCenter().subtract(from);
		double hx = (target.maxX - target.minX) * 0.5;
		double hy = (target.maxY - target.minY) * 0.5;
		double hz = (target.maxZ - target.minZ) * 0.5;
		double enter = 0, leave = distance;
		for (var axis : axes) {
			double size = radius(axis) + hx * Math.abs(axis.x) + hy * Math.abs(axis.y) + hz * Math.abs(axis.z);
			double position = relative.dot(axis), velocity = motion.dot(axis);
			if (Math.abs(velocity) < EPS) {
				if (Math.abs(position) > size + EPS) return Double.POSITIVE_INFINITY;
			} else {
				double a = (position - size) / velocity, b = (position + size) / velocity;
				enter = Math.max(enter, Math.min(a, b));
				leave = Math.min(leave, Math.max(a, b));
				if (enter > leave + EPS) return Double.POSITIVE_INFINITY;
			}
		}
		return enter <= distance + EPS ? Math.max(0, enter) : Double.POSITIVE_INFINITY;
	}

	public boolean intersects(AABB target, Vec3 center) { return Double.isFinite(contact(target, center, Vec3.ZERO, 0)); }

}
