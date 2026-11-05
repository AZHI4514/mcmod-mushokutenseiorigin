package com.example.examplemod.item;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import com.example.examplemod.ExampleMod;

/**
 * 示例物品注册。
 *
 * <p>用于验证 Fabric 注册流程是否正常工作：
 * 启动客户端后可在「原材料 / Ingredients」创造模式分页中找到该物品。
 */
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

		// 把示例物品加入「原材料」创造模式分页
		// 注意：26.1 起 ItemGroupEvents 已重命名为 CreativeModeTabEvents
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
				.register(entries -> entries.accept(EXAMPLE_ITEM));

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
				.register(entries -> entries.accept(ROXY_PANTSU));

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
				.register(entries -> entries.accept(ZENITH_GREYRAT_MAGIC_BOOK));
	}

	// 私有构造，防止实例化
	private ModItems() {
	}
}
