package com.example.examplemod.magic;

import com.example.examplemod.magic.feature.learning.ChantLesson;

/** Network/client compatibility facade; pure lesson logic lives in magic-core. */
public final class ChantScore {
	private final ChantLesson lesson;
	public ChantScore(int[] notes) { lesson = new ChantLesson(notes); }
	public static int noteCount(int rank, int seed) { return ChantLesson.noteCount(rank, seed); }
	public void press(int key) { lesson.press(key); }
	public int index() { return lesson.index(); }
	public int hits() { return lesson.hits(); }
	public boolean finished() { return lesson.finished(); }
	public boolean passed() { return lesson.passed(); }
}
