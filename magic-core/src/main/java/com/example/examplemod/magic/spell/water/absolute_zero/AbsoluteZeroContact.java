package com.example.examplemod.magic.spell.water.absolute_zero;
public final class AbsoluteZeroContact {
	public interface Target {boolean inside();boolean owner();boolean allowed();boolean visible();boolean warm();boolean wet();boolean hurt(float damage);void removeWet();void removeWarm();boolean canFreeze();void freeze();void control();void afterHit();boolean randomFreeze();}
	public static void apply(boolean release,Target t){
		if(!t.inside()||!t.owner()&&!t.allowed()||!t.visible())return;
		boolean warm=t.warm();
		if(release){boolean wet=t.wet();if(!t.hurt(AbsoluteZeroRules.damage(wet)))return;if(wet)t.removeWet();if(warm)t.removeWarm();else{if(t.canFreeze())t.freeze();t.control();}t.afterHit();}
		else if(!warm&&t.canFreeze()&&t.randomFreeze())t.freeze();
	}
	private AbsoluteZeroContact() { }
}
