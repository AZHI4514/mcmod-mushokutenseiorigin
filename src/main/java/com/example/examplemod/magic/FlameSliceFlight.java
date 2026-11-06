package com.example.examplemod.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** 火断直线飞行路程；生物命中或完全遮挡后消失，旧存档多余的 targets 字段被忽略。 */
public record FlameSliceFlight(double travelled) {
	public static final Codec<FlameSliceFlight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("travelled").forGetter(FlameSliceFlight::travelled)
	).apply(instance, FlameSliceFlight::new));

	public FlameSliceFlight {
		travelled = Double.isFinite(travelled) ? Math.max(0, travelled) : SpellDefinition.FLAME_SLICE.range();
	}

	public FlameSliceFlight advance(double distance) {
		return new FlameSliceFlight(travelled + Math.max(0, distance));
	}

	public double remaining() { return Math.max(0, SpellDefinition.FLAME_SLICE.range() - travelled); }
	public boolean exhausted() { return remaining() <= 1.0E-6; }

}
