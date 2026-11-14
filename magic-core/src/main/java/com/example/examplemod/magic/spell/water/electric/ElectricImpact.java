package com.example.examplemod.magic.spell.water.electric;
public final class ElectricImpact {
	public interface Target {boolean hurt();boolean immune();void slow();void control();void afterHit();void burst();}
	public static void apply(Target t){if(t.hurt()){if(t.immune())t.slow();else t.control();t.afterHit();}t.burst();}
	private ElectricImpact() { }
}
