package com.example.examplemod.magic.spell.fire.flash_over;
import com.example.examplemod.magic.mechanic.field.BurstFieldSchedule;
/** Compatibility wrapper for the first migrated consumer. */
public final class FlashOverState {
	public record Frame(boolean flash,boolean pulse){public boolean active(){return flash||pulse;}}
	private final BurstFieldSchedule schedule;
	public FlashOverState(long start,long expiry,long next,boolean flashed){schedule=new BurstFieldSchedule(start,expiry,next,flashed);}
	public long start(){return schedule.start();}public long expiry(){return schedule.expiry();}public long next(){return schedule.next();}public boolean flashed(){return schedule.flashed();}
	public Frame begin(long now){var f=schedule.begin(now);return new Frame(f.flash(),f.pulse());}
	public void shorten(long now,int ticks){schedule.shorten(now,ticks);}
}
