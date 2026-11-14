package com.example.examplemod.magic.domain.casting;
/** Ongoing-channel State; an absent seventh heartbeat cancels and never emits preparation READY. */
public record Channeling(int heartbeat) implements CastPhase {
	public Channeling { if(heartbeat<0)throw new IllegalArgumentException("Negative heartbeat"); }
	@Override public CastPhase onHeartbeat() { return new Channeling(0); }
	@Override public CastingSession.Step onTick(boolean available) {
		if(!available||heartbeat>=6)return onCancel();
		return new CastingSession.Step(new Channeling(heartbeat+1),0,CastingSession.Signal.NONE);
	}
	@Override public CastingSession.Step onCancel() {
		return new CastingSession.Step(new Idle(),0,CastingSession.Signal.CANCELLED);
	}
}
