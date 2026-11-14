package com.example.examplemod.client.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.MagicState;

/** Shared vanilla panel and localization helpers. */
final class MagicScreenStyle {
	static Component text(String key) {
		return Component.translatable("magic.mushokutenseiorigin." + key);
	}

	static Component name(String id) {
		return Component.translatable("spell.mushokutenseiorigin." + id);
	}

	static void panel(GuiGraphicsExtractor g, int x, int y, int w, int h) {
		g.fill(x, y, x + w, y + h, 0xFF555555);
		g.fill(x, y, x + w - 2, y + h - 2, 0xFFFFFFFF);
		g.fill(x + 3, y + 3, x + w - 3, y + h - 3, 0xFFC6C6C6);
	}

	private MagicScreenStyle() {}
}
