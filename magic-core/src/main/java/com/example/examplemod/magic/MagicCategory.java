package com.example.examplemod.magic;

/** 教材分类独立于弹体元素：冰归水，解毒与治愈分别教学。 */
public enum MagicCategory {
	FIRE(0xE03A2F), WATER(0x3498DB), WIND(0x3FA45B), EARTH(0xD4A017), HEALING(0xFE6C9F), DETOX(0x2EC4B6), BARRIER(0x8FD3FF), MIXED(0x8A7869), HOLY(0xFFF2A7), SPECIAL(0xB78AED), SUMMON(0x65DDB9);
	private final int color;
	MagicCategory(int color) { this.color = color; }
	public int color() { return color; }
	public String id() { return name().toLowerCase(java.util.Locale.ROOT); }
	public static MagicCategory of(SpellDefinition spell){return valueOf(com.example.examplemod.magic.domain.spec.SpellTraits.category(spell.id(),spell.element()));}
}
