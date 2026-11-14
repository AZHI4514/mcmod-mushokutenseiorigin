package com.example.examplemod.magic.domain.spec.catalog;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.SpellDefinition.*;
import static com.example.examplemod.magic.SpellDefinition.builder;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import static com.example.examplemod.magic.domain.spec.catalog.DefinitionPresets.*;
/** Owns authored summon spell values without world or registration state. */
public final class SummonSpellDefinitions {
	private static final Map<String,Supplier<SpellDefinition>> DEFINITIONS=Map.ofEntries(
		Map.entry("monster_summon", () -> builder("monster_summon")
			.manaCost(50)
			.damage(4)
			.cooldownTicks(20)
			.lifetimeTicks(2400)
			.element("summon")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SUMMON)
			.healing(0)
			.range(8)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("earth_fortress"))
			.timingRank(3)
			.officiallyGraded(false)
			.explicitProfile(null)
			.build()),
		Map.entry("spirit_summon", () -> builder("spirit_summon")
			.manaCost(120)
			.damage(4)
			.cooldownTicks(60)
			.lifetimeTicks(2400)
			.element("summon")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SUMMON)
			.healing(0)
			.range(16)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("monster_summon", "magic_shield"))
			.timingRank(4)
			.officiallyGraded(false)
			.explicitProfile(null)
			.build()),
		Map.entry("lamplight_spirits", () -> builder("lamplight_spirits")
			.manaCost(8)
			.damage(0)
			.cooldownTicks(10)
			.lifetimeTicks(6000)
			.element("summon")
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(Execution.SUMMON)
			.healing(0)
			.range(3)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of("heat_hand"))
			.timingRank(2)
			.officiallyGraded(false)
			.explicitProfile(null)
			.build()));
	public static SpellDefinition create(String id){var definition=DEFINITIONS.get(id);if(definition==null)throw new IllegalArgumentException(id);return definition.get();}
	private SummonSpellDefinitions(){}
}
