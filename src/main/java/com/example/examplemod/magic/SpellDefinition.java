package com.example.examplemod.magic;

import java.util.List;

/** 服务端参数的单一来源。未接入的施法方式与前置不能被当作可用技能。 */
public record SpellDefinition(String id, int manaCost, float damage, int cooldownTicks,
		int lifetimeTicks, String element, float speed, double gravity, double knockback,
		CastMode castMode, List<String> requirements, Execution execution, float healing,
		double range, int maxTargets, List<String> learningRequirements) {
	public enum Execution { PROJECTILE, FLAME_SLICE, SELF_HEAL }
	public enum CastMode { INSTANT, CHANT, CHARGE, CHANNEL }
	public static final SpellDefinition WATER_BALL = projectile("water_ball", 1, 1, 2, "water", 0.03, 0);
	public static final SpellDefinition FIREBALL = projectile("fireball", 3, 3, 12, "fire", 0.03, 0);
	public static final SpellDefinition WIND_BALL = projectile("wind_ball", 2, 1, 8, "wind", 0.01, 0.6);
	public static final SpellDefinition ROCK_BULLET = projectile("rock_bullet", 3, 4, 12, "earth", 0.06, 0);
	public static final SpellDefinition WATER_ARROW = new SpellDefinition("water_arrow", 2, 2, 6, 40, "water",
			2.5F, 0.01, 0, CastMode.INSTANT, List.of(), Execution.PROJECTILE, 0, 0, 1, List.of("water_ball"));
	public static final SpellDefinition ICE_SMASH = new SpellDefinition("ice_smash", 4, 5, 16, 60, "water",
			1.2F, 0.05, 0, CastMode.INSTANT, List.of(), Execution.PROJECTILE, 0, 0, 1, List.of("water_ball"));
	public static final SpellDefinition FLAME_SLICE = new SpellDefinition("flame_slice", 4, 4, 14, 20, "fire",
			0.75F, 0, 0, CastMode.INSTANT, List.of(), Execution.FLAME_SLICE, 0, 10, Integer.MAX_VALUE, List.of("fireball"));
	public static final SpellDefinition HEALING = new SpellDefinition("healing", 8, 0, 60, 1, "healing",
			0, 0, 0, CastMode.INSTANT, List.of(), Execution.SELF_HEAL, 4, 0, 1, List.of());
	public static final List<SpellDefinition> ALL = List.of(WATER_BALL, FIREBALL, WIND_BALL, ROCK_BULLET,
			WATER_ARROW, ICE_SMASH, FLAME_SLICE, HEALING);

	/** 原有基础弹体定义保持相同参数与 ID。 */
	public SpellDefinition(String id, int manaCost, float damage, int cooldownTicks, int lifetimeTicks,
			String element, float speed, double gravity, double knockback, CastMode mode, List<String> requirements) {
		this(id, manaCost, damage, cooldownTicks, lifetimeTicks, element, speed, gravity, knockback,
				mode, requirements, Execution.PROJECTILE, 0, 0, 1, List.of());
	}

	public SpellDefinition {
		requirements = List.copyOf(requirements);
		learningRequirements = List.copyOf(learningRequirements);
		if (manaCost < 0 || damage < 0 || healing < 0 || range < 0 || maxTargets < 1 || cooldownTicks < 1 || lifetimeTicks < 1)
			throw new IllegalArgumentException("Invalid spell parameters: " + id);
	}

	private static SpellDefinition projectile(String id, int mana, float damage, int cooldown, String element, double gravity, double knockback) {
		return new SpellDefinition(id, mana, damage, cooldown, 100, element, 1.5F, gravity, knockback, CastMode.INSTANT, List.of());
	}

	/** 疾病、成长等未来条件接入点。未知依赖默认拒绝，不能空实现后直接放行。 */
	public boolean prerequisitesMet(java.util.function.Predicate<String> available) {
		return requirements.stream().allMatch(available);
	}

	public boolean learningRequirementsMet(List<String> learned) {
		return learned.containsAll(learningRequirements);
	}

	/** 与原版 heal 配合；满血、死亡或无效生命数值不产生治疗。 */
	public float healingAmount(float health, float maximum) {
		if (execution != Execution.SELF_HEAL || !Float.isFinite(health) || !Float.isFinite(maximum) || health <= 0)
			return 0;
		return Math.min(healing, Math.max(0, maximum - health));
	}

	public static SpellDefinition find(String id) {
		return ALL.stream().filter(spell -> spell.id().equals(id)).findFirst().orElse(null);
	}
}
