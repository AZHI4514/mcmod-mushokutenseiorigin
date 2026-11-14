package com.example.examplemod.magic.mechanic.channel;
/** Independent channel heartbeat and generation cadence, with no world or mana ownership. */
public final class ChannelSession {
	private int heartbeat, generationTicks;
	public void heartbeat() { heartbeat=0; }
	public boolean expired(boolean eligible) { return !eligible || ++heartbeat>6; }
	/** Advance only after a successful payment. First generation happens separately on entry. */
	public boolean generationDue() {
		if(++generationTicks==20) { generationTicks=0;return true; }
		return false;
	}
}
