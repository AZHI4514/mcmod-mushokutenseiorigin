package com.example.examplemod.magic.spell.water.ocean_freeze;
import java.util.HashSet;
import java.util.UUID;
public final class OceanFreezeState {
	private final long start;private int scanned,blocks;private final HashSet<UUID> hit=new HashSet<>();
	public OceanFreezeState(long start,int scanned,int blocks){this.start=start;this.scanned=scanned;this.blocks=blocks;}
	public long start(){return start;}public int scanned(){return scanned;}public int blocks(){return blocks;}
	public void scanned(int value){scanned=value;}public void addBlocks(int count){blocks+=count;}
	public boolean hit(UUID id){return hit.contains(id);}public void claim(UUID id){hit.add(id);}
	public String savedHits(){return String.join(",",hit.stream().map(UUID::toString).toList());}
	public void restoreHits(String csv){for(var id:csv.split(",")){try{hit.add(UUID.fromString(id));}catch(IllegalArgumentException ignored){}}}
}
