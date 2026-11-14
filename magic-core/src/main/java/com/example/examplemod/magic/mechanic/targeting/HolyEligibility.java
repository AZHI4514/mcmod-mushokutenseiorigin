package com.example.examplemod.magic.mechanic.targeting;
public final class HolyEligibility {
	public interface Target {boolean player();boolean alive();boolean owner();boolean tagged();}
	public static boolean eligible(Target t){return !t.player()&&t.alive()&&!t.owner()&&t.tagged();}
	private HolyEligibility() { }
}
