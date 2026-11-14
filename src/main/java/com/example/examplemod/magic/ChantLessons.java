package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/** 学习会话只信任服务端收到的逐拍输入，不接受客户端成功声明。 */
public final class ChantLessons {
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<Map<UUID,Lesson>> SESSIONS =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("chant_lessons_runtime"),
				builder -> builder.initializer(() -> new HashMap<UUID,Lesson>()));
	}
	private static Map<UUID,Lesson> sessions(ServerPlayer player) {
		return player.level().getServer().overworld().getAttachedOrCreate(Runtime.SESSIONS);
	}
	private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();
	public static boolean active(ServerPlayer player) { return sessions(player).containsKey(player.getUUID()); }
	private static final class Lesson {
		final int section; final String spell; final long token; final String notes; final ChantScore score;
		final net.minecraft.world.item.Item book;
		final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		long start, offered = System.nanoTime(); int transportMillis; boolean ready;
		Lesson(ServerPlayer player, String spell, net.minecraft.world.item.Item book) {
			this.spell = spell; this.book = book; section = Math.clamp(player.getAttachedOrCreate(MagicAttachments.LESSON_SECTIONS).getOrDefault(spell, 0), 0, SegmentedChantScore.sectionCount(SpellDefinition.find(spell).rank()) - 1); dimension = player.level().dimension(); token = RANDOM.nextLong();
			int count = ChantScore.noteCount(SpellDefinition.find(spell).rank(), RANDOM.nextInt());
			int[] keys = new int[count]; var text = new StringBuilder();
			for (int i = 0; i < count; i++) { keys[i] = RANDOM.nextInt(26); text.append((char)('a' + keys[i])); }
			notes = text.toString(); score = new ChantScore(keys);
		}
	}
	public static void handle(ServerPlayer player, ChantPayload packet) {
		if (packet.action().equals("start")) {
			if (sessions(player).containsKey(player.getUUID())) return;
			var spell = SpellDefinition.find(packet.spell()); var state = MagicSystem.state(player);
			if (spell == null || !valid(player) || state.learned().contains(spell.id())) return;
			var stack = MagicBooks.teaches(player.getMainHandItem(), spell.id()) ? player.getMainHandItem() : player.getOffhandItem();
			if (!MagicBooks.teaches(stack, spell.id())) return;
			if (!MagicBooks.isLegacy(stack) && !spell.learningRequirementsMet(state.learned())) return;
			if (MagicBooks.isLegacy(stack)) {
				player.setAttached(MagicAttachments.STATE, MagicState.from(new com.example.examplemod.magic.application.learning.LearnSpell().execute(state.pure(), spell.id(), MagicBooks.policy(stack), MagicBooks.teaches(stack, spell.id()), false).state()));
				player.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.lesson_pass", Component.translatable("spell.mushokutenseiorigin." + spell.id())));
				return;
			}
			PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player); PlayerSpellRuntime.stop(player);
			var lesson = new Lesson(player, spell.id(), stack.getItem()); sessions(player).put(player.getUUID(), lesson);
			send(player, "offer", lesson); return;
		}
		var lesson = sessions(player).get(player.getUUID());
		// Closing before the offer arrives must also cancel the pending handshake.
		if (lesson != null && packet.action().equals("cancel") && packet.token() == 0 && !lesson.ready && lesson.spell.equals(packet.spell())) { clear(player); return; }
		if (lesson == null || lesson.token != packet.token() || !lesson.spell.equals(packet.spell())) return;
		if (packet.action().equals("cancel")) { clear(player); return; }
		if (!available(player, lesson)) { clear(player); return; }
		if (packet.action().equals("ready") && !lesson.ready) {
			// Offer/ready measures one round trip; go/note crosses the same two directions.
			lesson.transportMillis = (int)Math.clamp((System.nanoTime() - lesson.offered) / 1_000_000, 0, 500);
			lesson.ready = true; lesson.start = System.nanoTime(); send(player, "go", lesson); return;
		}
		if (packet.action().equals("note") && lesson.ready) {
			lesson.score.press(packet.value());
			ServerPlayNetworking.send(player, new ChantPayload("progress", lesson.spell, lesson.token, lesson.score.index(), ""));
			finish(player, lesson);
		}
	}
	private static long elapsed(Lesson lesson) { return (System.nanoTime() - lesson.start) / 1_000_000 - lesson.transportMillis; }
	private static boolean valid(ServerPlayer player) {
		return player.isAlive() && !player.isSpectator() && !player.isSleeping() && !player.isUsingItem() && player.containerMenu == player.inventoryMenu;
	}
	private static boolean available(ServerPlayer player, Lesson lesson) {
		return valid(player) && lesson.dimension.equals(player.level().dimension()) && (player.getMainHandItem().is(lesson.book) || player.getOffhandItem().is(lesson.book));
	}
	public static void tick(ServerPlayer player) {
		var lesson = sessions(player).get(player.getUUID()); if (lesson == null) return;
		if (!available(player, lesson) || (!lesson.ready && System.nanoTime() - lesson.offered > 10_000_000_000L)) { clear(player); return; }
		if (lesson.ready) {
			finish(player, lesson);
			if (sessions(player).containsKey(player.getUUID())) MagicChantEffects.tick(player);
		}
	}
	private static void finish(ServerPlayer player, Lesson lesson) {
		if (!lesson.score.finished()) return;
		boolean passed = lesson.score.passed();
		int total = SegmentedChantScore.sectionCount(SpellDefinition.find(lesson.spell).rank());
		if (passed && lesson.section + 1 < total) {
			var saved = new HashMap<>(player.getAttachedOrCreate(MagicAttachments.LESSON_SECTIONS));
			saved.put(lesson.spell, lesson.section + 1);
			player.setAttached(MagicAttachments.LESSON_SECTIONS, Map.copyOf(saved));
			var next = new Lesson(player, lesson.spell, lesson.book);
			sessions(player).put(player.getUUID(), next); send(player, "section", next); return;
		}
		if (passed) {
			var saved = new HashMap<>(player.getAttachedOrCreate(MagicAttachments.LESSON_SECTIONS));
			saved.remove(lesson.spell); player.setAttached(MagicAttachments.LESSON_SECTIONS, Map.copyOf(saved));
		}
		if (passed) player.setAttached(MagicAttachments.STATE, MagicState.from(new com.example.examplemod.magic.application.learning.LearnSpell().execute(MagicSystem.state(player).pure(), lesson.spell, MagicBooks.policy(new net.minecraft.world.item.ItemStack(lesson.book)), available(player, lesson), true).state()));
		player.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.lesson_" + (passed ? "pass" : "fail"), Component.translatable("spell.mushokutenseiorigin." + lesson.spell)));
		sessions(player).remove(player.getUUID()); send(player, passed ? "pass" : "fail", lesson);
	}
	private static void send(ServerPlayer player, String action, Lesson lesson) {
		ServerPlayNetworking.send(player, new ChantPayload(action, lesson.spell, lesson.token, lesson.section, lesson.notes));
	}
	public static void clear(ServerPlayer player) { sessions(player).remove(player.getUUID()); }
	private ChantLessons() { }
}
