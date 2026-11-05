package com.example.examplemod.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ModItems;

/** 自定义装备渲染的唯一注册入口，禁止从通用源集引用本类。 */
public final class ModArmorRenderers {

	public static void initialize() {
		ArmorRenderer.register(context -> new BlockbenchArmorRenderer(context,
				ExampleMod.id("models/equipment/roxy_pantsu.json")), ModItems.ROXY_PANTSU);
	}

	private ModArmorRenderers() {
	}
}
