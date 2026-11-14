package com.example.examplemod.magic.spell.wind.wind_bind;
public final class WindBindSpec {
	public static final int MANA=10,RECOVERY=10,PREPARATION_TICKS=160,DURATION=100;
	public static final double RANGE=12;
	public static final float PULSE_DAMAGE=1F;
	public static boolean pulse(int remaining) { return remaining==1||remaining<=80&&remaining>0&&remaining%20==0; }
	private WindBindSpec() { }
}
