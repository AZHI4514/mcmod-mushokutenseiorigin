package com.example.examplemod.magic.spell.water.ocean_freeze;
public final class OceanFreezeHit {
	public interface Target {boolean alreadyHit();boolean outside();boolean visible();void claim();boolean hurt();boolean canFreeze();void freeze();}
	public static void apply(Target t){if(t.alreadyHit()||t.outside()||!t.visible())return;t.claim();if(t.hurt()&&t.canFreeze())t.freeze();}
	private OceanFreezeHit() { }
}
