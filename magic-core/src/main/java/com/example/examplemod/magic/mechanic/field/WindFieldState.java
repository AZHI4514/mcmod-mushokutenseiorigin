package com.example.examplemod.magic.mechanic.field;
public final class WindFieldState {
	private final long start,expiry;private long next;
	public WindFieldState(long start,long expiry,long next){this.start=start;this.expiry=expiry;this.next=next;}
	public long start(){return start;}public long expiry(){return expiry;}public long next(){return next;}
	public boolean pulse(long now){boolean pulse=now>=next;if(pulse)next=now+20;return pulse;}
}
