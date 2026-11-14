package com.example.examplemod.magic.spell.fire.magma_gush;
public final class MagmaGushHit {
	public interface Target {boolean inside();boolean owner();boolean allowed();boolean visible();boolean claim();boolean hurt();void selfEffects();void otherEffects();void afterHit();}
	public static void apply(Target t){if(!t.inside()||!t.owner()&&!t.allowed()||!t.visible()||!t.claim()||!t.hurt())return;if(t.owner())t.selfEffects();else t.otherEffects();t.afterHit();}
	private MagmaGushHit() { }
}
