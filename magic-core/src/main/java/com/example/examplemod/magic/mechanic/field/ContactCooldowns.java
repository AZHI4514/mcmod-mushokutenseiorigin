package com.example.examplemod.magic.mechanic.field;
import java.util.*;
/** Runtime-owned contact windows, keyed by source identity then victim identity. */
public final class ContactCooldowns {
	private final int ticks;
	private final Map<UUID,Map<UUID,Long>> contacts=new HashMap<>();
	public ContactCooldowns(int ticks) { if(ticks<=0)throw new IllegalArgumentException("Nonpositive contact interval");this.ticks=ticks; }
	public boolean claim(UUID source,UUID target,long now) {
		var victims=contacts.computeIfAbsent(source,id->new HashMap<>());
		victims.values().removeIf(time->now-time>=ticks);
		if(victims.containsKey(target))return false;
		victims.put(target,now);return true;
	}
	public void prune(long now) {
		contacts.values().forEach(victims->victims.values().removeIf(time->now-time>=ticks));
		contacts.values().removeIf(Map::isEmpty);
	}
	public void clear(UUID source) { contacts.remove(source); }
}
