package com.example.examplemod.magic.spell.water.water_splash;
import com.example.examplemod.magic.domain.model.*;
public final class WaterSplashRules {
	public static Vec3d center(Vec3d origin,Vec3d direction,Vec3d blockHit) {
		return blockHit==null ? origin.add(direction.scale(WaterSplashSpec.REACH)) : blockHit.subtract(direction.scale(.02));
	}
	public static Box3d bounds(Vec3d center) { return new Box3d(center.x-6,center.y-6,center.z-6,center.x+6,center.y+6,center.z+6); }
	public static boolean affects(Vec3d center,Vec3d bodyCenter,boolean visible) { return visible && bodyCenter.subtract(center).lengthSqr()<=36; }
	private WaterSplashRules() { }
}
