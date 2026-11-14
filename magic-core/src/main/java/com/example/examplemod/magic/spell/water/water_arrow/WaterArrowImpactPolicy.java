package com.example.examplemod.magic.spell.water.water_arrow;
/** Evaluated after a successful eligible hit; consumes no random source itself. */
public final class WaterArrowImpactPolicy {
	public static int statusTicks(float roll) {
		return roll < WaterArrowSpec.STATUS_CHANCE ? WaterArrowSpec.STATUS_TICKS : 0;
	}
	private WaterArrowImpactPolicy() { }
}
