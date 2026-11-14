package com.example.examplemod.magic.spell.wind.sonic_blast;

import java.util.ArrayList;

/** Visibility is frozen before any destruction; rejected breaks do not stop the wave. */
public final class SonicBlastTerrain {
	public interface World<T> {
		boolean visible(T block);
		boolean breakBlock(T block);
	}
	public static <T> void apply(Iterable<T> orderedCandidates,World<T> world) {
		var visible=new ArrayList<T>();
		for (T block:orderedCandidates) if (world.visible(block)) visible.add(block);
		int changed=0;
		for (T block:visible) if (world.breakBlock(block)&&++changed>=128) break;
	}
	private SonicBlastTerrain() { }
}
