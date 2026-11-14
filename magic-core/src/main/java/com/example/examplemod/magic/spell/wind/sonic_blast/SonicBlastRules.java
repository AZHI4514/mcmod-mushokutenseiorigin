package com.example.examplemod.magic.spell.wind.sonic_blast;

/** Cone geometry only; inputs retain vanilla IEEE-754 semantics. */
public final class SonicBlastRules {
	public static final double RANGE=16;
	private static final double COS_HALF_ANGLE=Math.cos(Math.toRadians(35));
	public static boolean contains(double x,double y,double z,double dx,double dy,double dz) {
		double length=Math.sqrt(x*x+y*y+z*z);
		if (!(length<=RANGE)) return false;
		if (length<1.0E-6) return true;
		// Same threshold and component division as vanilla Vec3.normalize().
		double directionLength=Math.sqrt(dx*dx+dy*dy+dz*dz);
		if (directionLength<1.0E-5) {dx=0;dy=0;dz=0;}
		else {dx/=directionLength;dy/=directionLength;dz/=directionLength;}
		return (x*dx+y*dy+z*dz)/length>=COS_HALF_ANGLE;
	}
	private SonicBlastRules() { }
}
