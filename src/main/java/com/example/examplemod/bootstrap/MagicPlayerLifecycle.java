package com.example.examplemod.bootstrap;
import com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PreparationRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.CommunicationAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;
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

/** Explicit player lifecycle and ordered tick composition. */
public final class MagicPlayerLifecycle {
	public static void initialize(){
		ServerPlayerEvents.JOIN.register(player -> {
			com.example.examplemod.infrastructure.fabric.magic.ServerInputSessionStore.sessions(player).reset(player.getUUID());
			ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player);
			player.setAttached(MagicAttachments.STATE, state(player));
		});
		ServerPlayerEvents.LEAVE.register(player -> { ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player); PlayerSpellRuntime.clear(player); com.example.examplemod.infrastructure.fabric.magic.ServerInputSessionStore.sessions(player).remove(player.getUUID()); AreaRuntimeAdapter.clear(player); LightLeases.release(player); CommunicationAdapter.clear(player); });
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> { server.getPlayerList().getPlayers().forEach(player -> { ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player); PlayerSpellRuntime.clear(player); }); PreparationRuntimeAdapter.clear(server); StoneCharge.clear(server); PlayerSpellRuntime.clearChannels(server); com.example.examplemod.infrastructure.fabric.magic.ServerInputSessionStore.clear(server); AreaRuntimeAdapter.clearAll(server); });
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, player, alive) -> {
			com.example.examplemod.infrastructure.fabric.magic.ServerInputSessionStore.sessions(player).reset(player.getUUID());
			AreaRuntimeAdapter.clear(player);
			PlayerSpellRuntime.clear(oldPlayer);
			ChantLessons.clear(player); PreparationRuntimeAdapter.cancel(player); StoneCharge.cancel(player);
			if (!alive) player.setAttached(MagicAttachments.STATE, state(player).refill());
			player.setAttached(MagicAttachments.CAST_TICKS, 0);
		});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				Session<ResourceKey<Level>> session = com.example.examplemod.infrastructure.fabric.magic.ServerInputSessionStore.sessions(player).get(player.getUUID());
				session.age++;
				if (session.dimension != null && !session.dimension.equals(player.level().dimension())) { AreaRuntimeAdapter.clear(player); PlayerSpellRuntime.clear(player); PreparationRuntimeAdapter.cancel(player); session.blocked=true; }
				session.dimension = player.level().dimension();
				if (player.isAlive()) player.setAttached(MagicAttachments.STATE, state(player).tick());
				if (player.isUsingItem() || !player.isAlive()) { session.blocked = true; PreparationRuntimeAdapter.cancel(player); AreaRuntimeAdapter.breakMaintenance(player); PlayerSpellRuntime.breakMaintenance(player); }
				if (!player.isAlive()) AreaRuntimeAdapter.clear(player);
				SummonRuntimeAdapter.tickOwner(player);PoweredBarrierRuntimeAdapter.tickOwner(player);
				ChantLessons.tick(player);
				PreparationRuntimeAdapter.tick(player);
				if (StoneCharge.tick(player)) session.blocked=true;
				if (PlayerSpellRuntime.tick(player)) session.blocked=true;
				int pose = player.getAttachedOrElse(MagicAttachments.CAST_TICKS, 0);
				if (pose > 0) player.setAttached(MagicAttachments.CAST_TICKS,
						player.isUsingItem() ? 0 : pose - 1);
			}
		});
	}
	private MagicPlayerLifecycle(){}
}
