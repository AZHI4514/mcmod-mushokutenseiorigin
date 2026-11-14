package com.example.examplemod.magic.spell.special.communication_magic;
import java.util.*;
/** One instance per server; the signed message is opaque to the pure core. */
public final class CommunicationDrafts<M> {
	public record Draft<M>(UUID recipient,M message,long expires) { }
	private final Map<UUID,Draft<M>> drafts=new HashMap<>();
	public Draft<M> get(UUID sender){return drafts.get(sender);}public void put(UUID sender,Draft<M> draft){drafts.put(sender,draft);}public Draft<M> remove(UUID sender){return drafts.remove(sender);}
}
