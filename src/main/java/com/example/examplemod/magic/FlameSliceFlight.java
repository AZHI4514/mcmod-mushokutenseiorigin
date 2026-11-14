package com.example.examplemod.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** 火断存档兼容记录；穿透生物，完整截面遮挡才停止。旧targets字段忽略。 */
public record FlameSliceFlight(double travelled) {
	public static final Codec<FlameSliceFlight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("travelled").forGetter(FlameSliceFlight::travelled)
	).apply(instance, FlameSliceFlight::new));

	public FlameSliceFlight {
		travelled = new com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceFlightState(travelled).travelled();
	}

	public FlameSliceFlight advance(double distance) {
		return new FlameSliceFlight(core().advance(distance).travelled());
	}

	public com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceFlightState core() { return new com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceFlightState(travelled); }
	public double remaining() { return core().remaining(); }
	public boolean exhausted() { return core().exhausted(); }

}
