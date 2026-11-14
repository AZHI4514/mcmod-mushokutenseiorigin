package com.example.examplemod.item;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import com.example.examplemod.ExampleMod;

/** 模组物品统一注册，仅在「无职转生」创造分类展示正式物品。 */
public class ModItems {
	/**
	 * 示例物品。
	 *
	 * <p>使用 {@code stacksTo(16)} 只是为了演示属性配置，可自行调整。
	 */
	public static final Item MANA_CRYSTAL = register(
			"mana_crystal",
			ManaCrystalItem::new,
			ManaCrystalItem.createProperties()
	);
	public static final Item CHARGED_MANA_CRYSTAL = register(
			"charged_mana_crystal",
			Item::new,
			ManaCrystalItem.createProperties()
	);
	public static final Item KING_FIRE_MAGIC_TEXTBOOK = register(
			"king_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_FIRE_MAGIC_TEXTBOOK = register(
			"divine_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_EARTH_MAGIC_TEXTBOOK = register(
			"king_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_EARTH_MAGIC_TEXTBOOK = register(
			"emperor_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_EARTH_MAGIC_TEXTBOOK = register(
			"divine_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_EARTH_MAGIC_TEXTBOOK = register(
			"saint_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_DETOX_MAGIC_TEXTBOOK = register(
			"advanced_detox_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.DETOX, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item INTERMEDIATE_SPECIAL_MAGIC_TEXTBOOK = register(
			"intermediate_special_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SPECIAL, 2),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_SPECIAL_MAGIC_TEXTBOOK = register(
			"advanced_special_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SPECIAL, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_WIND_MAGIC_TEXTBOOK = register(
			"divine_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_SPECIAL_MAGIC_TEXTBOOK = register(
			"emperor_special_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SPECIAL, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_MIXED_MAGIC_TEXTBOOK = register(
			"divine_mixed_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.MIXED, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_BARRIER_MAGIC_TEXTBOOK = register(
			"advanced_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_BARRIER_MAGIC_TEXTBOOK = register(
			"saint_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_BARRIER_MAGIC_TEXTBOOK = register(
			"king_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_SPECIAL_MAGIC_TEXTBOOK = register(
			"king_special_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SPECIAL, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_SUMMON_MAGIC_TEXTBOOK = register(
			"advanced_summon_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SUMMON, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_SUMMON_MAGIC_TEXTBOOK = register(
			"saint_summon_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SUMMON, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item INTERMEDIATE_SUMMON_MAGIC_TEXTBOOK = register(
			"intermediate_summon_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.SUMMON, 2),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_BARRIER_MAGIC_TEXTBOOK = register(
			"emperor_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_BARRIER_MAGIC_TEXTBOOK = register(
			"divine_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item EXAMPLE_ITEM = register(
			"example_item",
			Item::new,
			new Item.Properties().stacksTo(16)
	);

	/** 洛琪希的圣遗物：属性与行为由所属物品类负责。 */
	public static final Item ROXY_PANTSU = register(
			"roxy_pantsu",
			RoxyPantsuItem::new,
			RoxyPantsuItem.createProperties()
	);

	/** 塞妮丝的魔法书，学习咏唱的入口。 */
	public static final Item ZENITH_GREYRAT_MAGIC_BOOK = register(
			"zenith_greyrat_magic_book",
			ZenithGreyratMagicBookItem::new,
			ZenithGreyratMagicBookItem.createProperties()
	);

	/** 与塞妮丝的魔法书共用四元素初级教材。 */
	public static final Item BASIC_ATTACK_MAGIC_PRIMER = register(
			"basic_attack_magic_primer",
			BasicAttackMagicPrimerItem::new,
			BasicAttackMagicPrimerItem.createProperties()
	);

