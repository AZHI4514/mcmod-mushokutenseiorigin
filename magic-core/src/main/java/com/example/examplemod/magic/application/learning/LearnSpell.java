package com.example.examplemod.magic.application.learning;

import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.feature.learning.BookPolicy;

/** One learning use case; the game boundary supplies validated possession/challenge evidence. */
public final class LearnSpell {
	public enum Status { LEARNED, ALREADY_KNOWN, INVALID_BOOK, NOT_IN_COURSE, PREREQUISITE, CHALLENGE_REQUIRED }
	public record Result(Status status, PlayerMagicState state) { }
	public Result execute(PlayerMagicState state, String id, BookPolicy policy, boolean held, boolean challengeCompleted) {
		if (!held) return new Result(Status.INVALID_BOOK, state);
		if (!policy.lessons(state.catalog()).contains(id)) return new Result(Status.NOT_IN_COURSE, state);
		if (state.learned().contains(id)) return new Result(Status.ALREADY_KNOWN, state);
		if (policy.requiresPrerequisites() && !state.learned().containsAll(state.catalog().find(id).orElseThrow().prerequisites()))
			return new Result(Status.PREREQUISITE, state);
		if (policy.requiresChallenge() && !challengeCompleted) return new Result(Status.CHALLENGE_REQUIRED, state);
		return new Result(Status.LEARNED, policy.requiresPrerequisites() ? state.learn(id) : state.learnWithoutRequirements(id));
	}
}
