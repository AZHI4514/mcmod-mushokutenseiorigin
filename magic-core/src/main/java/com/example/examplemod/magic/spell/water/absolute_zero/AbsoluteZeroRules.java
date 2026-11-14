package com.example.examplemod.magic.spell.water.absolute_zero;
public final class AbsoluteZeroRules {
	public static boolean contains(double x,double y,double z){return x*x+y*y+z*z<=256;}
	public static float damage(boolean wet){return 28+(wet?6:0);}
	public static int controlTicks(boolean player){return player?40:60;}
	private AbsoluteZeroRules() { }
}
