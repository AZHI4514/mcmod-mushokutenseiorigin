package com.example.examplemod.magic;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import com.example.examplemod.item.ModItems;

/** 塞妮丝书收录全部已注册法术；入门书和分类教材各自保留课程白名单。 */
public final class MagicBooks {
	public static final List<String> LESSONS = List.of("water_ball", "fireball", "wind_ball", "rock_bullet",
			"water_arrow", "ice_smash", "flame_slice", "healing", "water_shield", "ice_blade", "heat_hand", "heat_island",
			"burning_place", "detoxification", "magic_shield", "water_cannon", "water_splash", "icicle_field", "ex_flame", "sonic_boom", "stone_cannon", "ice_pillar", "waterfall", "ice_lance", "icicle_break", "icicle_edge", "flood_flush", "ice_fortress", "explosion", "flame_pillar", "flamethrower", "wind_slice", "wind_bind", "wind_blast", "earth_lance", "earth_pillar", "ex_healing", "antidote_ii", "physical_shield");
	public static final List<String> ZENITH_LESSONS = SpellDefinition.ALL.stream().map(SpellDefinition::id).toList();
	public static final List<String> INTRO_PAGES = List.of("nature", "elements", "ranks", "methods", "practice", "safety", "status_rules", "maintenance");

	public static boolean isTextbook(ItemStack stack) {
		return stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem || isLegacy(stack);
	}
	public static boolean isLegacy(ItemStack stack) {
		return stack.is(ModItems.ZENITH_GREYRAT_MAGIC_BOOK) || stack.is(ModItems.BASIC_ATTACK_MAGIC_PRIMER);
	}
	public static List<String> lessons(MagicCategory category, int rank) {
		return new com.example.examplemod.magic.feature.learning.ClassifiedBookPolicy(category.id(), rank).lessons(com.example.examplemod.bootstrap.MagicCatalogs.LEARNING);
	}
	public static List<String> lessons(ItemStack stack) {
		if (stack.is(ModItems.ZENITH_GREYRAT_MAGIC_BOOK)) return new com.example.examplemod.magic.feature.learning.ZenithBookPolicy().lessons(com.example.examplemod.bootstrap.MagicCatalogs.LEARNING);
		return stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem book ? lessons(book.category(), book.rank()) : new com.example.examplemod.magic.feature.learning.PrimerBookPolicy(LESSONS).lessons(com.example.examplemod.bootstrap.MagicCatalogs.LEARNING);
	}

	public static com.example.examplemod.magic.feature.learning.BookPolicy policy(ItemStack stack) {
		if (stack.is(ModItems.ZENITH_GREYRAT_MAGIC_BOOK)) return new com.example.examplemod.magic.feature.learning.ZenithBookPolicy();
		if (stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem book)
			return new com.example.examplemod.magic.feature.learning.ClassifiedBookPolicy(book.category().id(), book.rank());
		return new com.example.examplemod.magic.feature.learning.PrimerBookPolicy(LESSONS);
	}

	public static boolean teaches(ItemStack stack, String spell) {
		return (stack.getItem() instanceof com.example.examplemod.item.MagicTextbookItem || isLegacy(stack)) && lessons(stack).contains(spell);
	}

	private MagicBooks() {
	}
}
