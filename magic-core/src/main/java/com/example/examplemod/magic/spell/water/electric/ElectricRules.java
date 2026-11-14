package com.example.examplemod.magic.spell.water.electric;
public final class ElectricRules {
	public static boolean nearer(double contact,double nearest){return contact<nearest;}
	public static int controlTicks(boolean player){return player?40:60;}
	private ElectricRules() { }
}
