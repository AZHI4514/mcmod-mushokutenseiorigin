package com.example.examplemod.magic.domain.spec.catalog;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.SpellDefinition.*;
import static com.example.examplemod.magic.SpellDefinition.builder;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static com.example.examplemod.magic.domain.spec.catalog.DefinitionPresets.*;
/** Owns authored mixed spell values without world or registration state. */
public final class MixedSpellDefinitions {
	private static final Map<String,Supplier<SpellDefinition>> DEFINITIONS=Map.ofEntries(
		Map.entry("deep_mist", () -> builder("deep_mist")
			.manaCost(26)
			.damage(0)
			.cooldownTicks(20)
			.lifetimeTicks(600)
			.element("mixed")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SMOKE_FIELD)
			.healing(0)
			.range(32)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("waterfall", "heat_island", "icicle_field"))
			.timingRank(3)
			.officiallyGraded(true)
			.explicitProfile(null)
			.build()),
		Map.entry("mud_rain", () -> builder("mud_rain")
			.manaCost(42)
			.damage(0)
			.cooldownTicks(20)
			.lifetimeTicks(500)
			.element("mixed")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SMOKE_FIELD)
			.healing(0)
			.range(48)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("squall", "quagmire"))
			.timingRank(3)
			.officiallyGraded(true)
			.explicitProfile(null)
			.build()),
		Map.entry("quagmire", () -> builder("quagmire")
			.manaCost(28)
			.damage(0)
			.cooldownTicks(20)
			.lifetimeTicks(400)
			.element("mixed")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.QUAGMIRE)
			.healing(0)
			.range(32)
			.maxTargets(1)
			.learningRequirements(List.of("water_cannon","earth_lance"))
			.timingRank(3)
			.officiallyGraded(true)
			.explicitProfile(null)
			.build()));
	public static SpellDefinition create(String id){var definition=DEFINITIONS.get(id);if(definition==null)throw new IllegalArgumentException(id);return definition.get();}
	private MixedSpellDefinitions(){}
}
