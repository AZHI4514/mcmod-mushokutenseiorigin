package com.example.examplemod.magic.spell.special.howling;
public final class HowlingHit {
	public interface Target {boolean inside();boolean visible();boolean hurt();void control();}
	public static void apply(Target t){if(t.inside()&&t.visible()&&t.hurt())t.control();}
	private HowlingHit() { }
}
