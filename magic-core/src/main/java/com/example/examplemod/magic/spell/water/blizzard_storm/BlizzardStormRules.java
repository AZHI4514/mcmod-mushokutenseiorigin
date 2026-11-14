package com.example.examplemod.magic.spell.water.blizzard_storm;
public final class BlizzardStormRules {
	public static boolean pulse(int age){return age>=0&&age<80&&age%10==0;}
	public static long expiry(long time,int age){return time+Math.max(0,80-age);}
	public static int restore(int saved,long expiry,long time){int age=Math.clamp(saved,0,80);long left=expiry-time;return Math.max(age,80-(int)Math.clamp(left,0,80));}
	public static boolean within(double squaredDistance){return !(squaredDistance>64);}
	private BlizzardStormRules() { }
}
