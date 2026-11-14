package com.example.examplemod.client.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.MagicState;

import static com.example.examplemod.client.magic.MagicScreenStyle.*;

public final class MagicKnowledgeScreen extends Screen {
	private final boolean book;
	private String selected = SpellDefinition.WATER_BALL.id();
	private int page;
	private Button action;

	public MagicKnowledgeScreen(boolean book) {
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
			g.text(font, Component.translatable("magic.mushokutenseiorigin.cast_timing", spell.castingProfile().preparationTicks() / 20.0, spell.cooldownTicks() / 20.0), x + 120, y + 72, 0xFF303030, false);
			g.text(font, Component.translatable("magic.mushokutenseiorigin.element." + spell.element()), x + 120, y + 88, 0xFF164880, false);
			if (spell.damage() > 0 || spell.healing() > 0) g.text(font, Component.translatable("magic.mushokutenseiorigin." + (spell.healing() > 0 ? "healing" : "damage"), spell.healing() > 0 ? spell.healing() : spell.damage()), x + 120, y + 104, 0xFF303030, false);
			if (spell.execution() == SpellDefinition.Execution.AREA || spell.execution() == SpellDefinition.Execution.HEAT_HAND
					|| spell.execution() == SpellDefinition.Execution.DETOX || spell == SpellDefinition.ICE_BLADE || spell.rank()>=2) {
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
