package com.example.examplemod.magic.spell.water.cumulonimbus;
public final class CumulonimbusRules {
	public static boolean inside(double dx,double dz){return !(dx*dx+dz*dz>576);}
	public static boolean refresh(int age,long now,long next){return age%10==0||now>=next;}
	public static boolean strike(long now,long next,long expiry){return now>=next&&now<expiry;}
	public static long shorten(long now,long expiry,int ticks){return Math.max(now,expiry-ticks);}
	private CumulonimbusRules() { }
}
