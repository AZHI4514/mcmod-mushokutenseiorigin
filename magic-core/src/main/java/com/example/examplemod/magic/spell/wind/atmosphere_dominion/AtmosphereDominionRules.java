package com.example.examplemod.magic.spell.wind.atmosphere_dominion;
public final class AtmosphereDominionRules {
	public static final int MODE_COUNT=3,UPKEEP=30;
	public static boolean inside(double horizontalSquared,double y){return !(y<0||y>64||horizontalSquared>2304);}
	private AtmosphereDominionRules() { }
}
