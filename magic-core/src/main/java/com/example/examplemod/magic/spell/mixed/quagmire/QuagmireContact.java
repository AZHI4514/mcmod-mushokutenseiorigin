package com.example.examplemod.magic.spell.mixed.quagmire;
public final class QuagmireContact {
	public interface Target {boolean inside();boolean owner();boolean allowed();boolean visible();boolean immune();void slow(boolean boss);void hold();}
	public static void apply(Target t){if(!t.inside()||!t.owner()&&!t.allowed()||!t.visible())return;boolean boss=t.immune();t.slow(boss);if(!boss)t.hold();}
	private QuagmireContact() { }
}
