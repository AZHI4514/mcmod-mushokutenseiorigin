package com.example.examplemod.magic.spell.detox.antidote_ii;
public final class AntidoteIIEffect {
	public interface Patient {boolean removePoison();boolean removeNausea();boolean removeHunger();}
	public static boolean apply(Patient p){return p.removePoison()|p.removeNausea()|p.removeHunger();}
	private AntidoteIIEffect() { }
}
