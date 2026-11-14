package com.example.examplemod.magic.learning;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.learning.LearnSpell;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.feature.learning.*;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;

class LearningModuleTest {
	private final LearnSpell useCase = new LearnSpell();
	@Test void zenithLearns102ButLoadsOnlyTenWithoutChangingMana() {
		var catalog = FrozenLearningCatalog.create();
		var state = PlayerMagicState.initial(100, catalog);
		for (var id : catalog.ids()) {
			var result = useCase.execute(state, id, new ZenithBookPolicy(), true, false);
			assertEquals(LearnSpell.Status.LEARNED, result.status());
			state = result.state();
		}
		assertEquals(102, state.learned().size());
		assertEquals(10, state.loaded().size());
		assertEquals(100, state.mana());
		assertEquals("water_ball", state.selected());
		assertEquals(102, useCase.execute(state, "water_ball", new ZenithBookPolicy(), true, false).state().learned().size());
		assertEquals(LearnSpell.Status.NOT_IN_COURSE, useCase.execute(state, "past_transfer", new ZenithBookPolicy(), true, false).status());
	}
	@Test void everyBookRejectsUnverifiedPossessionAndPrimerStays39() {
		var catalog = FrozenLearningCatalog.create();
		var state = PlayerMagicState.initial(100, catalog);
		BookPolicy[] policies = {new ZenithBookPolicy(), new PrimerBookPolicy(FrozenLearningCatalog.primer()), new ClassifiedBookPolicy("water", 1)};
		for (var policy : policies) {
			assertEquals(LearnSpell.Status.INVALID_BOOK, useCase.execute(state, "water_ball", policy, false, true).status());
			for (var id : policy.lessons(catalog)) assertTrue(catalog.contains(id));
		}
		assertEquals(39, policies[1].lessons(catalog).size());
		assertFalse(policies[1].lessons(catalog).contains("blizzard_storm"));
	}
	@Test void classifiedRequiresPrerequisitesAndCompletedChallenge() {
		var state = PlayerMagicState.initial(100, FrozenLearningCatalog.create());
		var policy = new ClassifiedBookPolicy("water", 1);
		assertEquals(LearnSpell.Status.PREREQUISITE, useCase.execute(state, "water_arrow", policy, true, true).status());
		assertEquals(LearnSpell.Status.CHALLENGE_REQUIRED, useCase.execute(state, "water_ball", policy, true, false).status());
		state = useCase.execute(state, "water_ball", policy, true, true).state();
		assertEquals(LearnSpell.Status.LEARNED, useCase.execute(state, "water_arrow", policy, true, true).status());
	}
	@Test void interruptedPartialSectionRestartsButCompletedSectionsRemain() {
		var notes = java.util.stream.IntStream.range(0, 3).mapToObj(i -> new int[8]).toList();
		var score = new SegmentedLesson(5, notes, 0);
		for (int i=0;i<8;i++) score.press(0);
		assertEquals(1, score.completedSections());
		score.press(0); score.press(1);
		assertEquals(1, score.currentIndex());
		score.restartCurrentSection();
		assertEquals(1, score.completedSections());
		assertEquals(0, score.currentIndex());
	}
	@Test void restoreAndLoadoutPreserveKnowledgeAndRecoveryBoundaries() {
		var catalog = FrozenLearningCatalog.create();
		var state = new PlayerMagicState(2,100,23999,5,java.util.List.of("water_ball", "unknown", "water_ball"),java.util.List.of("unknown", "water_ball"),"unknown",catalog);
		assertEquals(java.util.List.of("water_ball"), state.learned());
		assertEquals("water_ball", state.selected());
		assertEquals(100, state.tick().mana());
		assertEquals(4, state.tick().cooldown());
		assertTrue(state.toggle("water_ball").learned().contains("water_ball"));
		assertTrue(state.toggle("water_ball").loaded().isEmpty());
		assertEquals(23999, state.restoreMana(10).onlineTicks());
	}
}
