package com.example.examplemod.client.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.MagicState;

import static com.example.examplemod.client.magic.MagicScreenStyle.*;

public final class MagicWheelScreen extends Screen {
	public MagicWheelScreen() { super(text("wheel")); }

	/** 圆盘面积约为窗口的九分之一，超宽窗口以短边限制。 */
	private int radius() {
		return com.example.examplemod.magic.feature.input.WheelGeometry.radius(width,height);
	}
	private int hitSlot(double mx,double my) {
		return com.example.examplemod.magic.feature.input.WheelGeometry.hitSlot(width,height,MagicClient.state().loaded().size(),mx,my);
	}

	@Override
	public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0 || event.button() == 1) {
			int slot = hitSlot(event.x(), event.y());
			if (slot >= 0) {
				MagicClient.send("select", MagicClient.state().loaded().get(slot));
			}
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public void tick() {
		var client = Minecraft.getInstance();
		if (!MagicClient.physicallyDown(MagicClient.WHEEL) || !client.isWindowActive()
				|| client.player == null || !client.player.isAlive()) onClose();
	}

	@Override
	public void removed() {
		var client = Minecraft.getInstance();
		for (var key : MagicClient.movementKeys(client))
			key.setDown(client.isWindowActive() && MagicClient.physicallyDown(key));
		super.removed();
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mx, int my, float delta) {
		// 只绘制圆盘，不添加原版全屏背景或模糊。
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
		int cx = width / 2, cy = height / 2, r = radius();
		var slots = MagicClient.state().loaded();
		int hover = hitSlot(mx, my);
		// 像素扫描线构成真正的圆形，扇区命中与高亮共用同一角度计算。
		for (int y = -r; y <= r; y++) {
			int half = (int) Math.sqrt((double) r * r - (double) y * y);
			g.fill(cx - half, cy + y, cx + half + 1, cy + y + 1, 0x80505050);
			g.fill(cx - half, cy + y, cx - half + 1, cy + y + 1, 0xFFAAAAAA);
			g.fill(cx + half, cy + y, cx + half + 1, cy + y + 1, 0xFFAAAAAA);
			if (hover >= 0) {
				int start = -1;
				for (int x = -half; x <= half + 1; x++) {
					boolean selected = x <= half && hitSlot(cx + x, cy + y) == hover;
					if (selected && start == -1) start = x + r;
					if (!selected && start != -1) {
						g.fill(cx + start - r, cy + y, cx + x, cy + y + 1, 0x806C78A0);
						start = -1;
					}
				}
			}
		}
		for (int i = 0; i < slots.size(); i++) {
			double angle = -Math.PI / 2 + i * Math.PI * 2 / slots.size();
			int x = cx + (int) (Math.cos(angle) * r * 0.62);
			int y = cy + (int) (Math.sin(angle) * r * 0.62);
			int available = Math.max(12, (int) (r * 0.85));
			float scale = Math.min(1, available / (float) Math.max(1, font.width(name(slots.get(i)))));
			g.pose().pushMatrix();
			g.pose().translate(x, y);
			g.pose().scale(scale, scale);
			g.centeredText(font, name(slots.get(i)), 0, -4, i == hover ? 0xFFFFFF80 : 0xFFFFFFFF);
			g.pose().popMatrix();
		}
		if (slots.isEmpty()) g.centeredText(font, text("empty"), cx, cy - 4, 0xFFFFFFFF);
	}

	@Override
	public boolean isPauseScreen() { return false; }
}
