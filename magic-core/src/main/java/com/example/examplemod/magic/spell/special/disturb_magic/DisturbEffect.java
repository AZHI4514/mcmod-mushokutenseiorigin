package com.example.examplemod.magic.spell.special.disturb_magic;
public final class DisturbEffect {
	public interface Target {void cancel();void preventCasting();void visual();}
	public static boolean apply(Target target){if(target!=null){target.cancel();target.preventCasting();target.visual();}return true;}
	private DisturbEffect() { }
}
