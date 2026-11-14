package com.example.examplemod.magic.mechanic.field;

/** Pure scheduling boundaries; no clock or persisted target state is hidden here. */
public final class FieldSchedule {
	public static boolean strikeReady(long now, long next, long expiry) { return now < expiry && now >= next; }
	public static boolean pulse(int age, int duration, int interval) {
		return age >= 0 && age < duration && interval > 0 && age % interval == 0;
	}
	public static boolean cadenceReady(long now, long previous, int interval) {
		return interval > 0 && now >= previous + interval;
	}
	private FieldSchedule() { }
}
