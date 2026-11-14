package com.example.examplemod.magic.spell.earth.earth_lance;
/** Preflight the entire spike before any world mutation; vanilla placement remains authoritative. */
public final class EarthLanceSpikes {
	public static final int HEIGHT=3,DURATION=200;
	public interface Placement {
		boolean available(int height);
		void begin();
		void place(int height,int duration);
		void finish();
	}
	public static boolean raise(Placement placement) {
		for(int y=0;y<HEIGHT;y++)if(!placement.available(y))return false;
		placement.begin();
		for(int y=0;y<HEIGHT;y++)placement.place(y,DURATION);
		placement.finish();return true;
	}
	private EarthLanceSpikes() { }
}
