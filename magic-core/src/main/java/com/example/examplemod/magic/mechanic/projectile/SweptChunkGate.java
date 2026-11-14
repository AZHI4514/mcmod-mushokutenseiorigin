package com.example.examplemod.magic.mechanic.projectile;

/** Loaded-only swept bounds gate, preserving legacy x-major iteration. */
public final class SweptChunkGate {
	@FunctionalInterface
	public interface Loaded {boolean at(int blockX,int blockZ);}
	public static boolean available(double minX,double maxX,double minZ,double maxZ,Loaded loaded) {
		for(int x=(int)Math.floor(minX)>>4;x<=((int)Math.floor(maxX)>>4);x++)
			for(int z=(int)Math.floor(minZ)>>4;z<=((int)Math.floor(maxZ)>>4);z++)
				if(!loaded.at(x<<4,z<<4))return false;
		return true;
	}
	private SweptChunkGate() { }
}
