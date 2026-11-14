package com.example.examplemod.magic.spell.fire.flash_over;
public final class FlashOverRules {
	public static float residual(long age){return age>=0&&age<160&&age%20==0?3:0;}
	public static boolean contains(double x,double y,double z){return y>=0&&y<=6&&x*x+z*z<=256;}
	private FlashOverRules() { }
}
