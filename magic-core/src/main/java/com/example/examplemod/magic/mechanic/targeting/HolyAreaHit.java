package com.example.examplemod.magic.mechanic.targeting;
public final class HolyAreaHit {
	public interface Target {boolean inside();boolean visible();void hurt();}
	public static void apply(Target t){if(t.inside()&&t.visible())t.hurt();}
	private HolyAreaHit() { }
}
