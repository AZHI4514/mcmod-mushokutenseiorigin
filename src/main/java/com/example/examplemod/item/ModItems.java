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
