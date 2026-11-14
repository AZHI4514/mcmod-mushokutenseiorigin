package com.example.examplemod.magic.spell.fire.inferno;
public final class InfernoRules {
	public static float residual(long age){return age>=20&&age<=400&&age%20==0?5:0;}
	public static boolean contains(double x,double y,double z){return y>=0&&y<=8&&x*x+z*z<=400;}
	private InfernoRules() { }
}
