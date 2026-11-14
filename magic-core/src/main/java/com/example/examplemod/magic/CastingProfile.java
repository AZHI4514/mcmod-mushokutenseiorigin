package com.example.examplemod.magic;

/** 所有术阶共用的施法时间；未解锁模式不能由客户端选择。 */
public record CastingProfile(int preparationTicks, int recoveryTicks, boolean requiresCircle) {
	public enum Mode { FULL, SHORTENED, SILENT }
	public static CastingProfile of(int rank, Mode mode) {
		var timing = com.example.examplemod.magic.domain.spec.CastingTiming.of(rank,
				com.example.examplemod.magic.domain.spec.CastingTiming.Mode.valueOf(mode.name()));
		return new CastingProfile(timing.preparationTicks(), timing.recoveryTicks(), timing.requiresCircle());
	}
	public static boolean unlocked(Mode mode) { return mode == Mode.FULL; }
}
