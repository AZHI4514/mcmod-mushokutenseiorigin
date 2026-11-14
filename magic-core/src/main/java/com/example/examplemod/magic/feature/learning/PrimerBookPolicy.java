package com.example.examplemod.magic.feature.learning;

import java.util.List;
import com.example.examplemod.magic.catalog.LearningCatalog;

public record PrimerBookPolicy(List<String> whitelist) implements BookPolicy {
	public PrimerBookPolicy { whitelist = List.copyOf(whitelist); }
	public List<String> lessons(LearningCatalog catalog) { return whitelist.stream().filter(catalog::contains).toList(); }
	public boolean requiresChallenge() { return false; }
	public boolean requiresPrerequisites() { return false; }
}
