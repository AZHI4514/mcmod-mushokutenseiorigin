package com.example.examplemod.magic.feature.learning;

import java.util.List;

/** Completed sections can be persisted; partial sections restart after interruption. */
public final class SegmentedLesson {
	private final List<int[]> sections;
	private int completed;
	private ChantLesson current;
	public SegmentedLesson(int rank, List<int[]> sections, int completed) {
		if (sections.size() != sectionCount(rank) || completed < 0 || completed > sections.size()) throw new IllegalArgumentException();
		this.sections = sections.stream().map(int[]::clone).toList();
		for (int[] notes : this.sections) {
			int minimum = rank >= 6 ? 10 : rank == 5 ? 8 : 2 + rank * 2;
			if (notes.length < minimum || notes.length > minimum + 2) throw new IllegalArgumentException();
			new ChantLesson(notes);
		}
		this.completed = completed;
		restartCurrentSection();
	}
	public static int sectionCount(int rank) {
		if (rank < 1 || rank > 7) throw new IllegalArgumentException();
		return switch (rank) { case 5 -> 3; case 6 -> 4; case 7 -> 7; default -> 1; };
	}
	public void press(int key) {
		if (finished()) return;
		current.press(key);
		if (current.finished()) { completed++; restartCurrentSection(); }
	}
	public void restartCurrentSection() { current = finished() ? null : new ChantLesson(sections.get(completed)); }
	public int completedSections() { return completed; }
	public int currentIndex() { return current == null ? 0 : current.index(); }
	public boolean finished() { return completed == sections.size(); }
}
