package com.example.examplemod.magic;

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

/** 所有状态变化发生在服务端线程；一次请求最多执行一次成功施法。 */
public final class MagicSystem {
	public static final EntityType<WaterBallEntity> WATER_BALL = com.example.examplemod.bootstrap.MagicEntityTypes.WATER_BALL;
	public static final EntityType<WaterBallEntity> FIREBALL = com.example.examplemod.bootstrap.MagicEntityTypes.FIREBALL;
	public static final EntityType<WaterBallEntity> WIND_BALL = com.example.examplemod.bootstrap.MagicEntityTypes.WIND_BALL;
	public static final EntityType<WaterBallEntity> ROCK_BULLET = com.example.examplemod.bootstrap.MagicEntityTypes.ROCK_BULLET;
	public static final EntityType<WaterBallEntity> WATER_ARROW = com.example.examplemod.bootstrap.MagicEntityTypes.WATER_ARROW;
	public static final EntityType<WaterBallEntity> ICE_SMASH = com.example.examplemod.bootstrap.MagicEntityTypes.ICE_SMASH;
	public static final EntityType<WaterBallEntity> ICE_BLADE = com.example.examplemod.bootstrap.MagicEntityTypes.ICE_BLADE;
	public static final EntityType<MagicAreaEntity> MAGIC_AREA = com.example.examplemod.bootstrap.MagicEntityTypes.MAGIC_AREA;
	public static final EntityType<FlameSliceEntity> FLAME_SLICE = com.example.examplemod.bootstrap.MagicEntityTypes.FLAME_SLICE;
	public static final EntityType<SweptSpellProjectile> WATER_CANNON = com.example.examplemod.bootstrap.MagicEntityTypes.WATER_CANNON;
	public static final EntityType<SweptSpellProjectile> WATER_SPLASH = com.example.examplemod.bootstrap.MagicEntityTypes.WATER_SPLASH;
	public static final EntityType<SweptSpellProjectile> ICICLE_FIELD = com.example.examplemod.bootstrap.MagicEntityTypes.ICICLE_FIELD;
	public static final EntityType<SweptSpellProjectile> TORNADO_IMPACT = com.example.examplemod.bootstrap.MagicEntityTypes.TORNADO_IMPACT;
	public static final EntityType<SweptSpellProjectile> EXODUS_FLAME = com.example.examplemod.bootstrap.MagicEntityTypes.EXODUS_FLAME;
	public static final EntityType<SweptSpellProjectile> EX_FLAME = com.example.examplemod.bootstrap.MagicEntityTypes.EX_FLAME;
	public static final EntityType<SweptSpellProjectile> SONIC_BOOM = com.example.examplemod.bootstrap.MagicEntityTypes.SONIC_BOOM;
	public static final EntityType<SweptSpellProjectile> STONE_CANNON = com.example.examplemod.bootstrap.MagicEntityTypes.STONE_CANNON;
	public static final EntityType<DirectionalSpellCarrier> PHASE_TWO = com.example.examplemod.bootstrap.MagicEntityTypes.PHASE_TWO;
	public static EntityType<SweptSpellProjectile> intermediateType(SpellDefinition spell) { return com.example.examplemod.bootstrap.MagicEntityTypes.intermediateType(spell); }
	static EntityType<WaterBallEntity> projectileType(SpellDefinition spell) { return com.example.examplemod.bootstrap.MagicEntityTypes.projectileType(spell); }

	public static void initialize(){com.example.examplemod.bootstrap.MagicBootstrap.initialize();}

	public static MagicState state(ServerPlayer player) {
		return player.getAttachedOrCreate(MagicAttachments.STATE);
	}

	/** 配置重载或后续成长系统可调用；增加上限不自动回满。 */
	public static void setMaximum(ServerPlayer player, int maximum) {
		player.setAttached(MagicAttachments.STATE, state(player).withMaximum(maximum));
	}

	public static void restoreMana(ServerPlayer player, int amount) {
		player.setAttached(MagicAttachments.STATE, state(player).restoreMana(amount));
	}

	public static void successfulSleep(ServerPlayer player) {
		player.setAttached(MagicAttachments.STATE, state(player).refill());
	}


	private MagicSystem() {
	}
}
