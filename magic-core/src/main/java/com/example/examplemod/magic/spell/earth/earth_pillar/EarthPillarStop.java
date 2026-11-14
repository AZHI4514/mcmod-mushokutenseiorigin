package com.example.examplemod.magic.spell.earth.earth_pillar;
/** Preserve committed world effects and original exception propagation. */
public final class EarthPillarStop {
	public interface World { void move(double distance); void show(); boolean hasOwner(); void stone(); void discard(); }
	public static void apply(double stop,World world) {
		world.move(EarthPillarFall.movement(stop));world.show();
		if(world.hasOwner())world.stone();
		world.discard();
	}
	private EarthPillarStop() { }
}
