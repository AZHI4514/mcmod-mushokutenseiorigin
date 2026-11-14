package com.example.examplemod.magic.mechanic.projectile;
/** Coordinates an already selected shield contact, shared by water and physical shields. */
public final class ShieldInterception {
	public interface Contact {boolean terrainClear();float incomingDamage();void absorb(float damage);boolean trident();void rebound();void discard();}
	public static boolean apply(Contact contact){
		if(!contact.terrainClear())return false;
		contact.absorb(contact.incomingDamage());
		if(contact.trident())contact.rebound();else contact.discard();
		return true;
	}
	private ShieldInterception() { }
}
