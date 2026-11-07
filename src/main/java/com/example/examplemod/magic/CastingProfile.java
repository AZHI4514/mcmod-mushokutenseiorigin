package com.example.examplemod.magic;

/** 所有术阶共用的施法时间；未解锁模式不能由客户端选择。 */
public record CastingProfile(int preparationTicks, int recoveryTicks, boolean requiresCircle) {
	public enum Mode { FULL, SHORTENED, SILENT }
	private static final int[][] PREPARE = {{100,160,240,400,600,900,1800},{40,60,100,160,240,360},{5,6,8,10,14,20}};
	private static final int[][] RECOVER = {{0,10,20,60,100,160,600},{4,10,20,40,80,120},{10,20,40,100,200,400}};
	public static CastingProfile of(int rank, Mode mode) {
		if (rank < 1 || rank > 7 || (rank == 7 && mode != Mode.FULL)) throw new IllegalArgumentException("Unavailable casting profile");
		return new CastingProfile(PREPARE[mode.ordinal()][rank - 1], RECOVER[mode.ordinal()][rank - 1], rank == 7);
	}
	public static boolean unlocked(Mode mode) { return mode == Mode.FULL; }
}
