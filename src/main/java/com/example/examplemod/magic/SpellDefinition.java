package com.example.examplemod.magic;

/** 法术数值的单一来源；冷却和寿命均以服务端 tick 表示。 */
public record SpellDefinition(String id, int manaCost, float damage, int cooldownTicks,
		int lifetimeTicks, String element) {
	public static final SpellDefinition WATER_BALL = new SpellDefinition("water_ball", 1, 1.0F, 2, 100, "water");
	public static final java.util.List<SpellDefinition> ALL = java.util.List.of(WATER_BALL);

	public static SpellDefinition find(String id) {
		return ALL.stream().filter(spell -> spell.id().equals(id)).findFirst().orElse(null);
	}
}
