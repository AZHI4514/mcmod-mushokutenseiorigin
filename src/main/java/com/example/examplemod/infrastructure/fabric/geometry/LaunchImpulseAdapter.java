package com.example.examplemod.infrastructure.fabric.geometry;
import net.minecraft.world.phys.Vec3;
/** Vanilla vector normalization at the boundary; the height solver is pure. */
public final class LaunchImpulseAdapter {
	public static Vec3 launchVelocity(Vec3 direction,double distance) {
		var n=direction.normalize(); double vertical=n.y;
		double y=vertical>0?com.example.examplemod.magic.mechanic.projectile.LaunchBallistics.verticalLaunch(distance*vertical):vertical<0?-1.2*Math.abs(vertical):0.2;
		return new Vec3(n.x*1.35,y,n.z*1.35);
	}
	private LaunchImpulseAdapter() {}
}
