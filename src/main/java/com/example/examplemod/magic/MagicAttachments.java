package com.example.examplemod.magic;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import com.example.examplemod.ExampleMod;

public final class MagicAttachments {
	public static final AttachmentType<MagicState> STATE = AttachmentRegistry.create(
			ExampleMod.id("magic_state"), builder -> builder
					.initializer(() -> MagicState.initial(MagicConfig.maximumMana()))
					.persistent(MagicState.CODEC).copyOnDeath()
					.syncWith(ByteBufCodecs.fromCodecWithRegistries(MagicState.CODEC), AttachmentSyncPredicate.targetOnly()));
	public static final AttachmentType<Integer> CAST_COLOR = AttachmentRegistry.create(ExampleMod.id("cast_color"), builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> CAST_TICKS = AttachmentRegistry.create(
			ExampleMod.id("cast_ticks"), builder -> builder.initializer(() -> 0)
					.syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all()));

	public static final AttachmentType<Integer> CHARGE_MANA = AttachmentRegistry.create(
			ExampleMod.id("charge_mana"), builder -> builder.initializer(() -> 0).persistent(Codec.INT).copyOnDeath()
					.syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly()));
	public static final AttachmentType<Integer> CHARGE_BUDGET = AttachmentRegistry.create(
			ExampleMod.id("charge_budget"), builder -> builder.initializer(() -> 0)
					.syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.targetOnly()));
	/** 在模组初始化阶段注册附件，不能等首个玩家加入后才注册。 */
	public static void initialize() {
	}

	private MagicAttachments() {
	}
}
