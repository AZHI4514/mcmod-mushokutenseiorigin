package com.example.examplemod.magic;

import java.util.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.*;

/** Per-player channel sessions, committed burst queues, physical shields. */
public final class PlayerSpellRuntime {
	public static final AttachmentType<Integer> CHANNEL_DEBT=com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.CHANNEL_DEBT;
	public static void clearChannels(net.minecraft.server.MinecraftServer server) {
		com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.clearServer(server);
		com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.clearServer(server);
		com.example.examplemod.infrastructure.fabric.magic.FlamePillarBurnRuntimeAdapter.clearServer(server);
		com.example.examplemod.infrastructure.fabric.magic.ContactCooldownRuntimeAdapter.clearServer(server);
		com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldRuntimeAdapter.clearServer(server);
	}

	public static void initialize() {
		com.example.examplemod.infrastructure.fabric.magic.ContactCooldownRuntimeAdapter.initialize();com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.initialize();com.example.examplemod.infrastructure.fabric.magic.FlamePillarBurnRuntimeAdapter.initialize();
		MagicWaterSources.initialize();MagicSpikes.initialize();
		com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.initialize();
	}
	public static boolean contactReady(ServerLevel level,UUID area,UUID target,long now) {
		return com.example.examplemod.infrastructure.fabric.magic.ContactCooldownRuntimeAdapter.claim(level,area,target,now);
	}
	public static boolean channelActive(ServerPlayer player) { return com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.channelActive(player); }
	public static void channel(ServerPlayer player,SpellDefinition spell,MagicAim aim) { com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.channel(player,spell,aim); }
	public static boolean generateWater(ServerPlayer player,MagicAim aim) { return com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.generateWater(player,aim); }
	public static void stop(ServerPlayer player) { com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.stop(player); }
	public static boolean shield(ServerPlayer player,MagicAim aim) {
		return com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldRuntimeAdapter.release(player,aim);
	}
	public static void breakMaintenance(ServerPlayer player) {
		com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldRuntimeAdapter.breakMaintenance(player);
	}

	public static boolean tick(ServerPlayer player) {
		long now=player.level().getGameTime();
		com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.tick(player); com.example.examplemod.infrastructure.fabric.magic.FlamePillarBurnRuntimeAdapter.tick(player);
		com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldRuntimeAdapter.tick(player);

		com.example.examplemod.infrastructure.fabric.magic.ContactCooldownRuntimeAdapter.prune((ServerLevel)player.level(),now);
		return com.example.examplemod.infrastructure.fabric.magic.ChannelRuntimeAdapter.tick(player);
	}

	public static boolean intercept(Projectile projectile) {
		return com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.intercept(projectile);
	}

	public static void clear(ServerPlayer player) {
		stop(player);com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.clear(player);com.example.examplemod.infrastructure.fabric.magic.FlamePillarBurnRuntimeAdapter.clear(player);
		com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldRuntimeAdapter.clear(player); com.example.examplemod.infrastructure.fabric.magic.ContactCooldownRuntimeAdapter.clear(player);
	}
	/** Shield collision uses the oriented volume, never its larger axis-aligned bounds. */
	public static Vec3 clipShieldMovement(Entity entity,Vec3 movement) {
		return com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.clipShieldMovement(entity,movement);
	}

	private PlayerSpellRuntime() { }
}
