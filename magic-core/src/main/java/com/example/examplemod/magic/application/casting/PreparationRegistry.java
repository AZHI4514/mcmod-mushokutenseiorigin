package com.example.examplemod.magic.application.casting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** One owner creates one registry; no process-wide session state. */
public final class PreparationRegistry<T> {
	private final Map<UUID,T> active=new HashMap<>();
	public T get(UUID player) {return active.get(player);}
	public boolean contains(UUID player) {return active.containsKey(player);}
	public void put(UUID player,T pending) {active.put(player,pending);}
	public T remove(UUID player) {return active.remove(player);}
	public void clear() {active.clear();}
}
