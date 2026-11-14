package com.example.examplemod.magic.spell.holy.sacred_nova;
public final class SacredNovaRules {
	public static final double RADIUS=16;
	public static boolean inside(double squaredDistance){return squaredDistance<=256;}
	public static boolean ringDue(long remaining){return remaining%5==0;}
	public static double ringRadius(int lifetime,long remaining){return Math.min(RADIUS,(lifetime+1-remaining)*2.4);}
	private SacredNovaRules() { }
}
