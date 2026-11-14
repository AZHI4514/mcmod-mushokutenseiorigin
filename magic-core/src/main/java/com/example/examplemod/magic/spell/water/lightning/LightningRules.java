package com.example.examplemod.magic.spell.water.lightning;
public final class LightningRules {
	public static boolean lockNow(int remaining){return remaining==15;}
	public static boolean warn(int remaining){return remaining%3==0;}
	public static int steps(double length){return Math.max(1,(int)Math.ceil(length));}
	public static boolean inRange(double squaredDistance){return squaredDistance<=4096.01;}
	public static boolean inside(double dx,double dz){return !(dx*dx+dz*dz>4);}
	private LightningRules() { }
}
