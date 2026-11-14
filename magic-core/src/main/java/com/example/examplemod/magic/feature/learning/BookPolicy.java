package com.example.examplemod.magic.feature.learning;

import java.util.List;
import com.example.examplemod.magic.catalog.LearningCatalog;

/** Same contract for each book: list permitted IDs, and declare its learning requirements. */
public interface BookPolicy {
	List<String> lessons(LearningCatalog catalog);
	boolean requiresChallenge();
	boolean requiresPrerequisites();
}
