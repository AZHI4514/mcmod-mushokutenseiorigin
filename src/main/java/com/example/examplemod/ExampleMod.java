package com.example.examplemod;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.examplemod.item.ModItems;

public class ExampleMod implements ModInitializer {
	public static final String MOD_ID = "mushokutenseiorigin";

	// 使用模组 ID 作为 logger 名称，便于区分日志来源
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * 构造一个属于本模组的 {@link Identifier}。
	 *
	 * @param path 资源路径，例如 {@code "example_item"}
	 */
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		// 此方法在 Minecraft 进入可加载模组状态时执行。
		// 资源等内容可能尚未初始化，请谨慎操作。

		LOGGER.info("[{}] Initializing common entrypoint...", MOD_ID);

		// 触发 ModItems 的静态初始化，完成物品注册
		ModItems.initialize();

		LOGGER.info("[{}] Common initialization complete.", MOD_ID);
	}
}
