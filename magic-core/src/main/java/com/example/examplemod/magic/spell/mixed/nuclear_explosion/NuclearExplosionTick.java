package com.example.examplemod.magic.spell.mixed.nuclear_explosion;
public final class NuclearExplosionTick {
	public interface World {long age();boolean exploded();void commitExplosion();void targets();void terrain();boolean visualDue();void visual();}
	public static void tick(World w){if(NuclearExplosionRules.detonationDue(w.age(),w.exploded())){w.commitExplosion();w.targets();w.terrain();}if(w.visualDue())w.visual();}
	private NuclearExplosionTick() { }
}
