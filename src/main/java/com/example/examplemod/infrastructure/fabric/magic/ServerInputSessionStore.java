package com.example.examplemod.infrastructure.fabric.magic;
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

/** Transient server-owned input sessions. */
public final class ServerInputSessionStore {
	private static final AttachmentType<InputSessions<ResourceKey<Level>>> INPUT_SESSIONS = AttachmentRegistry.create(
		ExampleMod.id("input_sessions_runtime"), builder -> builder.initializer(InputSessions::new));
	public static InputSessions<ResourceKey<Level>> sessions(ServerPlayer player) {
		return player.level().getServer().overworld().getAttachedOrCreate(INPUT_SESSIONS);
	}
	public static void clear(net.minecraft.server.MinecraftServer server){server.overworld().getAttachedOrCreate(INPUT_SESSIONS).clear();}
	private ServerInputSessionStore(){}
}
