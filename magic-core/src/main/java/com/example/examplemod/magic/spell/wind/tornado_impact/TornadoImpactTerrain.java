package com.example.examplemod.magic.spell.wind.tornado_impact;
/** Walks already ordered contacts; successful world mutations commit their count immediately. */
public final class TornadoImpactTerrain {
	public interface World<T> {
		int broken();
		boolean breakBlock(T contact);
		void incrementBroken();
	}
	public static <T> T firstObstacle(Iterable<T> ordered,World<T> world) {
		for (T contact : ordered) {
			if (world.broken()<TornadoImpactSpec.MAX_BROKEN_BLOCKS && world.breakBlock(contact)) {
				world.incrementBroken();
				continue;
			}
			return contact;
		}
		return null;
	}
	private TornadoImpactTerrain() { }
}
