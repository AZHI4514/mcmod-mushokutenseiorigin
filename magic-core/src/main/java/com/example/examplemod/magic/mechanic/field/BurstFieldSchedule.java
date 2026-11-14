package com.example.examplemod.magic.mechanic.field;
public final class BurstFieldSchedule {
	public record Frame(boolean flash,boolean pulse) { public boolean active(){return flash||pulse;} }
	private final long start;private long expiry,next;private boolean flashed;
	public BurstFieldSchedule(long start,long expiry,long next,boolean flashed){this.start=start;this.expiry=expiry;this.next=next;this.flashed=flashed;}
	public long start(){return start;}public long expiry(){return expiry;}public long next(){return next;}public boolean flashed(){return flashed;}
	public Frame begin(long now){boolean flash=!flashed,pulse=now>=next;if(!flash&&!pulse)return new Frame(false,false);flashed=true;if(pulse)next=now+20;return new Frame(flash,pulse);}
	public void shorten(long now,int ticks){expiry=Math.max(now,expiry-ticks);}
}
