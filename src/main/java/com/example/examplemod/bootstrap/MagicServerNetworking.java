package com.example.examplemod.bootstrap;
import com.example.examplemod.magic.*;

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

/** Server payload registration only; receiver policy has its own owner. */
public final class MagicServerNetworking {
	public static void initialize(){
		PayloadTypeRegistry.serverboundPlay().register(ChantPayload.TYPE, ChantPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(ChantPayload.TYPE, ChantPayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SpellVisualPayload.TYPE, SpellVisualPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ChantPayload.TYPE, (packet, context) -> ChantLessons.handle(context.player(), packet));
		PayloadTypeRegistry.serverboundPlay().register(MagicActionPayload.TYPE, MagicActionPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(MagicActionPayload.TYPE,
				(packet, context) -> com.example.examplemod.infrastructure.fabric.magic.MagicActionReceiver.handle(context.player(), packet));
	}
	private MagicServerNetworking(){}
}
