package com.example.examplemod.magic;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter;
/** Compatibility boundary; all collision arithmetic lives in the pure spell module. */
public final class FlameSliceVolume {
	private final com.example.examplemod.magic.domain.geometry.BladeSectionVolume volume;
	public final Vec3 forward, side, up;
	public FlameSliceVolume(Vec3 direction,float yaw) {
		volume=new com.example.examplemod.magic.domain.geometry.BladeSectionVolume(GeometryAdapter.vector(direction),yaw);
		forward=GeometryAdapter.vector(volume.forward);side=GeometryAdapter.vector(volume.side);up=GeometryAdapter.vector(volume.up);
	}
	public AABB bounds(Vec3 center) { return GeometryAdapter.box(volume.bounds(GeometryAdapter.vector(center))); }
	public double contact(AABB target,Vec3 from,double distance) { return volume.contact(GeometryAdapter.box(target),GeometryAdapter.vector(from),distance); }
	public boolean intersects(AABB target,Vec3 center) { return volume.intersects(GeometryAdapter.box(target),GeometryAdapter.vector(center)); }
	public boolean fullyBlocked(Vec3 center,List<AABB> blocks) { return volume.fullyBlocked(GeometryAdapter.vector(center),blocks.stream().map(GeometryAdapter::box).toList()); }
	public double blockingDistance(Vec3 from,double distance,List<AABB> blocks) { return volume.blockingDistance(GeometryAdapter.vector(from),distance,blocks.stream().map(GeometryAdapter::box).toList()); }
}
