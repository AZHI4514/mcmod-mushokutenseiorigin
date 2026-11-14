package com.example.examplemod.magic.spell.fire.heat_island;
public final class HeatIslandRules {
	public static boolean inside(double dx,double dz){return dx*dx+dz*dz<=9;}
	public interface Target {void removeWet();void warm();}
	public static void apply(Target target){target.removeWet();target.warm();}
	private HeatIslandRules() { }
}
