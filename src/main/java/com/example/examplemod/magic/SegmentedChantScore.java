package com.example.examplemod.magic;

import java.util.List;
import com.example.examplemod.magic.feature.learning.SegmentedLesson;

/** Compatibility facade, removed when all lesson callers migrate. */
public final class SegmentedChantScore {
	private final SegmentedLesson lesson;
	public SegmentedChantScore(int rank, List<int[]> sections, int completed) { lesson = new SegmentedLesson(rank, sections, completed); }
	public static int sectionCount(int rank) { return SegmentedLesson.sectionCount(rank); }
	public void press(int key) { lesson.press(key); }
	public void restartCurrentSection() { lesson.restartCurrentSection(); }
	public int completedSections() { return lesson.completedSections(); }
	public int currentIndex() { return lesson.currentIndex(); }
	public boolean finished() { return lesson.finished(); }
}
