package com.example.examplemod.magic;

import net.minecraft.world.phys.Vec3;

/** Fixed point channel accounting: units are twentieths of one mana point. */
public final class PhaseTwoRules {
	public static final int WIND_SLOW_FALL_TICKS = 100;
	public static final double WIND_LAUNCH_DISTANCE = 10;
	/** One initial impulse. Airborne 0.91 horizontal drag gives a nominal ten-block recoil. */
	public static Vec3 windLaunchVelocity(Vec3 direction) {
		var n=direction.normalize();
		double horizontal=WIND_LAUNCH_DISTANCE*(1-.91);
		double y=n.y>0?verticalLaunch(WIND_LAUNCH_DISTANCE*n.y):n.y<0?-1.2*Math.abs(n.y):.42;
		return new Vec3(n.x*horizontal,y,n.z*horizontal);
	}

	public static int debit(int debt,int rate) { return (debt+rate+19)/20; }
	public static int remainder(int debt,int rate,int debit) { return debt+rate-debit*20; }
	public static Vec3 boundMovement(Vec3 movement) {
		return Vec3.ZERO;
	}
	public static boolean inCone(Vec3 delta,Vec3 direction,double range) {
		double length=delta.length();
		return length<=range && (length<1.0E-6 || delta.dot(direction)/length>=Math.cos(Math.toRadians(25)));
	}
	/** Solve vanilla airborne drag/gravity so launch reaches the requested height without teleporting. */
	public static Vec3 launchVelocity(Vec3 direction,double distance) {
		var n=direction.normalize(); double vertical=n.y;
		double y=vertical>0?verticalLaunch(distance*vertical):vertical<0?-1.2*Math.abs(vertical):0.2;
		return new Vec3(n.x*1.35,y,n.z*1.35);
	}
	public static double launchPeak(double velocity) {
		double height=0;for(int tick=0;tick<200&&velocity>0;tick++){height+=velocity;velocity=(velocity-0.08)*0.98;}return height;
	}
	private static double verticalLaunch(double height) {
		double low=0,high=Math.max(2,height);for(int i=0;i<48;i++){double middle=(low+high)/2;if(launchPeak(middle)<height)low=middle;else high=middle;}return (low+high)/2;
	}
	/** Existing overlap may escape on its original side, but cannot cross the barrier plane. */
	public static Vec3 shieldOverlapMotion(Vec3 movement,Vec3 normal,double side) {
		double toward=movement.dot(normal);
		if(Math.abs(side)>1.0E-6&&side*toward>=0)return movement;
		return movement.subtract(normal.scale(toward));
	}

	public static boolean bindPulse(int remaining) { return remaining==1 || remaining<=80&&remaining>0&&remaining%20==0; }
	public static final float TNT_POWER=4F;
	public static boolean underfoot(net.minecraft.core.BlockPos hit,net.minecraft.core.BlockPos casterFeet) {
		return hit.equals(casterFeet.below());
	}

	private PhaseTwoRules() { }
}
