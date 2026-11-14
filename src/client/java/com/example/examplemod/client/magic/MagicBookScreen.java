package com.example.examplemod.client.magic;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.example.examplemod.magic.*;
import com.example.examplemod.item.MagicTextbookItem;

/** 双页教材：分类书使用学习谱；两本旧教材直接解锁课程。 */
public final class MagicBookScreen extends Screen {
	private final ItemStack book;
	private final List<String> lessons;
	private int page;
	private Button learn, back, next;
	private String notes = "";
	private long token, start, requested;
	private boolean waiting, running;
	private int completed, section;
	private final boolean[] held = new boolean[26];
	public MagicBookScreen(ItemStack book) {
		super(book.getHoverName()); this.book = book.copy(); lessons = MagicBooks.lessons(book);
	}
	private int left() { return (width - 300) / 2; }
	private int top() { return (height - 210) / 2; }
	private String spell() { return page == 0 ? "" : lessons.get(page - 1); }
	private void send(String action, int value) { ClientPlayNetworking.send(new ChantPayload(action, spell(), token, value, "")); }
	@Override
	protected void init() {
		learn = addRenderableWidget(Button.builder(MagicScreenStyle.text("learn"), b -> { waiting = !MagicBooks.isLegacy(book); requested = System.nanoTime(); send("start", 0); }).bounds(left() + 163, top() + 105, 116, 20).build());
		back = addRenderableWidget(Button.builder(Component.literal("<"), b -> { if (page > 0) page--; }).bounds(left() + 18, top() + 183, 24, 20).build());
		next = addRenderableWidget(Button.builder(Component.literal(">"), b -> { if (page < lessons.size()) page++; }).bounds(left() + 258, top() + 183, 24, 20).build());
	}
	public void receive(ChantPayload packet) {
		if (!packet.spell().equals(spell())) return;
		if ((packet.action().equals("offer") && waiting || packet.action().equals("section") && running)) {
			running = false; waiting = true; requested = System.nanoTime(); section = packet.value(); token = packet.token(); notes = packet.notes(); send("ready", 0);
		} else if (packet.token() == token && packet.action().equals("go")) {
			running = true; waiting = false; start = System.nanoTime();
			completed = 0;
			java.util.Arrays.fill(held, false);
		} else if (packet.token() == token && packet.action().equals("progress")) {
			completed = packet.value();
		} else if (packet.token() == token && packet.action().equals("fail")) { running = false; waiting = false; onClose(); }
		else if (packet.token() == token && packet.action().equals("pass")) { running = false; waiting = false; }
	}
	@Override
	public void tick() {
		var client = Minecraft.getInstance();
		if (client.player == null || !client.player.isAlive() || !client.isWindowActive()) { onClose(); return; }
		if (!client.player.getMainHandItem().is(book.getItem()) && !client.player.getOffhandItem().is(book.getItem())) { onClose(); return; }
		if (waiting && System.nanoTime() - requested > 10_000_000_000L) { send("cancel", 0); waiting = false; }
	}
	@Override
	public void removed() { if (running || waiting) send("cancel", 0); super.removed(); }
	@Override
	public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
		if (running && event.key() >= org.lwjgl.glfw.GLFW.GLFW_KEY_A && event.key() <= org.lwjgl.glfw.GLFW.GLFW_KEY_Z) {
			int letter = event.key() - org.lwjgl.glfw.GLFW.GLFW_KEY_A;
			if (!held[letter]) send("note", letter);
			held[letter] = true;
			return true;
		}
		return super.keyPressed(event);
	}
	@Override
	public boolean keyReleased(net.minecraft.client.input.KeyEvent event) {
		if (event.key() >= org.lwjgl.glfw.GLFW.GLFW_KEY_A && event.key() <= org.lwjgl.glfw.GLFW.GLFW_KEY_Z) held[event.key() - org.lwjgl.glfw.GLFW.GLFW_KEY_A] = false;
		return super.keyReleased(event);
	}
	private void paragraph(GuiGraphicsExtractor g, Component text, int x, int y, int maxLines) {
		var lines = font.split(text, 120);
		for (int i = 0; i < Math.min(maxLines, lines.size()); i++) g.text(font, lines.get(i), x, y + i * 10, 0xFF49351F, false);
	}
	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
		g.fill(0, 0, width, height, 0x80000000);
		int x = left(), y = top();
		g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, com.example.examplemod.ExampleMod.id("textures/gui/magic_textbook.png"), x, y, 0, 0, 300, 210, 300, 210);
		boolean active = running || waiting; back.active = !active && page > 0; next.active = !active && page < lessons.size();
		learn.visible = page > 0 && !active;
		var definition = SpellDefinition.find(spell());
		boolean known = MagicClient.state().learned().contains(spell());
		learn.active = definition != null && !known && (MagicBooks.isLegacy(book) || definition.learningRequirementsMet(MagicClient.state().learned()));
		learn.setMessage(MagicScreenStyle.text(known ? "already_learned" : learn.active ? "learn" : "missing_knowledge"));
		if (page == 0) {
			paragraph(g, title, x + 20, y + 22, 3);
			if (book.getItem() instanceof MagicTextbookItem textbook) paragraph(g, MagicScreenStyle.text("category_intro_" + textbook.category().id()), x + 20, y + 58, 11);
			else paragraph(g, MagicScreenStyle.text("legacy_readonly"), x + 20, y + 60, 10);
			g.text(font, MagicScreenStyle.text("contents"), x + 163, y + 22, 0xFF49351F, false);
			for (int i = 0; i < Math.min(13, lessons.size()); i++) g.text(font, MagicScreenStyle.name(lessons.get(i)), x + 163, y + 42 + i * 10, 0xFF49351F, false);
		} else if (definition != null) {
			g.text(font, MagicScreenStyle.name(spell()), x + 20, y + 23, 0xFF49351F, false);
			int introY = y + 42;
			if (!MagicBooks.isLegacy(book) && !definition.learningRequirements().isEmpty()) {
				String required = definition.learningRequirements().stream().map(MagicScreenStyle::name).map(Component::getString).collect(java.util.stream.Collectors.joining("、"));
				paragraph(g, Component.translatable("magic.mushokutenseiorigin.prerequisite", required), x + 20, introY, 2);
				introY += 19;
			}
			paragraph(g, MagicScreenStyle.text("lesson_intro_" + spell()), x + 20, introY, Math.max(3, (y + 116 - introY) / 10));
			g.text(font, Component.translatable("magic.mushokutenseiorigin.cost", definition.manaCost()), x + 20, y + 119, 0xFF49351F, false);
			paragraph(g, Component.translatable("magic.mushokutenseiorigin.cast_timing", definition.castingProfile().preparationTicks() / 20.0, definition.cooldownTicks() / 20.0), x + 20, y + 133, 2);
			g.text(font, Component.translatable("magic.mushokutenseiorigin.damage", definition.damage()), x + 20, y + 158, 0xFF49351F, false);
			if (!active) paragraph(g, MagicScreenStyle.text(MagicBooks.isLegacy(book) ? "legacy_direct_learn" : "score_rules"), x + 163, y + 30, 7);
		}
		if (running) {
			g.text(font, MagicScreenStyle.text("score_title"), x + 163, y + 24, 0xFF49351F, false);
			if (definition != null && definition.rank() >= 5) g.text(font, (section + 1) + " / " + SegmentedChantScore.sectionCount(definition.rank()), x + 252, y + 24, 0xFF49351F, false);
			for (int i = 0; i < notes.length(); i++) {
				int code = notes.charAt(i);
				if (code < 'a' || code > 'z') continue;
				int nx = x + 165 + (i % 4) * 29, ny = y + 54 + (i / 4) * 30;
				g.fill(nx, ny, nx + 25, ny + 23, i < completed ? 0xFF72A66B : i == completed ? 0xFFD1A653 : 0xFFAD9773);
				g.pose().pushMatrix();
				g.pose().translate(nx + 5, ny + 4);
				g.pose().scale(2, 2);
				g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
						net.minecraft.resources.Identifier.withDefaultNamespace("textures/font/ascii_sga.png"),
						0, 0, (code % 16) * 8, (code / 16) * 8, 8, 8, 128, 128);
				g.pose().popMatrix();
			}
			paragraph(g, MagicScreenStyle.text("score_rules"), x + 163, y + 125, 5);
		}
		g.centeredText(font, (page + 1) + " / " + (lessons.size() + 1), x + 150, y + 190, 0xFF49351F);
		super.extractRenderState(g, mx, my, delta);
	}
	@Override
	public boolean isPauseScreen() { return false; }
}
