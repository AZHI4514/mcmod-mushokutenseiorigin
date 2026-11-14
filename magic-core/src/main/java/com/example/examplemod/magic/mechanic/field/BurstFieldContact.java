package com.example.examplemod.magic.mechanic.field;
public final class BurstFieldContact {
	public interface Target {boolean inside();boolean owner();boolean allowed();boolean visible();void flash();boolean claim();void residual();}
	public static void apply(boolean flash,boolean pulse,Target t){if(!t.inside()||!t.owner()&&!t.allowed()||!t.visible())return;if(flash)t.flash();if(pulse&&t.claim())t.residual();}
	private BurstFieldContact() { }
}
