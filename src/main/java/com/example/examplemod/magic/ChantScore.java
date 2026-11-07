package com.example.examplemod.magic;

/** 服务端逐字母校验：无限时，错误输入不推进且不清空进度。 */
public final class ChantScore {
	private final int[] notes;
	private int index;
	public ChantScore(int[] notes) {
		if (notes.length < 4 || notes.length > 12) throw new IllegalArgumentException("Invalid score length");
		this.notes = notes.clone();
		for (int note : notes) if (note < 0 || note > 25) throw new IllegalArgumentException("Invalid letter");
	}
	public static int noteCount(int rank, int seed) {
		if (rank < 1 || rank > 4) throw new IllegalArgumentException("Unsupported learning rank");
		return 2 + rank * 2 + Math.floorMod(seed, 3);
	}
	public void press(int key) {
		if (index < notes.length && key == notes[index]) index++;
	}
	public int index() { return index; }
	public int hits() { return index; }
	public boolean finished() { return index == notes.length; }
	public boolean passed() { return finished(); }
}
