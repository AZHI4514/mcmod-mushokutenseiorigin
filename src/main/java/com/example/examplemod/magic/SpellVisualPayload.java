package com.example.examplemod.magic;

import com.example.examplemod.ExampleMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

/** Cosmetic only: no client input and no damage or world state in this packet. */
public record SpellVisualPayload(String spell, Vec3 position, Vec3 direction) implements CustomPacketPayload {
	public static final Type<SpellVisualPayload> TYPE = new Type<>(ExampleMod.id("spell_visual"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpellVisualPayload> CODEC = StreamCodec.of(
			(b,p) -> { b.writeUtf(p.spell,64); b.writeDouble(p.position.x); b.writeDouble(p.position.y); b.writeDouble(p.position.z); b.writeDouble(p.direction.x); b.writeDouble(p.direction.y); b.writeDouble(p.direction.z); },
			b -> new SpellVisualPayload(b.readUtf(64),new Vec3(b.readDouble(),b.readDouble(),b.readDouble()),new Vec3(b.readDouble(),b.readDouble(),b.readDouble())));
	@Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
