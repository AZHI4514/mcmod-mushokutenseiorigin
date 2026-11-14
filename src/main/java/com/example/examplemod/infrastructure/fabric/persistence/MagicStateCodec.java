package com.example.examplemod.infrastructure.fabric.persistence;

import com.example.examplemod.magic.MagicState;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** Old field names and constructor normalization retained exactly. */
public final class MagicStateCodec {
	public static final Codec<MagicState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("mana").forGetter(MagicState::mana),
			Codec.INT.fieldOf("maximum").forGetter(MagicState::maximum),
			Codec.INT.fieldOf("online_ticks").forGetter(MagicState::onlineTicks),
			Codec.INT.fieldOf("cooldown").forGetter(MagicState::cooldown),
			Codec.STRING.listOf().fieldOf("learned").forGetter(MagicState::learned),
			Codec.STRING.listOf().fieldOf("loaded").forGetter(MagicState::loaded),
			Codec.STRING.fieldOf("selected").forGetter(MagicState::selected)
	).apply(instance, MagicState::new));

	private MagicStateCodec() { }
}
