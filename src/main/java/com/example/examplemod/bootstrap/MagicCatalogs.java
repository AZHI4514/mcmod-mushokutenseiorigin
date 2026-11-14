package com.example.examplemod.bootstrap;

import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.MagicCategory;
import com.example.examplemod.magic.catalog.LearningCatalog;

/** Explicit immutable legacy-definition bridge during catalog migration. No world state. */
public final class MagicCatalogs {
	public static final LearningCatalog LEARNING = new LearningCatalog(SpellDefinition.ALL.stream()
			.map(s -> new LearningCatalog.Lesson(s.id(), s.learningRequirements(), MagicCategory.of(s).id(), s.rank())).toList());
	private MagicCatalogs() { }
}
