package com.example.examplemod.magic.spell.water.icicle_field;
import com.example.examplemod.magic.domain.model.*;
/** Original collider endpoint and target-center exclusion, including IEEE comparisons. */
public final class IcicleFieldRules {
	public static Vec3d center(Vec3d origin,Vec3d direction,Vec3d hit) { return hit==null?origin.add(direction.scale(12)):hit.subtract(direction.scale(.02)); }
	public static Box3d bounds(Vec3d center) { return new Box3d(center.x-6,center.y-6,center.z-6,center.x+6,center.y+6,center.z+6); }
	public static boolean within(Vec3d center,Vec3d target) { return within(target.x-center.x,target.y-center.y,target.z-center.z); }
	public static boolean within(double dx,double dy,double dz) { return !(dx*dx+dy*dy+dz*dz>36); }
	private IcicleFieldRules() { }
}
