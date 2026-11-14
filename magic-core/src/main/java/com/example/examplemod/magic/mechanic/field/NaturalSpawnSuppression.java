package com.example.examplemod.magic.mechanic.field;
public final class NaturalSpawnSuppression {
	public interface Field {boolean removed();boolean sameLevel();boolean unexpired();boolean inside();}
	public static boolean suppresses(Field f){return !f.removed()&&f.sameLevel()&&f.unexpired()&&f.inside();}
	private NaturalSpawnSuppression() { }
}
