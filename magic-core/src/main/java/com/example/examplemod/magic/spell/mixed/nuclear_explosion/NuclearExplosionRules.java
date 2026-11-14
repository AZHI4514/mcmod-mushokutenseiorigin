package com.example.examplemod.magic.spell.mixed.nuclear_explosion;
public final class NuclearExplosionRules {
	public static final int MODE_COUNT=4;
	public static int extra(int raw){return Math.clamp(raw,0,3000);}
	public static int cost(int raw){return 3000+extra(raw);}
	public static double radius(int extra){return 24+extra*8.0/3000;}
	public static float damage(int extra){return 60+extra*20F/3000;}
	public static boolean detonationDue(long age,boolean exploded){return age>=100&&!exploded;}
	private NuclearExplosionRules() { }
}