	public static final Item INTERMEDIATE_MIXED_MAGIC_TEXTBOOK = register(
			"intermediate_mixed_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.MIXED, 2),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_MIXED_MAGIC_TEXTBOOK = register(
			"king_mixed_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.MIXED, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_MIXED_MAGIC_TEXTBOOK = register(
			"advanced_mixed_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.MIXED, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_EARTH_MAGIC_TEXTBOOK = register(
			"advanced_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_WIND_MAGIC_TEXTBOOK = register(
			"saint_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_WIND_MAGIC_TEXTBOOK = register(
			"king_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_WIND_MAGIC_TEXTBOOK = register(
			"emperor_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_WIND_MAGIC_TEXTBOOK = register(
			"advanced_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_FIRE_MAGIC_TEXTBOOK = register(
			"emperor_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_FIRE_MAGIC_TEXTBOOK = register(
			"saint_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_FIRE_MAGIC_TEXTBOOK = register(
			"advanced_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item BEGINNER_HOLY_MAGIC_TEXTBOOK = register(
			"beginner_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 1),
			MagicTextbookItem.createProperties()
	);
	public static final Item INTERMEDIATE_HOLY_MAGIC_TEXTBOOK = register(
			"intermediate_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 2),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_HOLY_MAGIC_TEXTBOOK = register(
			"advanced_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_HOLY_MAGIC_TEXTBOOK = register(
			"saint_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_HOLY_MAGIC_TEXTBOOK = register(
			"king_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_HOLY_MAGIC_TEXTBOOK = register(
			"divine_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_HOLY_MAGIC_TEXTBOOK = register(
			"emperor_holy_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HOLY, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item DIVINE_WATER_MAGIC_TEXTBOOK = register(
			"divine_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 7),
			MagicTextbookItem.createProperties()
	);
	public static final Item EMPEROR_WATER_MAGIC_TEXTBOOK = register(
			"emperor_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 6),
			MagicTextbookItem.createProperties()
	);
	public static final Item KING_WATER_MAGIC_TEXTBOOK = register(
			"king_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 5),
			MagicTextbookItem.createProperties()
	);
	public static final Item SAINT_WATER_MAGIC_TEXTBOOK = register(
			"saint_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 4),
			MagicTextbookItem.createProperties()
	);
	public static final Item ADVANCED_WATER_MAGIC_TEXTBOOK = register(
			"advanced_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 3),
			MagicTextbookItem.createProperties()
	);
	public static final Item BEGINNER_FIRE_MAGIC_TEXTBOOK = register(
			"beginner_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item BEGINNER_WATER_MAGIC_TEXTBOOK = register(
			"beginner_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item BEGINNER_WIND_MAGIC_TEXTBOOK = register(
			"beginner_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item BEGINNER_EARTH_MAGIC_TEXTBOOK = register(
			"beginner_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item BEGINNER_HEALING_MAGIC_TEXTBOOK = register(
			"beginner_healing_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HEALING, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item BEGINNER_DETOX_MAGIC_TEXTBOOK = register(
			"beginner_detox_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.DETOX, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item BEGINNER_BARRIER_MAGIC_TEXTBOOK = register(
			"beginner_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 1),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_FIRE_MAGIC_TEXTBOOK = register(
			"intermediate_fire_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.FIRE, 2),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_WATER_MAGIC_TEXTBOOK = register(
			"intermediate_water_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WATER, 2),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_WIND_MAGIC_TEXTBOOK = register(
			"intermediate_wind_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.WIND, 2),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_EARTH_MAGIC_TEXTBOOK = register(
			"intermediate_earth_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.EARTH, 2),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_HEALING_MAGIC_TEXTBOOK = register(
			"intermediate_healing_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.HEALING, 2),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_DETOX_MAGIC_TEXTBOOK = register(
			"intermediate_detox_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.DETOX, 2),
			MagicTextbookItem.createProperties()
	);

	public static final Item INTERMEDIATE_BARRIER_MAGIC_TEXTBOOK = register(
			"intermediate_barrier_magic_textbook",
			properties -> new MagicTextbookItem(properties, com.example.examplemod.magic.MagicCategory.BARRIER, 2),
			MagicTextbookItem.createProperties()
	);

