package com.example.examplemod.magic.spell.wind.atmosphere_dominion;
public final class AtmosphereContact {
	public interface Target {boolean owner();double height();double horizontalSquared();boolean allowed();boolean visible();boolean claim();void hurt();boolean immune();void push();}
	public static void apply(Target t,int mode,boolean pulse){
		if(t.owner()||!AtmosphereDominionRules.inside(t.horizontalSquared(),t.height())||!t.allowed()||!t.visible())return;
		if(mode==2&&pulse&&t.claim())t.hurt();
		if(mode==1&&pulse&&!t.immune())t.push();
	}
	private AtmosphereContact() { }
}
