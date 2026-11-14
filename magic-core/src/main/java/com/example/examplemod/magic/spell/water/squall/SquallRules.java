package com.example.examplemod.magic.spell.water.squall;
public final class SquallRules {
	public static int duration(boolean evaporates){return evaporates?450:900;}
	public static long shorten(long now,long expiry,int ticks){return Math.max(now,expiry-ticks);}
	public static boolean inside(double dx,double dz){return !(dx*dx+dz*dz>256);}
	public static boolean pulse(int age){return age%10==0;}
	private SquallRules() { }
}