	/** 本模组统一创造模式分类；新增物品同时加入这里的展示列表。 */
	public static final CreativeModeTab MUSHOKUTENSEIORIGIN_TAB = Registry.register(
			BuiltInRegistries.CREATIVE_MODE_TAB,
			ExampleMod.id("mushokutenseiorigin"),
			FabricCreativeModeTab.builder()
					.title(Component.translatable("itemGroup.mushokutenseiorigin"))
					.icon(() -> new ItemStack(ZENITH_GREYRAT_MAGIC_BOOK))
					.displayItems((parameters, output) -> {
						output.accept(ROXY_PANTSU);
						output.accept(ZENITH_GREYRAT_MAGIC_BOOK);
						output.accept(BASIC_ATTACK_MAGIC_PRIMER);
						output.accept(ADVANCED_WATER_MAGIC_TEXTBOOK);
						output.accept(SAINT_WATER_MAGIC_TEXTBOOK);
						output.accept(KING_WATER_MAGIC_TEXTBOOK);
						output.accept(MANA_CRYSTAL);
						output.accept(CHARGED_MANA_CRYSTAL);
						output.accept(KING_FIRE_MAGIC_TEXTBOOK);
						output.accept(DIVINE_FIRE_MAGIC_TEXTBOOK);
						output.accept(KING_EARTH_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_EARTH_MAGIC_TEXTBOOK);
						output.accept(DIVINE_EARTH_MAGIC_TEXTBOOK);
						output.accept(SAINT_EARTH_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_DETOX_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_SPECIAL_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_SPECIAL_MAGIC_TEXTBOOK);
						output.accept(DIVINE_WIND_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_SPECIAL_MAGIC_TEXTBOOK);
						output.accept(DIVINE_MIXED_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_BARRIER_MAGIC_TEXTBOOK);
						output.accept(SAINT_BARRIER_MAGIC_TEXTBOOK);
						output.accept(KING_BARRIER_MAGIC_TEXTBOOK);
						output.accept(KING_SPECIAL_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_SUMMON_MAGIC_TEXTBOOK);
						output.accept(SAINT_SUMMON_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_SUMMON_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_BARRIER_MAGIC_TEXTBOOK);
						output.accept(DIVINE_BARRIER_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_HOLY_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_HOLY_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_HOLY_MAGIC_TEXTBOOK);
						output.accept(SAINT_HOLY_MAGIC_TEXTBOOK);
						output.accept(KING_HOLY_MAGIC_TEXTBOOK);
						output.accept(DIVINE_HOLY_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_HOLY_MAGIC_TEXTBOOK);
						output.accept(DIVINE_WATER_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_WATER_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_FIRE_MAGIC_TEXTBOOK);
						output.accept(SAINT_FIRE_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_FIRE_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_WIND_MAGIC_TEXTBOOK);
						output.accept(EMPEROR_WIND_MAGIC_TEXTBOOK);
						output.accept(KING_WIND_MAGIC_TEXTBOOK);
						output.accept(SAINT_WIND_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_EARTH_MAGIC_TEXTBOOK);
						output.accept(ADVANCED_MIXED_MAGIC_TEXTBOOK);
						output.accept(KING_MIXED_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_MIXED_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_FIRE_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_WATER_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_WIND_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_EARTH_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_HEALING_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_DETOX_MAGIC_TEXTBOOK);
						output.accept(BEGINNER_BARRIER_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_FIRE_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_WATER_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_WIND_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_EARTH_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_HEALING_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_DETOX_MAGIC_TEXTBOOK);
						output.accept(INTERMEDIATE_BARRIER_MAGIC_TEXTBOOK);
					})
					.build()
	);

	/**
	 * 通用注册方法。
	 *
	 * @param name        物品的注册名（不含命名空间）
	 * @param itemFactory 物品工厂，例如 {@code Item::new}
	 * @param properties  物品属性
	 * @param <T>         物品类型
	 * @return 已注册的物品实例
	 */
	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
		// 1. 创建资源键
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ExampleMod.id(name));

		// 2. 创建物品实例（26.1 起必须在 properties 上设置 id）
		T item = itemFactory.apply(properties.setId(itemKey));

		// 3. 注册到游戏注册表
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

	/**
	 * 强制触发本类的静态初始化，使上面声明的物品完成注册。
	 */
	public static void initialize() {
		ExampleMod.LOGGER.info("Registering items for {}", ExampleMod.MOD_ID);

	}

	// 私有构造，防止实例化
	private ModItems() {
	}
}
