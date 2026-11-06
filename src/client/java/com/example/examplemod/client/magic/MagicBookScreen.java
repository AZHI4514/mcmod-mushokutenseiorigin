package com.example.examplemod.client.magic;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.MagicBooks;
import com.example.examplemod.magic.SpellDefinition;

/** 两本书使用同一教材；原版翻页、页码与纸张，法术页提供对应学习按钮。 */
public final class MagicBookScreen extends BookViewScreen {
	private Button learn;
	private final List<String> pageSpells = new ArrayList<>();
	private int shownPage;

	public MagicBookScreen() {
		super(new BookAccess(pages()));
		for (int i = 0; i < MagicBooks.INTRO_PAGES.size(); i++) pageSpells.add("");
		pageSpells.addAll(MagicBooks.LESSONS);
	}

	private static List<Component> pages() {
		List<Component> pages = new ArrayList<>();
		for (String id : MagicBooks.INTRO_PAGES) pages.add(Component.translatable("magic.mushokutenseiorigin.textbook." + id));
		for (String id : MagicBooks.LESSONS) pages.add(Component.translatable("magic.mushokutenseiorigin.textbook." + id));
		return pages;
	}

	@Override
	protected void init() {
		super.init();
		learn = addRenderableWidget(Button.builder(MagicScreens.text("learn"), button -> {
			String id = pageSpells.get(shownPage);
			if (!id.isEmpty()) MagicClient.send("learn", id);
		// Leave the vanilla page arrows unobstructed, even before learning.
		}).bounds((width - 192) / 2 + 36, 174, 114, 20).build());
	}

	@Override
	protected void pageBack() { super.pageBack(); shownPage = Math.max(0, shownPage - 1); }
	@Override
	protected void pageForward() { super.pageForward(); shownPage = Math.min(pageSpells.size() - 1, shownPage + 1); }
	@Override
	public boolean setPage(int page) {
		boolean changed = super.setPage(page);
		if (changed) shownPage = Math.clamp(page, 0, MagicBooks.INTRO_PAGES.size() + MagicBooks.LESSONS.size() - 1);
		return changed;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		String id = pageSpells.get(shownPage);
		boolean learned = MagicClient.state().learned().contains(id);
		learn.visible = !id.isEmpty();
		var spell = SpellDefinition.find(id);
		boolean allowed = spell != null && spell.learningRequirementsMet(MagicClient.state().learned());
		learn.active = !learned && allowed;
		learn.setMessage(learned ? MagicScreens.text("already_learned") : !allowed ? MagicScreens.text("missing_knowledge") : Component.translatable(
				"magic.mushokutenseiorigin.learn_spell", MagicScreens.name(id)));
		super.extractRenderState(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean isPauseScreen() { return false; }
}
