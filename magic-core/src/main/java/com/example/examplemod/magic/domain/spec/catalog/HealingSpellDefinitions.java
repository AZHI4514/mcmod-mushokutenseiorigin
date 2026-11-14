package com.example.examplemod.magic.domain.spec.catalog;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.SpellDefinition.*;
import static com.example.examplemod.magic.SpellDefinition.builder;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static com.example.examplemod.magic.domain.spec.catalog.DefinitionPresets.*;
/** Owns authored healing spell values without world or registration state. */
public final class HealingSpellDefinitions {
	private static final Map<String,Supplier<SpellDefinition>> DEFINITIONS=Map.ofEntries(
		Map.entry("healing", () -> builder("healing")
			.manaCost(8)
			.damage(0)
			.cooldownTicks(60)
			.lifetimeTicks(1)
			.element("healing")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SELF_HEAL)
			.healing(4)
			.range(0)
			.maxTargets(1)
			.learningRequirements(List.of())
			.build()),
		Map.entry("detoxification", () -> utility("detoxification", 6, 60, 1, "healing", Execution.DETOX)),
		Map.entry("ex_healing", () -> builder("ex_healing")
			.manaCost(16)
			.damage(0)
			.cooldownTicks(100)
			.lifetimeTicks(1)
			.element("healing")
			.speed(1.5F)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.PHASE_TWO)
			.healing(8)
			.range(0)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("healing"))
			.build()),
		Map.entry("antidote_ii", () -> builder("antidote_ii")
			.manaCost(12)
			.damage(0)
			.cooldownTicks(100)
			.lifetimeTicks(1)
			.element("healing")
			.speed(1.5F)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.PHASE_TWO)
			.healing(0)
			.range(0)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("detoxification"))
			.build()),
		Map.entry("antidote_iii", () -> builder("antidote_iii")
			.manaCost(28)
			.damage(0)
			.cooldownTicks(20)
			.lifetimeTicks(1)
			.element("healing")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SUPPORT)
			.healing(0)
			.range(12)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("antidote_ii"))
			.timingRank(3)
			.officiallyGraded(true)
			.explicitProfile(null)
			.build()));
	public static SpellDefinition create(String id){var definition=DEFINITIONS.get(id);if(definition==null)throw new IllegalArgumentException(id);return definition.get();}
	private HealingSpellDefinitions(){}
}
