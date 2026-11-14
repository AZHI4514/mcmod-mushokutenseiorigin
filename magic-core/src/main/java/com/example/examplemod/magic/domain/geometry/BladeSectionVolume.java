package com.example.examplemod.magic.domain.geometry;

import java.util.ArrayList;
import java.util.List;
import com.example.examplemod.magic.domain.model.Box3d;
import com.example.examplemod.magic.domain.model.Vec3d;

/** 与准星朝向一致的3宽×1高×1厚长方体：连续实体扫掠、完整截面遮挡。 */
public final class BladeSectionVolume {
	private static final double EPS = 1.0E-8;
	private static final Vec3d[] WORLD_AXES = {new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, 1)};
	public final Vec3d forward;
	public final Vec3d side;
	public final Vec3d up;
	private final List<Vec3d> axes = new ArrayList<>();

	public BladeSectionVolume(Vec3d direction, float yaw) {
		forward = direction.normalize();
		double angle = Math.toRadians(yaw);
		side = new Vec3d(Math.cos(angle), 0, -Math.sin(angle));
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
		return Math.abs(side.dot(axis)) * 1.5 + Math.abs(up.dot(axis)) * 0.5 + Math.abs(forward.dot(axis)) * 0.5;
	}

	public Box3d bounds(Vec3d center) {
		return new Box3d(center.x - radius(WORLD_AXES[0]), center.y - radius(WORLD_AXES[1]), center.z - radius(WORLD_AXES[2]),
				center.x + radius(WORLD_AXES[0]), center.y + radius(WORLD_AXES[1]), center.z + radius(WORLD_AXES[2]));
	}

	/** 固定朝向长方体沿forward平移，与生物Box3d的连续分离轴求交，不依赖中心射线。 */
	public double contact(Box3d target, Vec3d from, double distance) {
		var relative = target.getCenter().subtract(from);
		double hx = (target.maxX - target.minX) * 0.5;
		double hy = (target.maxY - target.minY) * 0.5;
		double hz = (target.maxZ - target.minZ) * 0.5;
		double enter = 0, leave = distance;
		for (var axis : axes) {
			double size = radius(axis) + hx * Math.abs(axis.x) + hy * Math.abs(axis.y) + hz * Math.abs(axis.z);
			double position = relative.dot(axis), velocity = forward.dot(axis);
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

	public boolean intersects(Box3d target, Vec3d center) { return Double.isFinite(contact(target, center, 0)); }

	/** 用凸多边形裁剪精确减去方块覆盖面积，不能用“碰到任意方块”代替完全遮挡。 */
	public boolean fullyBlocked(Vec3d center, List<Box3d> blocks) {
		List<List<Point>> uncovered = new ArrayList<>();
		uncovered.add(List.of(new Point(-1.5, -0.5), new Point(1.5, -0.5), new Point(1.5, 0.5), new Point(-1.5, 0.5)));
		for (var box : blocks) {
			List<List<Point>> remaining = new ArrayList<>();
			for (var polygon : uncovered) {
				List<Point> inside = polygon;
				for (int axis = 0; axis < 3 && !inside.isEmpty(); axis++) {
					double a = component(side, axis), b = component(up, axis), c = component(center, axis);
					double min = axis == 0 ? box.minX : axis == 1 ? box.minY : box.minZ;
					double max = axis == 0 ? box.maxX : axis == 1 ? box.maxY : box.maxZ;
					for (int sign : new int[]{1, -1}) {
						double limit = sign == 1 ? max - c : c - min;
						if (Math.abs(a) + Math.abs(b) < EPS) {
							if (limit < -EPS) { remaining.add(inside); inside = List.of(); }
							continue;
						}
						var outside = clip(inside, -sign * a, -sign * b, -limit);
						if (area(outside) > EPS) remaining.add(outside);
						inside = clip(inside, sign * a, sign * b, limit);
					}
				}
			}
			uncovered = remaining;
			if (uncovered.isEmpty()) return true;
		}
		return false;
	}

	/** 以1/32格步长检查飞行体厚度覆盖的截面；部分遮挡不改变形状或删除方块。 */
	public double blockingDistance(Vec3d from, double distance, List<Box3d> blocks) {
		if (blocks.isEmpty()) return Double.POSITIVE_INFINITY;
		int steps = Math.max(1, (int) Math.ceil((distance + 1) * 32));
		for (int i = 0; i <= steps; i++) {
			double plane = -0.5 + (distance + 1) * i / steps;
			if (fullyBlocked(from.add(forward.scale(plane)), blocks)) return Math.max(0, plane - 0.5);
		}
		return Double.POSITIVE_INFINITY;
	}

	private static double component(Vec3d vector, int axis) { return axis == 0 ? vector.x : axis == 1 ? vector.y : vector.z; }
	private record Point(double x, double y) { }

	private static List<Point> clip(List<Point> polygon, double a, double b, double limit) {
		if (polygon.isEmpty()) return polygon;
		List<Point> result = new ArrayList<>();
		Point previous = polygon.getLast();
		double p = a * previous.x + b * previous.y - limit;
		for (var current : polygon) {
			double q = a * current.x + b * current.y - limit;
			boolean inP = p <= EPS, inQ = q <= EPS;
			if (inP != inQ) {
				double t = p / (p - q);
				result.add(new Point(previous.x + t * (current.x - previous.x), previous.y + t * (current.y - previous.y)));
			}
			if (inQ) result.add(current);
			previous = current; p = q;
		}
		return result;
	}

	private static double area(List<Point> polygon) {
		if (polygon.size() < 3) return 0;
		double result = 0;
		Point previous = polygon.getLast();
		for (var current : polygon) {
			result += previous.x * current.y - current.x * previous.y;
			previous = current;
		}
		return Math.abs(result) * 0.5;
	}
}
