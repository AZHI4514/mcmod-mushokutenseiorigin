package com.example.examplemod.magic.spell.barrier.physical_shield;
import java.util.*;
/** One runtime instance per server, never a process-wide registry. */
public final class PhysicalShieldRegistry<T> {
	private final Map<UUID,T> shields=new HashMap<>();
	public T get(UUID owner){return shields.get(owner);}
	public void put(UUID owner,T shield){shields.put(owner,shield);}
	public T remove(UUID owner){return shields.remove(owner);}
	public Collection<T> values(){return shields.values();}
}
