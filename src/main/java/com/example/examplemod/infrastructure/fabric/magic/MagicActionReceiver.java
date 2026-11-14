package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import static com.example.examplemod.magic.MagicSystem.state;

import com.example.examplemod.magic.feature.input.InputSessions;
import com.example.examplemod.magic.feature.input.InputSessions.Session;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import com.example.examplemod.ExampleMod;

/** Network input validation and action dispatch; never registers entities or events. */
public final class MagicActionReceiver {
	public static void handle(ServerPlayer player, MagicActionPayload packet) {
		Session<ResourceKey<Level>> session = ServerInputSessionStore.sessions(player).get(player.getUUID());
		if (!session.accept(packet.sequence())) return;
		if (packet.action().equals("release")) { PreparationRuntimeAdapter.cancel(player); StoneCharge.release(player,new MagicAim(packet.yaw(),packet.pitch())); PlayerSpellRuntime.stop(player); PlayerSpellRuntime.breakMaintenance(player); AreaRuntimeAdapter.breakMaintenance(player); session.blocked = true; return; }
		if (packet.action().equals("stop")) { ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player); PlayerSpellRuntime.stop(player); PlayerSpellRuntime.breakMaintenance(player); AreaRuntimeAdapter.breakMaintenance(player); session.blocked = true; player.setAttached(MagicAttachments.CAST_TICKS, 0); return; }
		if (packet.action().equals("arm")) {
			if (!player.isUsingItem() && player.isAlive() && !player.isSpectator()) session.blocked = false;
			return;
		}
		if (!player.isAlive() || player.isSpectator()) return;
		switch (packet.action()) {
			case "mode" -> { if(!player.isUsingItem()&&player.containerMenu==player.inventoryMenu)MaintainedFieldRuntimeAdapter.cycle(player,SpellDefinition.find(state(player).selected())); }
			case "learn" -> { /* Requires validated rhythm challenge. */ }
			case "toggle" -> { PlayerSpellRuntime.stop(player); PlayerSpellRuntime.breakMaintenance(player); ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player); player.setAttached(MagicAttachments.STATE, state(player).toggle(packet.spell())); }
			case "select" -> { PlayerSpellRuntime.stop(player); PlayerSpellRuntime.breakMaintenance(player); ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player); player.setAttached(MagicAttachments.STATE, state(player).select(packet.spell())); }
			case "cast" -> {
				if (player.isUsingItem() || player.containerMenu != player.inventoryMenu || player.isSleeping()) {
					ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player);
					PlayerSpellRuntime.stop(player);
					session.blocked = true;
					player.setAttached(MagicAttachments.CAST_TICKS, 0);
					return;
				}
				if (session.blocked) return;
				SpellDefinition spell = SpellDefinition.find(packet.spell());
				SequentialMagic.request(player, spell, new MagicAim(packet.yaw(), packet.pitch()));
			}
			default -> { }
		}
	}

	private MagicActionReceiver(){}
}
