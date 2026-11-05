package com.example.examplemod.client;

import net.fabricmc.api.ClientModInitializer;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.client.render.ModArmorRenderers;

/**
 * 客户端入口点。
 *
 * <p>只有渲染、按键绑定、GUI 等仅客户端逻辑才应放在这里。
 * 由 {@code fabric.mod.json} 的 {@code client} 入口点声明。
 */
public class ExampleModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ExampleMod.LOGGER.info("[{}] Initializing client entrypoint...", ExampleMod.MOD_ID);

		// 在这里注册客户端专属内容，例如：
		//   - 按键绑定
		//   - 物品/方块着色
		//   - 渲染层（26.1 起大多数情况会自动推导，无需手动注册）

		ModArmorRenderers.initialize();
		com.example.examplemod.client.magic.MagicClient.initialize();

		ExampleMod.LOGGER.info("[{}] Client initialization complete.", ExampleMod.MOD_ID);
	}
}
