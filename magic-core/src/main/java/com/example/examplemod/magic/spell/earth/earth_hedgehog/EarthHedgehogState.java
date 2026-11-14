package com.example.examplemod.magic.spell.earth.earth_hedgehog;
import java.util.*;
public final class EarthHedgehogState {
	private final long start;private long processed;private final Map<String,Integer> hits=new HashMap<>();
	public EarthHedgehogState(long start,long processed,Map<String,Integer> hits){this.start=start;this.processed=processed;this.hits.putAll(hits);}
	public long start(){return start;}public long processed(){return processed;}public Map<String,Integer> hits(){return hits;}
	public boolean begin(long age){if(age<=processed)return false;processed=age;return true;}
	public void claim(String key,int previous){hits.put(key,previous+1);}
}
