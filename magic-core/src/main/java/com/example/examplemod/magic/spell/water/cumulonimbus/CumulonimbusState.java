package com.example.examplemod.magic.spell.water.cumulonimbus;
import java.util.Map;
import java.util.HashMap;
/** One live storm's schedule and victim attempts, independent of Minecraft. */
public final class CumulonimbusState {
	private long expiry,next;private final Map<String,Long> victims=new HashMap<>();
	public CumulonimbusState(long expiry,long next,Map<String,Long> victims){this.expiry=expiry;this.next=next;this.victims.putAll(victims);}
	public long expiry(){return expiry;}public long next(){return next;}public Map<String,Long> victims(){return victims;}
	public void shorten(long now,int ticks){expiry=CumulonimbusRules.shorten(now,expiry,ticks);}
	public void beginStrike(long now){next=now+40;victims.values().removeIf(t->t<=now);}
	public void claim(String id,long now){victims.put(id,now+80);}
}
