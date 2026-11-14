package com.example.examplemod.magic.mechanic.projectile;
/** A WATERLOGGED property is a capability even when its current value is false. */
public final class WaterPassage {
	public static boolean passes(boolean penetrating, boolean hasWaterloggedProperty, boolean waterFluid) {
		return penetrating && (hasWaterloggedProperty || waterFluid);
	}
	private WaterPassage() { }
}
