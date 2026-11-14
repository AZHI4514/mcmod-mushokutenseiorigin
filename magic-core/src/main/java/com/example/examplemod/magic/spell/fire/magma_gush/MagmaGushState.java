package com.example.examplemod.magic.spell.fire.magma_gush;
public final class MagmaGushState {
	private final long start;private int processed;
	public MagmaGushState(long start,int processed){this.start=start;this.processed=processed;}
	public long start(){return start;}public int processed(){return processed;}
	public boolean begin(long age){if(age<=processed)return false;processed=(int)age;return true;}
}
