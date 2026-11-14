package com.example.examplemod.magic.spell.water.ocean_freeze;
public final class OceanFreezeRules {
	public static double radius(long elapsed){return Math.clamp(elapsed*.4,0,64);}
	public static int edge(long elapsed){return Math.min(64,(int)radius(elapsed));}
	public static boolean expired(long elapsed){return elapsed<0||elapsed>=12000;}
	public static boolean scanDue(int scanned,int edge,long elapsed){return edge>scanned||elapsed==0;}
	public static int remainingBudget(int blocks){return 8192-blocks;}
	public static boolean damageActive(long elapsed){return elapsed<=160;}
	public static int restoredScanned(int value){return Math.clamp(value,-1,64);}
	public static int restoredBlocks(int value){return Math.clamp(value,0,8192);}
	private OceanFreezeRules() { }
}
