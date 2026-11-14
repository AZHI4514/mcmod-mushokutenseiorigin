package com.example.examplemod.client.magic;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.magic.MagicAttachments;
import com.example.examplemod.magic.MagicState;

/** Registers only magic key bindings; input policy is separate. */
public final class MagicKeyBindings {
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ExampleMod.id("magic"));
	public static final KeyMapping KNOWLEDGE = key("knowledge", GLFW.GLFW_KEY_G);
	public static final KeyMapping WHEEL = key("wheel", GLFW.GLFW_KEY_R);
	public static final KeyMapping CAST = key("cast", GLFW.GLFW_KEY_V);
	public static final KeyMapping[] CHANT_KEYS = {key("chant_1", GLFW.GLFW_KEY_UP), key("chant_2", GLFW.GLFW_KEY_DOWN), key("chant_3", GLFW.GLFW_KEY_LEFT), key("chant_4", GLFW.GLFW_KEY_RIGHT)};
	private static KeyMapping key(String name, int code) {
		return KeyMappingHelper.registerKeyMapping(new KeyMapping("key.mushokutenseiorigin." + name,
				InputConstants.Type.KEYSYM, code, CATEGORY));
	}

	private MagicKeyBindings(){}
}
