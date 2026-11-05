package com.example.examplemod.client.magic;

import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.SpellDefinition;

/** 直接复用原版书页界面，不复制纹理或修改原版书与笔物品。 */
public final class MagicBookScreen extends BookViewScreen {
	private Button learn;

	public MagicBookScreen() {
		super(new BookAccess(List.of(Component.translatable("magic.mushokutenseiorigin.book_page"))));
	}

	@Override
	protected void init() {
		super.init();
		learn = addRenderableWidget(Button.builder(MagicScreens.text("learn"), button ->
				MagicClient.send("learn", SpellDefinition.WATER_BALL.id()))
				.bounds((width - 192) / 2 + 36, 154, 114, 20).build());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		boolean learned = MagicClient.state().learned().contains(SpellDefinition.WATER_BALL.id());
		learn.active = !learned;
		learn.setMessage(MagicScreens.text(learned ? "already_learned" : "learn"));
		super.extractRenderState(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean isPauseScreen() { return false; }
}
