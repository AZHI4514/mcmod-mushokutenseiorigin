package com.example.examplemod.magic.feature.learning;

import java.util.List;
import com.example.examplemod.magic.catalog.LearningCatalog;

public final class ZenithBookPolicy implements BookPolicy {
	public List<String> lessons(LearningCatalog catalog) { return catalog.ids(); }
	public boolean requiresChallenge() { return false; }
	public boolean requiresPrerequisites() { return false; }
}
