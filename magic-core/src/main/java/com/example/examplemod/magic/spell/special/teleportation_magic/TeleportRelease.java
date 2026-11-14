package com.example.examplemod.magic.spell.special.teleportation_magic;
public final class TeleportRelease {
	public interface World {int travelerCount();boolean move(int index);void rollback(int index);void payExtra();void consumeInvitations();void visual();}
	public static boolean cast(World w){for(int i=0;i<w.travelerCount();i++){if(!w.move(i)){for(int j=i-1;j>=0;j--)w.rollback(j);return false;}}w.payExtra();w.consumeInvitations();w.visual();return true;}
	private TeleportRelease() { }
}
