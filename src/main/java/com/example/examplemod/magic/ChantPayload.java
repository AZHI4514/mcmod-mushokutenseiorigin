package com.example.examplemod.magic;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.example.examplemod.ExampleMod;

/** 双向有界协议：start/ready/note/cancel 与 offer/go/pass/fail。 */
public record ChantPayload(String action, String spell, long token, int value, String notes) implements CustomPacketPayload {
	public static final Type<ChantPayload> TYPE = new Type<>(ExampleMod.id("chant_lesson"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ChantPayload> CODEC = StreamCodec.of(
			(b, p) -> { b.writeUtf(p.action, 12); b.writeUtf(p.spell, 64); b.writeLong(p.token); b.writeVarInt(p.value); b.writeUtf(p.notes, 12); },
			b -> new ChantPayload(b.readUtf(12), b.readUtf(64), b.readLong(), b.readVarInt(), b.readUtf(12)));
	@Override
	public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
