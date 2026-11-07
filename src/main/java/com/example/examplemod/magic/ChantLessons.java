package com.example.examplemod.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/** 学习会话只信任服务端收到的逐拍输入，不接受客户端成功声明。 */
public final class ChantLessons {
	private static final Map<UUID, Lesson> ACTIVE = new HashMap<>();
	private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();
	public static boolean active(ServerPlayer player) { return ACTIVE.containsKey(player.getUUID()); }
	private static final class Lesson {
		final String spell; final long token; final String notes; final ChantScore score;
		final net.minecraft.world.item.Item book;
		final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		long start, offered = System.nanoTime(); int transportMillis; boolean ready;
		Lesson(ServerPlayer player, String spell, net.minecraft.world.item.Item book) {
			this.spell = spell; this.book = book; dimension = player.level().dimension(); token = RANDOM.nextLong();
			int count = ChantScore.noteCount(SpellDefinition.find(spell).rank(), RANDOM.nextInt());
			int[] keys = new int[count]; var text = new StringBuilder();
			for (int i = 0; i < count; i++) { keys[i] = RANDOM.nextInt(26); text.append((char)('a' + keys[i])); }
			notes = text.toString(); score = new ChantScore(keys);
		}
	}
	public static void handle(ServerPlayer player, ChantPayload packet) {
		if (packet.action().equals("start")) {
			if (ACTIVE.containsKey(player.getUUID())) return;
			var spell = SpellDefinition.find(packet.spell()); var state = MagicSystem.state(player);
			if (spell == null || !valid(player) || state.learned().contains(spell.id())) return;
			var stack = MagicBooks.teaches(player.getMainHandItem(), spell.id()) ? player.getMainHandItem() : player.getOffhandItem();
			if (!MagicBooks.teaches(stack, spell.id())) return;
			if (!MagicBooks.isLegacy(stack) && !spell.learningRequirementsMet(state.learned())) return;
			if (MagicBooks.isLegacy(stack)) {
				player.setAttached(MagicAttachments.STATE, state.learnWithoutRequirements(spell.id()));
				player.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.lesson_pass", Component.translatable("spell.mushokutenseiorigin." + spell.id())));
				return;
			}
			MagicPreparation.cancel(player); StoneCharge.cancel(player); PhaseTwoRuntime.stop(player);
			var lesson = new Lesson(player, spell.id(), stack.getItem()); ACTIVE.put(player.getUUID(), lesson);
			send(player, "offer", lesson); return;
		}
		var lesson = ACTIVE.get(player.getUUID());
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
		var lesson = ACTIVE.get(player.getUUID()); if (lesson == null) return;
		if (!available(player, lesson) || (!lesson.ready && System.nanoTime() - lesson.offered > 10_000_000_000L)) { clear(player); return; }
		if (lesson.ready) {
			finish(player, lesson);
			if (ACTIVE.containsKey(player.getUUID())) MagicChantEffects.tick(player);
		}
	}
	private static void finish(ServerPlayer player, Lesson lesson) {
		if (!lesson.score.finished()) return;
		boolean passed = lesson.score.passed();
		if (passed) player.setAttached(MagicAttachments.STATE, MagicSystem.state(player).learn(lesson.spell));
		player.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.lesson_" + (passed ? "pass" : "fail"), Component.translatable("spell.mushokutenseiorigin." + lesson.spell)));
		ACTIVE.remove(player.getUUID()); send(player, passed ? "pass" : "fail", lesson);
	}
	private static void send(ServerPlayer player, String action, Lesson lesson) {
		ServerPlayNetworking.send(player, new ChantPayload(action, lesson.spell, lesson.token, lesson.score.hits(), lesson.notes));
	}
	public static void clear(ServerPlayer player) { ACTIVE.remove(player.getUUID()); }
	private ChantLessons() { }
}
