package com.example.examplemod.magic.spell.mixed.frost_nova;
public final class FrostNovaRules {
	public static final double RADIUS=8;
	public static final int FREEZE_TICKS=300;
	public static boolean inside(double distanceSquared){return !(distanceSquared>RADIUS*RADIUS);}
	public static float damage(boolean wet){return 10+(wet?4:0);}
	public static int controlTicks(boolean player){return player?20:40;}
	private FrostNovaRules() { }
}
