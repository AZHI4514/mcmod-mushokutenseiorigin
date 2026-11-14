package com.example.examplemod.magic.spell.special.communication_magic;
public final class CommunicationRelease {
	public interface World {boolean ready();void takeDraft();void sendRecipient();void sendSender();void visual();}
	public static boolean cast(World w){if(!w.ready())return false;w.takeDraft();w.sendRecipient();w.sendSender();w.visual();return true;}
	private CommunicationRelease() { }
}
