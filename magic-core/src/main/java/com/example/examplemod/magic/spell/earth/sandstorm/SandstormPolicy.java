package com.example.examplemod.magic.spell.earth.sandstorm;
import com.example.examplemod.magic.mechanic.field.SmokeFieldPolicy;
public final class SandstormPolicy implements SmokeFieldPolicy {
	public double radius(){return 20;}public double height(){return 16;}
	public Sight sight(){return new Sight(8,40,0xB99D74);}
	public Slowing slowing(){return new Slowing(40,1,false);}
	public int particleCount(){return 56;}
	public boolean damaging(){return true;}
}
