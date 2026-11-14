package com.example.examplemod.magic.spell.barrier.physical_shield;
/** A failed replacement must leave the previous shield intact. */
public final class PhysicalShieldRelease {
	public interface World { boolean renewPrevious(); boolean spawn(); void discardPrevious(); void remember(); }
	public static boolean apply(World world){
		if(world.renewPrevious())return true;
		if(!world.spawn())return false;
		world.discardPrevious();world.remember();return true;
	}
	private PhysicalShieldRelease() { }
}
