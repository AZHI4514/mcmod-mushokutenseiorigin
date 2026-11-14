package com.example.examplemod.magic.mechanic.field;
public final class WindFieldPulse {
	public interface Target {boolean claim();boolean hurt();void fireEffects();void afterHit();void slow();}
	public static void apply(boolean flame,boolean vacuum,Target t){if(t.claim()&&t.hurt()){if(flame)t.fireEffects();t.afterHit();}if(vacuum)t.slow();}
	private WindFieldPulse() { }
}
