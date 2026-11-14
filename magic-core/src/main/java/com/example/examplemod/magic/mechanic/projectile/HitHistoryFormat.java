package com.example.examplemod.magic.mechanic.projectile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/** String representation only; the Minecraft Codec remains in the adapter. */
public final class HitHistoryFormat {
	public static List<String> save(Set<UUID> hits) {
		return hits.stream().map(UUID::toString).toList();
	}
	public static void restore(Set<UUID> hits,Supplier<? extends Iterable<String>> saved) {
		hits.clear();
		for(String id:saved.get()) {
			try {hits.add(UUID.fromString(id));} catch(IllegalArgumentException ignored) { }
		}
	}
	private HitHistoryFormat() { }
}
