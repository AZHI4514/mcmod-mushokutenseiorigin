package com.example.examplemod.magic.spell.fire.magma_gush;
public final class MagmaGushRules {
	public static float damage(long age){return age==0?10:age>=20&&age<=160&&age%20==0?2:0;}
	public static boolean contains(double x,double y,double z){return x*x+z*z<=16&&y>=0&&y<=5;}
	private MagmaGushRules() { }
}
