package com.example.examplemod.magic.mechanic.targeting;
public final class SupportConsent {
	public interface Recipient {boolean self();boolean player();boolean trusted();boolean pet();boolean owned();boolean allied();}
	public static boolean allows(Recipient r){return r.self()||r.player()&&r.trusted()||r.pet()&&r.owned()||!r.player()&&r.allied();}
	private SupportConsent() { }
}
