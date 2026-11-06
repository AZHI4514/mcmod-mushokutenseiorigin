package com.example.examplemod.magic;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.example.examplemod.ExampleMod;

/** 有界请求：服务端不信任客户端的魔力、伤害、计时和学习状态。 */
public record MagicActionPayload(String action, String spell, int sequence, float yaw, float pitch) implements CustomPacketPayload {
	public static final Type<MagicActionPayload> TYPE = new Type<>(ExampleMod.id("magic_action"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MagicActionPayload> CODEC = StreamCodec.of(
			(buffer, packet) -> {
				buffer.writeUtf(packet.action(), 16);
				buffer.writeUtf(packet.spell(), 64);
				buffer.writeVarInt(packet.sequence());
				buffer.writeFloat(packet.yaw());
				buffer.writeFloat(packet.pitch());
			}, buffer -> new MagicActionPayload(buffer.readUtf(16), buffer.readUtf(64), buffer.readVarInt(), buffer.readFloat(), buffer.readFloat()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
