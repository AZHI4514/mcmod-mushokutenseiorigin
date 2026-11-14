package com.example.examplemod.magic.spell.earth.dust_storm;
import com.example.examplemod.magic.mechanic.field.SmokeFieldPolicy;
public final class DustStormPolicy implements SmokeFieldPolicy {
	public double radius(){return 10;}public double height(){return 8;}
	public Sight sight(){return new Sight(12,40,0xB99D74);}
	public Slowing slowing(){return new Slowing(40,0,false);}
	public int particleCount(){return 32;}
	
}
