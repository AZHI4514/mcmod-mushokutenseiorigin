package com.example.examplemod.magic.mechanic.field;
public final class SmokeContact {
	public interface Target {boolean inside();boolean exposed();boolean visible();void sight();void slowing();void damage();}
	public static void apply(Target t){if(!t.inside()||!t.exposed()||!t.visible())return;t.sight();t.slowing();t.damage();}
	private SmokeContact() { }
}
