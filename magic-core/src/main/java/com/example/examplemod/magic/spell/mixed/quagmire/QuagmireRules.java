package com.example.examplemod.magic.spell.mixed.quagmire;
public final class QuagmireRules {
	public static boolean contains(double x,double y,double z){return x*x+z*z<=36&&y>=-.2&&y<=.65;}
	public static double jumpVelocity(double upward){return upward>0?Math.min(upward,.115):upward;}
	private QuagmireRules() { }
}
