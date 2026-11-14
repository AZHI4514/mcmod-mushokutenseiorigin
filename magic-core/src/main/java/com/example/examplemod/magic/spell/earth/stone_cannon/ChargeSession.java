package com.example.examplemod.magic.spell.earth.stone_cannon;
/** Pure elapsed/heartbeat context. World eligibility and reserved mana remain explicit adapter inputs. */
public final class ChargeSession {
	private int ticks,heartbeat;
	public void heartbeat() { heartbeat=0; }
	public boolean expired(boolean eligible) { return !eligible || ++heartbeat>10; }
	public ChargeStep advance(int mana,int reserved) {
		var step=ChargeStep.advance(ticks,mana,reserved);ticks=step.ticks();return step;
	}
}
