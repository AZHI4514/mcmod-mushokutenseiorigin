package com.example.examplemod.magic;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import com.example.examplemod.item.ModItems;

/** 两本书共用唯一教材目录和学习白名单；未来新书必须显式分配课程。 */
public final class MagicBooks {
	public static final List<String> LESSONS = List.of("water_ball", "fireball", "wind_ball", "rock_bullet",
			"water_arrow", "ice_smash", "flame_slice", "healing", "water_shield", "ice_blade", "heat_hand", "heat_island",
			"burning_place", "detoxification", "magic_shield", "water_cannon", "water_splash", "icicle_field", "ex_flame", "sonic_boom", "stone_cannon", "ice_pillar", "waterfall", "ice_lance", "icicle_break", "icicle_edge", "flood_flush", "ice_fortress", "explosion", "flame_pillar", "flamethrower", "wind_slice", "wind_bind", "wind_blast", "earth_lance", "earth_pillar", "ex_healing", "antidote_ii", "physical_shield");
	public static final List<String> INTRO_PAGES = List.of("nature", "elements", "ranks", "methods", "practice", "safety", "status_rules", "maintenance");

	public static boolean isTextbook(ItemStack stack) {
		return stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem || isLegacy(stack);
	}
	public static boolean isLegacy(ItemStack stack) {
		return stack.is(ModItems.ZENITH_GREYRAT_MAGIC_BOOK) || stack.is(ModItems.BASIC_ATTACK_MAGIC_PRIMER);
	}
	public static List<String> lessons(MagicCategory category, int rank) {
		return SpellDefinition.ALL.stream().filter(s -> s.rank() == rank && MagicCategory.of(s) == category).map(SpellDefinition::id).toList();
	}
	public static List<String> lessons(ItemStack stack) {
		return stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem book ? lessons(book.category(), book.rank()) : LESSONS;
	}

	public static boolean teaches(ItemStack stack, String spell) {
		return (stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem || isLegacy(stack)) && lessons(stack).contains(spell);
	}

	private MagicBooks() {
	}
}
