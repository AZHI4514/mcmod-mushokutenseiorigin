package com.example.examplemod.magic.spell.detox.detoxification;
public final class DetoxificationEffect {
	public interface Patient {boolean removePoison();}
	public static boolean apply(Patient p){return p.removePoison();}
	private DetoxificationEffect() { }
}
