package com.example.examplemod.magic.spell.fire.burning_place;
public final class BurningPlaceContact {
	public interface Target {boolean alreadyContacted();boolean touches();boolean allowed();boolean visible();void claim();boolean hurt();void effects();void afterHit();}
	public static void apply(Target target){
		if(target.alreadyContacted()||!target.touches()||!target.allowed()||!target.visible())return;
		target.claim();if(!target.hurt())return;target.effects();target.afterHit();
	}
	private BurningPlaceContact() { }
}
