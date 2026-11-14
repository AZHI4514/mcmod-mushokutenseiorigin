package com.example.examplemod.magic;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import com.example.examplemod.ExampleMod;

public final class MagicAttachments {
	public static final AttachmentType<Boolean> AURA_ACTIVE=AttachmentRegistry.create(ExampleMod.id("aura_active"),b->b.initializer(()->false).syncWith(ByteBufCodecs.BOOL,AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> FOG_LEFT = AttachmentRegistry.create(ExampleMod.id("fog_left"),builder->builder.initializer(()->0).syncWith(ByteBufCodecs.VAR_INT,AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> FOG_RANGE = AttachmentRegistry.create(ExampleMod.id("fog_range"),builder->builder.initializer(()->256).syncWith(ByteBufCodecs.VAR_INT,AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> FOG_LINGER = AttachmentRegistry.create(ExampleMod.id("fog_linger"),builder->builder.initializer(()->10).syncWith(ByteBufCodecs.VAR_INT,AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> FOG_COLOR = AttachmentRegistry.create(ExampleMod.id("fog_color"),builder->builder.initializer(()->12044753).syncWith(ByteBufCodecs.VAR_INT,AttachmentSyncPredicate.all()));
	public static final AttachmentType<Boolean> HYPNOSIS_ACTIVE=AttachmentRegistry.create(ExampleMod.id("hypnosis_active"),b->b.initializer(()->false).persistent(Codec.BOOL));
	public static final AttachmentType<Long> HYPNOSIS_UNTIL=AttachmentRegistry.create(ExampleMod.id("hypnosis_until"),b->b.initializer(()->0L).persistent(Codec.LONG));
	public static final AttachmentType<Long> COUNTER_UNTIL=AttachmentRegistry.create(ExampleMod.id("counter_until"),b->b.initializer(()->0L).persistent(Codec.LONG));
	public static final AttachmentType<Long> SPIKES_STARTED=AttachmentRegistry.create(ExampleMod.id("spikes_started"),builder->builder.initializer(()->0L).syncWith(ByteBufCodecs.LONG,AttachmentSyncPredicate.all()));
	public static final AttachmentType<java.util.List<net.minecraft.core.BlockPos>> SPIKE_BASES=AttachmentRegistry.create(ExampleMod.id("spike_bases"),builder->builder.initializer(()->java.util.List.<net.minecraft.core.BlockPos>of()).syncWith(ByteBufCodecs.fromCodecWithRegistries(net.minecraft.core.BlockPos.CODEC.listOf()),AttachmentSyncPredicate.all()));
	public static final AttachmentType<java.util.Map<String,Long>> FIELD_HITS = AttachmentRegistry.create(ExampleMod.id("field_hits"), builder -> builder.initializer(() -> java.util.Map.<String,Long>of()).persistent(Codec.unboundedMap(Codec.STRING,Codec.LONG)));
	public static final AttachmentType<Long> CONTROL_END = AttachmentRegistry.create(ExampleMod.id("control_end"), builder -> builder.initializer(() -> Long.MIN_VALUE/2).persistent(Codec.LONG));
	public static final AttachmentType<java.util.Map<String, Integer>> LESSON_SECTIONS = AttachmentRegistry.create(ExampleMod.id("lesson_sections"), builder -> builder.initializer(() -> java.util.Map.<String,Integer>of()).persistent(Codec.unboundedMap(Codec.STRING, Codec.INT)).copyOnDeath());
	public static final AttachmentType<Boolean> ARC_BOUND_VISUAL = AttachmentRegistry.create(ExampleMod.id("arc_bound_visual"), builder -> builder.initializer(() -> false).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));
	public static final AttachmentType<Boolean> WIND_BOUND_VISUAL = AttachmentRegistry.create(ExampleMod.id("wind_bound_visual"), builder -> builder.initializer(() -> false).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));
	/** Cosmetic identity only; does not expose learned spells or mana to other players. */
	public static final AttachmentType<String> VISUAL_SPELL = AttachmentRegistry.create(ExampleMod.id("visual_spell"), builder -> builder.initializer(() -> "").syncWith(ByteBufCodecs.STRING_UTF8, AttachmentSyncPredicate.all()));
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

	public static final AttachmentType<Integer> PREPARE_LEFT = AttachmentRegistry.create(ExampleMod.id("prepare_left"), builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> PREPARE_TOTAL = AttachmentRegistry.create(ExampleMod.id("prepare_total"), builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all()));
	public static final AttachmentType<Integer> RECOVERY_TOTAL = AttachmentRegistry.create(ExampleMod.id("recovery_total"), builder -> builder.initializer(() -> 0).syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all()));
	/** 在模组初始化阶段注册附件，不能等首个玩家加入后才注册。 */
	public static void initialize() {
	}

	private MagicAttachments() {
	}
}
