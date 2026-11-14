package com.example.examplemod.magic.spell.earth.sand_cloud;
import com.example.examplemod.magic.mechanic.field.SmokeFieldPolicy;
public final class SandCloudPolicy implements SmokeFieldPolicy {
	public double radius(){return 4;}public double height(){return 4;}
	public Sight sight(){return new Sight(6,20,0xB99D74);}
	public Slowing slowing(){return null;}
	public int particleCount(){return 32;}
	public boolean launched(){return true;}
}
