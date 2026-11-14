package com.example.examplemod.magic.spell.special.hypnosis_magic;
public final class HypnosisRules {
	public interface Target {boolean immune();boolean allowed();boolean hypnotized();boolean ready();}
	public static boolean eligible(Target t){return !t.immune()&&t.allowed()&&!t.hypnotized()&&t.ready();}
	public static int duration(boolean player){return player?40:160;}
	private HypnosisRules() { }
}
