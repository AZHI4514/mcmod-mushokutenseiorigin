package com.example.examplemod.magic.spell.earth.continental_sink;
import com.example.examplemod.magic.mechanic.field.GroundFieldPolicy;
public final class ContinentalSinkPolicy implements GroundFieldPolicy {
	public double radius(){return 48;}public double height(){return 4;}
	public int damagePulse(long age){return age==0?0:-1;}
	public boolean warning(long age){return false;}
	public boolean continuousContacts(){return true;}
}
