package com.example.examplemod.magic;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import com.example.examplemod.item.ModItems;

/** 两本书共用唯一教材目录和学习白名单；未来新书必须显式分配课程。 */
public final class MagicBooks {
	public static final List<String> LESSONS = List.of("water_ball", "fireball", "wind_ball", "rock_bullet");
	public static final List<String> INTRO_PAGES = List.of("nature", "elements", "ranks", "methods", "practice", "safety");

	public static boolean isTextbook(ItemStack stack) {
		return stack.is(ModItems.ZENITH_GREYRAT_MAGIC_BOOK) || stack.is(ModItems.BASIC_ATTACK_MAGIC_PRIMER);
	}

	public static boolean teaches(ItemStack stack, String spell) {
		return isTextbook(stack) && LESSONS.contains(spell) && SpellDefinition.find(spell) != null;
	}

	private MagicBooks() {
	}
}
