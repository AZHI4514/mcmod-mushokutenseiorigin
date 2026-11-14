package com.example.examplemod.magic.spell.water.lightning;
public final class LightningImpact {
	public interface Target {boolean inside();boolean skyVisible();boolean owner();boolean allowed();boolean visible();boolean hurt();void control();void afterHit();}
	public static void apply(Target t){if(!t.inside()||!t.skyVisible()||!t.owner()&&!t.allowed()||!t.visible())return;if(t.hurt()){t.control();t.afterHit();}}
	private LightningImpact() { }
}
