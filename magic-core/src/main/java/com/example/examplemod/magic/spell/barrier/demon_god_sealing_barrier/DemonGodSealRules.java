package com.example.examplemod.magic.spell.barrier.demon_god_sealing_barrier;
public final class DemonGodSealRules {
	public interface Target {boolean player();float health();float maxHealth();boolean tagged();}
	public static boolean eligible(Target t){return !t.player()&&t.health()<=t.maxHealth()*.2&&t.tagged();}
	private DemonGodSealRules() { }
}
