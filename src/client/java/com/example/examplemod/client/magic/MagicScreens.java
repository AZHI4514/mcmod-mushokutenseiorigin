package com.example.examplemod.client.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.MagicState;

/** 原版字体、按钮与凹槽面板；没有修改玩家的魔力上限控件。 */
public final class MagicScreens {
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

	public static void drawHud(GuiGraphicsExtractor g) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.options.hideGui || client.player.isSpectator()) return;
		MagicState state = MagicClient.state();
		// 原版一行 10 颗心：9 像素心形以 8 像素间距排列，总宽 81。
		// 九格栏宽 182、高 22；魔力槽在右侧，底边同为屏幕底边。
		int x = g.guiWidth() / 2 + 97;
		int barWidth = 81;
		// 最大 GUI 缩放下右侧可能不足 81 像素，整体等比缩放保留定位。
		float scale = Math.min(1.0F, Math.max(1, g.guiWidth() - x - 2) / (float) barWidth);
		g.pose().pushMatrix();
		g.pose().translate(x, g.guiHeight());
		g.pose().scale(scale, scale);
		g.fill(0, -7, barWidth, 0, 0xFF101A25);
		int filled = (int) (79L * state.mana() / state.maximum());
		g.fill(1, -6, 1 + filled, -1, 0xFF285BC5);
		g.fill(1, -6, 1 + filled, -5, 0xFF78BDFF);
		g.centeredText(client.font, state.mana() + " / " + state.maximum(), barWidth / 2, -18, 0xFF75BFFF);
		g.pose().popMatrix();
		int reserved=client.player.getAttachedOrElse(com.example.examplemod.magic.MagicAttachments.CHARGE_MANA,0);
		if (reserved>0) {
			g.centeredText(client.font,Integer.toString(com.example.examplemod.magic.IntermediateRules.chargeDisplay(reserved)),g.guiWidth()/2,g.guiHeight()/2+17,0xFFFFA090);
		}
		if (state.selected().isEmpty()) return;
		// 使用原版药水 HUD 的背景精灵和 24 像素行高，仅显示名字。
		int hudWidth = Math.max(24, client.font.width(name(state.selected())) + 12);
		int baseTop = client.isDemo() ? 16 : 1;
		int top = baseTop;
		// 原版增益/负面效果分别占用 26 像素的一行，避免盖住真实药水效果。
		for (var effect : client.player.getActiveEffects()) {
			if (effect.showIcon()) top = Math.max(top, baseTop + (effect.getEffect().value().isBeneficial() ? 26 : 52));
		}
		int left = g.guiWidth() - hudWidth - 1;
		g.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
				net.minecraft.resources.Identifier.withDefaultNamespace("hud/effect_background"), left, top, hudWidth, 24);
		g.centeredText(client.font, name(state.selected()), left + hudWidth / 2, top + 8, 0xFFFFFFFF);
	}

	public static final class Knowledge extends Screen {
		private final boolean book;
		private String selected = SpellDefinition.WATER_BALL.id();
		private int page;
		private Button action;

		public Knowledge(boolean book) {
			super(text(book ? "book" : "knowledge"));
			this.book = book;
		}

		@Override
		protected void init() {
			int x = left(), y = top();
			var spells = book ? SpellDefinition.ALL : SpellDefinition.ALL.stream()
					.filter(spell -> MagicClient.state().learned().contains(spell.id())).toList();
			for (int i = page * 5; i < Math.min(spells.size(), page * 5 + 5); i++) {
				String id = spells.get(i).id();
				addRenderableWidget(Button.builder(name(id), button -> selected = id)
						.bounds(x + 10, y + 35 + (i % 5) * 23, 100, 20).build());
			}
			action = addRenderableWidget(Button.builder(text("load"), button -> {
				MagicClient.send(book ? "learn" : "toggle", selected);
			}).bounds(x + 120, y + 147, 146, 20).build());
			addRenderableWidget(Button.builder(Component.literal("<"), b -> { if (page > 0) { page--; rebuildWidgets(); } })
					.bounds(x + 10, y + 158, 24, 20).build());
			addRenderableWidget(Button.builder(Component.literal(">"), b -> { if ((page + 1) * 5 < spells.size()) { page++; rebuildWidgets(); } })
					.bounds(x + 86, y + 158, 24, 20).build());
			addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> onClose())
					.bounds(x + 180, y + 190, 86, 20).build());
		}

		int left() { return (width - 280) / 2; }
		int top() { return (height - 222) / 2; }

		@Override
		public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
			g.fill(0, 0, width, height, 0x80000000);
			int x = left(), y = top();
			panel(g, x, y, 280, 222);
			g.text(font, title, x + 10, y + 12, 0xFF303030, false);
			MagicState state = MagicClient.state();
			g.text(font, text("learned"), x + 10, y + 25, 0xFF404040, false);
			SpellDefinition spell = SpellDefinition.find(selected);
			boolean visible = spell != null && (book || state.learned().contains(selected));
			action.active = visible && (!book || !state.learned().contains(selected));
			action.setMessage(text(book ? (state.learned().contains(selected) ? "already_learned" : "learn")
					: state.loaded().contains(selected) ? "unload" : "load"));
			if (visible) {
				g.text(font, name(selected), x + 120, y + 36, 0xFF164880, false);
				g.text(font, Component.translatable("magic.mushokutenseiorigin.cost", spell.manaCost()), x + 120, y + 56, 0xFF303030, false);
				g.text(font, Component.translatable("magic.mushokutenseiorigin.cooldown", spell.cooldownTicks() / 20.0), x + 120, y + 72, 0xFF303030, false);
				g.text(font, Component.translatable("magic.mushokutenseiorigin.element." + spell.element()), x + 120, y + 88, 0xFF164880, false);
				if (spell.damage() > 0 || spell.healing() > 0) g.text(font, Component.translatable("magic.mushokutenseiorigin." + (spell.healing() > 0 ? "healing" : "damage"), spell.healing() > 0 ? spell.healing() : spell.damage()), x + 120, y + 104, 0xFF303030, false);
				if (spell.execution() == SpellDefinition.Execution.AREA || spell.execution() == SpellDefinition.Execution.HEAT_HAND
						|| spell.execution() == SpellDefinition.Execution.DETOX || spell == SpellDefinition.ICE_BLADE || spell.rank()==2) {
					var lines = font.split(text("summary_" + spell.id()), 146);
					for (int i=0; i<Math.min(3,lines.size()); i++) g.text(font, lines.get(i), x+120, y+115+i*9, 0xFF505050, false);
				}
				if (spell.knockback() > 0) g.text(font, text("effect_knockback"), x + 120, y + 122, 0xFF505050, false);
				else if (spell.execution() == SpellDefinition.Execution.FLAME_SLICE)
					g.text(font, text("effect_flame_slice"), x + 120, y + 122, 0xFF505050, false);
			} else g.text(font, text("empty"), x + 12, y + 60, 0xFF505050, false);
			g.text(font, Component.translatable("magic.mushokutenseiorigin.loaded", state.loaded().size()), x + 10, y + 183, 0xFF303030, false);
			String loaded = String.join(", ", state.loaded().stream().map(id -> name(id).getString()).toList());
			g.text(font, font.plainSubstrByWidth(loaded, 158), x + 10, y + 198, 0xFF164880, false);
			super.extractRenderState(g, mx, my, delta);
		}

		@Override
		public boolean isPauseScreen() { return false; }
	}

	public static final class Wheel extends Screen {
		public Wheel() { super(text("wheel")); }

		/** 圆盘面积约为窗口的九分之一，超宽窗口以短边限制。 */
		private int radius() {
			return Math.max(1, (int) Math.min(Math.sqrt((double) width * height / (9 * Math.PI)), Math.min(width, height) / 2.0 - 4));
		}

		private int hitSlot(double mx, double my) {
			int count = MagicClient.state().loaded().size();
			double dx = mx - width / 2, dy = my - height / 2;
			double distance = dx * dx + dy * dy;
			if (count == 0 || distance < 100 || distance > (double) radius() * radius()) return -1;
			double step = Math.PI * 2 / count;
			double angle = (Math.atan2(dy, dx) + Math.PI / 2 + step / 2 + Math.PI * 2) % (Math.PI * 2);
			return Math.min(count - 1, (int) (angle / step));
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

	private MagicScreens() {
	}
}
