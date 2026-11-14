package com.example.examplemod.magic.spell.earth.earthquake;
import com.example.examplemod.magic.mechanic.field.GroundFieldPolicy;
public final class EarthquakePolicy implements GroundFieldPolicy {
	public double radius(){return 16;}public double height(){return 4;}
	public int damagePulse(long age){return age==0?0:-1;}
	public boolean warning(long age){return false;}
}
