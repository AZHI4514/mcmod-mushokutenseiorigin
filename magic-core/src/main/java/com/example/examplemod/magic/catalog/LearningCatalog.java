package com.example.examplemod.magic.catalog;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;

/** Immutable knowledge catalog, supplied explicitly rather than read from game statics. */
public final class LearningCatalog {
	public record Lesson(String id, List<String> prerequisites, String category, int rank) {
		public Lesson { prerequisites = List.copyOf(prerequisites); }
	}
	private final Map<String, Lesson> lessons;
	public LearningCatalog(Collection<Lesson> input) {
		var entries = new LinkedHashMap<String, Lesson>();
		for (var lesson : input) {
			new com.example.examplemod.magic.domain.model.SpellId(lesson.id());
			if (entries.putIfAbsent(lesson.id(), lesson) != null) throw new IllegalArgumentException("Duplicate lesson: " + lesson.id());
		}
		lessons = java.util.Collections.unmodifiableMap(entries);
	}
	public boolean contains(String id) { return lessons.containsKey(id); }
	public Optional<Lesson> find(String id) { return Optional.ofNullable(lessons.get(id)); }
	public List<String> ids() { return List.copyOf(lessons.keySet()); }
	public List<String> course(String category, int rank) {
		return lessons.values().stream().filter(l -> l.category().equals(category) && l.rank() == rank).map(Lesson::id).toList();
	}
}
