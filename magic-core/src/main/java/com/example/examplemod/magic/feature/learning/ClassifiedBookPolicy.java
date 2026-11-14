package com.example.examplemod.magic.feature.learning;

import java.util.List;
import com.example.examplemod.magic.catalog.LearningCatalog;

public record ClassifiedBookPolicy(String category, int rank) implements BookPolicy {
	public List<String> lessons(LearningCatalog catalog) { return catalog.course(category, rank); }
	public boolean requiresChallenge() { return true; }
	public boolean requiresPrerequisites() { return true; }
}
