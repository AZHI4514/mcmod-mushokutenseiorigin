package com.example.examplemod.magic.spell.special.gravity_magic;
public final class GravityContact {
	public interface Target {double height();double horizontalSquared();boolean allowed();boolean visible();void heavyEffects();void hurt();void slowFalling();boolean immune();void lift();}
	public static void apply(Target t,int mode,boolean pulse){
		if(!GravityMagicRules.inside(t.horizontalSquared(),t.height())||!t.allowed()||!t.visible())return;
		if(mode==0){t.heavyEffects();if(pulse)t.hurt();}else{t.slowFalling();if(!t.immune())t.lift();}
	}
	private GravityContact() { }
}
