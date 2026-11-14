package com.example.examplemod.magic.mechanic.targeting;
public final class HolyGeometry {
	public static boolean column(double x,double y,double z,double ax,double ay,double az,double length){double along=x*ax+y*ay+z*az;double dx=x-ax*along,dy=y-ay*along,dz=z-az*along;return along>=0&&along<=length&&dx*dx+dy*dy+dz*dz<=4;}
	private HolyGeometry() { }
}
