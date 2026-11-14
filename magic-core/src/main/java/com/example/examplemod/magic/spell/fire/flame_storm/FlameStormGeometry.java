package com.example.examplemod.magic.spell.fire.flame_storm;
public final class FlameStormGeometry {
	public static boolean wall(double x,double y,double z,double fx,double fy,double fz){
		double lateral=x*(-fz)+y*0+z*fx;double forward=x*fx+y*fy+z*fz;
		return y>=0&&y<=8&&Math.abs(lateral)<=8&&Math.abs(forward)<=2;
	}
	public static double travel(long age){return Math.min(24,age*.1);}
	private FlameStormGeometry() { }
}
