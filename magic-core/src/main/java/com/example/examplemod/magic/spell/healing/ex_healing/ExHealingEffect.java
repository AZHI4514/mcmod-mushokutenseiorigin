package com.example.examplemod.magic.spell.healing.ex_healing;
/** Reads health at application time; zero healing is still a successful compatibility call. */
public final class ExHealingEffect {
	public interface Patient { float health(); float maximum(); void heal(float amount); }
	public static boolean apply(Patient patient) {
		patient.heal(ExHealingRules.amount(patient.health(),patient.maximum()));return true;
	}
	private ExHealingEffect() { }
}
