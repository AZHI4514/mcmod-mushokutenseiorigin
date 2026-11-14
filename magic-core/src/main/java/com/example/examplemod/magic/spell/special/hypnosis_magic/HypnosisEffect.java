package com.example.examplemod.magic.spell.special.hypnosis_magic;
public final class HypnosisEffect {
	public interface Target {boolean addEffect();void setImmunity();void markActive();void visual();}
	public static boolean apply(Target t){if(!t.addEffect())return false;t.setImmunity();t.markActive();t.visual();return true;}
	private HypnosisEffect() { }
}
