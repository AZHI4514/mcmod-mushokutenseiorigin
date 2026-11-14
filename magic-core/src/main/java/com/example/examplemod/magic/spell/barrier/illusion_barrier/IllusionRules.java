package com.example.examplemod.magic.spell.barrier.illusion_barrier;
public final class IllusionRules {
	public static boolean contains(double dx,double dz,double y,double originY){return dx*dx+dz*dz<144&&y>=originY&&y<=originY+12;}
	private IllusionRules() { }
}
