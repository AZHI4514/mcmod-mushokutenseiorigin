package com.example.examplemod.magic.domain.spec.catalog;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.SpellDefinition.*;
import static com.example.examplemod.magic.SpellDefinition.builder;
import java.util.List;
/** Named construction presets; effective parameter validation remains in the immutable product. */
public final class DefinitionPresets {
	public static SpellDefinition sweptProjectile(String id,int mana,float damage,int cooldown,String element,float speed,double range,double knockback,Execution execution,String... prerequisites) {
		return builder(id)
			.manaCost(mana)
			.damage(damage)
			.cooldownTicks(cooldown)
			.lifetimeTicks(100)
			.element(element)
			.speed(speed)
			.gravity(0)
			.knockback(knockback)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(execution)
			.healing(0)
			.range(range)
			.maxTargets(Integer.MAX_VALUE)
			.learningRequirements(List.of(prerequisites))
			.build();
	}
	public static SpellDefinition utility(String id, int mana, int cooldown, int duration, String element, Execution execution, String... prerequisites) {
		return builder(id)
			.manaCost(mana)
			.damage(0)
			.cooldownTicks(cooldown)
			.lifetimeTicks(duration)
			.element(element)
			.speed(0)
			.gravity(0)
			.knockback(0)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.execution(execution)
			.healing(0)
			.range(0)
			.maxTargets(1)
			.learningRequirements(List.of(prerequisites))
			.build();
	}
	public static SpellDefinition projectile(String id, int mana, float damage, int cooldown, String element, double gravity, double knockback) {
		return builder(id)
			.manaCost(mana)
			.damage(damage)
			.cooldownTicks(cooldown)
			.lifetimeTicks(100)
			.element(element)
			.speed(1.5F)
			.gravity(gravity)
			.knockback(knockback)
			.castMode(CastMode.INSTANT)
			.requirements(List.of())
			.build();
	}
	private DefinitionPresets(){}
}
