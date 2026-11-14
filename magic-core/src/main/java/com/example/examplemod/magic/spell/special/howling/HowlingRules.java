package com.example.examplemod.magic.spell.special.howling;
public final class HowlingRules {
	public static boolean inside(double length,double dot){return !(length>12||length>.00001&&dot/length<Math.cos(Math.toRadians(50)));}
	public static int controlTicks(boolean player){return player?10:20;}
	private HowlingRules() { }
}
