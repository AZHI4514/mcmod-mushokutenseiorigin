package com.example.examplemod.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

/** 服务端准备阶段不预扣魔力；完成后重新校验并原子释放。 */
public final class MagicPreparation {
	private static final Map<UUID, Pending> ACTIVE = new HashMap<>();
	private static final class Pending {
		final SpellDefinition spell;
		final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
		MagicAim aim;
		int remaining, heartbeat;
		Pending(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
			this.spell = spell; this.aim = aim; dimension = player.level().dimension();
			remaining = spell.castingProfile().preparationTicks();
		}
	}
	private static boolean available(ServerPlayer player, SpellDefinition spell) {
		var state = MagicSystem.state(player);
		return player.isAlive() && !player.isSpectator() && !player.isUsingItem() && !player.isSleeping()
				&& player.containerMenu == player.inventoryMenu && state.cooldown() == 0
				&& state.selected().equals(spell.id()) && state.learned().contains(spell.id()) && state.loaded().contains(spell.id())
				&& state.mana() >= (PhaseTwoSpells.isChannel(spell) ? 1 : spell.manaCost())
				&& SpellHooks.canUse(player, spell, SpellHooks.CastSource.KEYBOARD);
	}
	public static void request(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
		if (spell == null || !aim.valid() || ChantLessons.active(player) || !MagicSystem.state(player).selected().equals(spell.id())) return;
		if (PhaseTwoRuntime.channelActive(player)) { PhaseTwoRuntime.channel(player, spell, aim); return; }
		if (StoneCharge.active(player)) { StoneCharge.heartbeat(player, aim); return; }
		var pending = ACTIVE.get(player.getUUID());
		if (pending != null) {
			if (pending.spell == spell) { pending.aim = aim; pending.heartbeat = 0; }
			else cancel(player);
			return;
		}
		if (!available(player, spell) || !SpellExecutors.canExecute(player, spell, aim)) return;
		var next = new Pending(player, spell, aim);
		ACTIVE.put(player.getUUID(), next);
		player.setAttached(MagicAttachments.VISUAL_SPELL,spell.id());
		player.setAttached(MagicAttachments.PREPARE_TOTAL, next.remaining);
		player.setAttached(MagicAttachments.PREPARE_LEFT, next.remaining);
		player.setAttached(MagicAttachments.CAST_COLOR, 0);
		player.setAttached(MagicAttachments.CAST_TICKS, next.remaining + 1);
	}
	public static void tick(ServerPlayer player) {
		var pending = ACTIVE.get(player.getUUID());
		if (pending == null) return;
		if (!available(player, pending.spell) || !pending.dimension.equals(player.level().dimension()) || ++pending.heartbeat > 10) { cancel(player); return; }
		MagicChantEffects.tick(player);
		player.setAttached(MagicAttachments.PREPARE_LEFT, --pending.remaining);
		if (pending.remaining > 0) return;
		ACTIVE.remove(player.getUUID());
		player.setAttached(MagicAttachments.CAST_TICKS, 0);
		var spell = pending.spell;
		if (!SpellExecutors.canExecute(player, spell, pending.aim)) return;
		if (PhaseTwoSpells.isChannel(spell)) { PhaseTwoRuntime.channel(player, spell, pending.aim); return; }
		if (spell == SpellDefinition.STONE_CANNON) { StoneCharge.begin(player, pending.aim); return; }
		var before = MagicSystem.state(player);
		if (!before.canCast(spell)) return;
		player.setAttached(MagicAttachments.STATE, before.cast(spell));
		if (!SpellExecutors.execute(player, spell, pending.aim)) { player.setAttached(MagicAttachments.STATE, before); return; }
		player.setAttached(MagicAttachments.RECOVERY_TOTAL, spell.cooldownTicks());
		MagicGlow.success(player, spell);
		MagicSounds.listener.accept(player, MagicSounds.Phase.RELEASE);
	}
	public static void cancel(ServerPlayer player) {
		if (ACTIVE.remove(player.getUUID()) != null) player.setAttached(MagicAttachments.CAST_TICKS, 0);
		player.setAttached(MagicAttachments.PREPARE_LEFT, 0);
		player.setAttached(MagicAttachments.PREPARE_TOTAL, 0);
	}
	private MagicPreparation() { }
}
