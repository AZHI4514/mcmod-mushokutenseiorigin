package com.example.examplemod.magic.domain.geometry;

import java.util.ArrayList;
import java.util.List;
import com.example.examplemod.magic.domain.model.Box3d;
import com.example.examplemod.magic.domain.model.Vec3d;

/** 可配置大小与准星方向的连续体积碰撞。 */
public final class SweptVolume {
	private static final double EPS = 1.0E-8;
	private static final Vec3d[] WORLD_AXES = {new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, 1)};
	public final Vec3d forward;
	public final Vec3d side;
	public final Vec3d up;
	private final double halfWidth, halfHeight, halfDepth;
	private final List<Vec3d> axes = new ArrayList<>();

	public SweptVolume(Vec3d direction, double width, double height, double depth) {
		forward = direction.normalize(); halfWidth=width/2; halfHeight=height/2; halfDepth=depth/2;
		side = forward.horizontalDistance() < 1.0E-6 ? new Vec3d(1,0,0) : new Vec3d(forward.z,0,-forward.x).normalize();
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

	private double radius(Vec3d axis) {
		return Math.abs(side.dot(axis))*halfWidth + Math.abs(up.dot(axis))*halfHeight + Math.abs(forward.dot(axis))*halfDepth;
	}

	public Box3d bounds(Vec3d center) {
		return new Box3d(center.x - radius(WORLD_AXES[0]), center.y - radius(WORLD_AXES[1]), center.z - radius(WORLD_AXES[2]),
				center.x + radius(WORLD_AXES[0]), center.y + radius(WORLD_AXES[1]), center.z + radius(WORLD_AXES[2]));
	}

	/** 固定朝向长方体沿forward平移，与生物AABB的连续分离轴求交，不依赖中心射线。 */
	public double contact(Box3d target, Vec3d from, Vec3d motion, double distance) {
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

	public boolean intersects(Box3d target, Vec3d center) { return Double.isFinite(contact(target, center, Vec3d.ZERO, 0)); }

}
