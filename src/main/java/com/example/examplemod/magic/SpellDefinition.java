package com.example.examplemod.magic;

import java.util.List;

/** 服务端参数的单一来源。未接入的施法方式与前置不能被当作可用技能。 */
public record SpellDefinition(String id, int manaCost, float damage, int cooldownTicks,
		int lifetimeTicks, String element, float speed, double gravity, double knockback,
		CastMode castMode, List<String> requirements) {
	public enum CastMode { INSTANT, CHANT, CHARGE, CHANNEL }
	public static final SpellDefinition WATER_BALL = projectile("water_ball", 1, 1, 2, "water", 0.03, 0);
	public static final SpellDefinition FIREBALL = projectile("fireball", 3, 3, 12, "fire", 0.03, 0);
	public static final SpellDefinition WIND_BALL = projectile("wind_ball", 2, 1, 8, "wind", 0.01, 0.6);
	public static final SpellDefinition ROCK_BULLET = projectile("rock_bullet", 3, 4, 12, "earth", 0.06, 0);
	public static final List<SpellDefinition> ALL = List.of(WATER_BALL, FIREBALL, WIND_BALL, ROCK_BULLET);

	public SpellDefinition {
		requirements = List.copyOf(requirements);
		if (manaCost < 0 || damage < 0 || cooldownTicks < 1 || lifetimeTicks < 1)
			throw new IllegalArgumentException("Invalid spell parameters: " + id);
	}

	private static SpellDefinition projectile(String id, int mana, float damage, int cooldown, String element, double gravity, double knockback) {
		return new SpellDefinition(id, mana, damage, cooldown, 100, element, 1.5F, gravity, knockback, CastMode.INSTANT, List.of());
	}

	/** 疾病、成长等未来条件接入点。未知依赖默认拒绝，不能空实现后直接放行。 */
	public boolean prerequisitesMet(java.util.function.Predicate<String> available) {
		return requirements.stream().allMatch(available);
	}

	public static SpellDefinition find(String id) {
		return ALL.stream().filter(spell -> spell.id().equals(id)).findFirst().orElse(null);
	}
}
