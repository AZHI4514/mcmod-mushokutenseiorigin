package com.example.examplemod.magic.mechanic.field;
import java.util.*;
import java.util.function.*;
/** Server-owned live area index. Entity operations are supplied by the world adapter. */
public final class AreaRegistry<T> {
	private final Map<UUID,Map<String,T>> active=new HashMap<>();
	public Map<String,T> get(UUID owner){return active.get(owner);}
	public Map<String,T> entries(UUID owner){return active.computeIfAbsent(owner,id->new HashMap<>());}
	public Collection<Map<String,T>> values(){return active.values();}
	public void prune(Predicate<T> removed){active.values().forEach(map->map.values().removeIf(removed));active.values().removeIf(Map::isEmpty);}
	public void clear(UUID owner,Consumer<T> discard){var entries=active.remove(owner);if(entries!=null)entries.values().forEach(discard);}
	public void clearAll(Consumer<T> discard){active.values().forEach(map->map.values().forEach(discard));active.clear();}
}
