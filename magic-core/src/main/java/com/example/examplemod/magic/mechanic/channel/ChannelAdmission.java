package com.example.examplemod.magic.mechanic.channel;

/** Commit a begun channel before first emission; failed emission never undoes registration. */
public final class ChannelAdmission {
	public interface Entry {
		boolean begin();
		void register();
		void emit();
		void releaseSound();
	}
	public static boolean apply(Entry entry) {
		if(!entry.begin())return false;
		entry.register();
		entry.emit();
		entry.releaseSound();
		return true;
	}
	private ChannelAdmission() { }
}
