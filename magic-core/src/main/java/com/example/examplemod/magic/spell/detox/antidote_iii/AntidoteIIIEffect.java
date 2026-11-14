package com.example.examplemod.magic.spell.detox.antidote_iii;
import com.example.examplemod.magic.spell.detox.antidote_ii.AntidoteIIEffect;
public final class AntidoteIIIEffect {
	public interface Patient extends AntidoteIIEffect.Patient {boolean removeWeakness();void visual();}
	public static boolean apply(Patient p){boolean changed=AntidoteIIEffect.apply(p)|p.removeWeakness();if(!changed)return false;p.visual();return true;}
	private AntidoteIIIEffect() { }
}
