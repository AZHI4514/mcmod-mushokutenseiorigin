package com.example.examplemod.magic.spell.holy.god_purge;
public final class GodPurgeRules {
	public static final double RADIUS=48;
	public static boolean inside(double squaredDistance){return squaredDistance<=2304;}
	public static double ringRadius(int lifetime,long remaining){return Math.min(RADIUS,(lifetime+1-remaining)*2.4);}
	private GodPurgeRules() { }
}
