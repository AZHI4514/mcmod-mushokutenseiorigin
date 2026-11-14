package com.example.examplemod.magic.spell.fire.heat_hand;
public final class HeatHandMelt {
	public interface World<T> {
		Iterable<T> targets();boolean melt(T target);boolean hasWarmth();int frozenTicks();void warm();void particles(T target);
	}
	public static <T> boolean apply(World<T> world){
		var targets=world.targets();boolean changed=false;
		for(T target:targets)changed|=world.melt(target);
		boolean warmth=world.hasWarmth();
		if(changed||world.frozenTicks()>0||!warmth){world.warm();changed=true;}
		if(changed)for(T target:targets)world.particles(target);
		return changed;
	}
	private HeatHandMelt() { }
}
