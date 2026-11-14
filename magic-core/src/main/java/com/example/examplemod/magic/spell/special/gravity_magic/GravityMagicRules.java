package com.example.examplemod.magic.spell.special.gravity_magic;
public final class GravityMagicRules {
	public static final int MODE_COUNT=2,UPKEEP=8;
	public static boolean inside(double horizontalSquared,double y){return !(y<0||y>12||horizontalSquared>36);}
	private GravityMagicRules() { }
}
