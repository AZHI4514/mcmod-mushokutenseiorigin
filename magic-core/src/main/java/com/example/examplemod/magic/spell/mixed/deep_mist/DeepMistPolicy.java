package com.example.examplemod.magic.spell.mixed.deep_mist;
import com.example.examplemod.magic.mechanic.field.SmokeFieldPolicy;
public final class DeepMistPolicy implements SmokeFieldPolicy {
	public double radius(){return 12;}public double height(){return 8;}
	public Sight sight(){return new Sight(6,10,0xB7C9D1);}
	public Slowing slowing(){return null;}
	public int particleCount(){return 32;}
	
}
