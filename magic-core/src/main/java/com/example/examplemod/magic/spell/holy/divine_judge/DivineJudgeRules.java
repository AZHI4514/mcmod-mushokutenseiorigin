package com.example.examplemod.magic.spell.holy.divine_judge;
public final class DivineJudgeRules {
	public static int pulseIndex(long elapsed){return elapsed<0||elapsed>=60?-1:(int)(elapsed/20);}
	public static boolean inside(double x,double y,double z){return y>=0&&y<=32&&x*x+z*z<=144;}
	public static int restoredCount(int count){return Math.clamp(count,0,3);}
	private DivineJudgeRules() { }
}
