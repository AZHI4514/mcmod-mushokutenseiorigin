package com.example.examplemod.magic.spell.mixed.mud_rain;
import com.example.examplemod.magic.mechanic.field.SmokeFieldPolicy;
public final class MudRainPolicy implements SmokeFieldPolicy {
	public double radius(){return 14;}public double height(){return 24;}
	public Sight sight(){return null;}
	public Slowing slowing(){return new Slowing(60,1,true);}
	public int particleCount(){return 44;}
	public boolean requiresSky(){return true;}
}
