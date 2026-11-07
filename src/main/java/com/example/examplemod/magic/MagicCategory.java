package com.example.examplemod.magic;

/** 教材分类独立于弹体元素：冰归水，解毒与治愈分别教学。 */
public enum MagicCategory {
	FIRE(0xE03A2F), WATER(0x3498DB), WIND(0x3FA45B), EARTH(0xD4A017), HEALING(0xFE6C9F), DETOX(0x2EC4B6), BARRIER(0x8FD3FF);
	private final int color;
	MagicCategory(int color) { this.color = color; }
	public int color() { return color; }
	public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
	public static MagicCategory of(SpellDefinition spell) {
		if (spell.id().equals("detoxification") || spell.id().equals("antidote_ii")) return DETOX;
		return switch (spell.element()) {
			case "fire" -> FIRE; case "water" -> WATER; case "wind" -> WIND; case "earth" -> EARTH; case "healing" -> HEALING; default -> BARRIER;
		};
	}
}
