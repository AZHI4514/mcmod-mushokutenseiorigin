package com.example.examplemod.magic.mechanic.field;
/** Lazy sky exposure checks precede extinguishing and wet status application. */
public final class RainExposure {
	public interface Target {boolean inside();boolean loaded();boolean skyVisible();void extinguish();void wet();}
	public static boolean apply(Target target){if(!target.inside()||!target.loaded()||!target.skyVisible())return false;target.extinguish();target.wet();return true;}
	private RainExposure() { }
}
