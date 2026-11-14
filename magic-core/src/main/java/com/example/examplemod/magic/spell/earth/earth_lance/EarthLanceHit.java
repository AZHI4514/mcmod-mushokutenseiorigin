package com.example.examplemod.magic.spell.earth.earth_lance;
public final class EarthLanceHit {
	public interface Target { boolean raise(); void damage(); void show(); }
	public static boolean apply(Target t) { if(!t.raise())return false; t.damage();t.show();return true; }
	private EarthLanceHit() { }
}
