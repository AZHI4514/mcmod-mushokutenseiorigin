package com.example.examplemod.magic.domain.casting;

public final class Idle implements CastPhase {
	public CastPhase onHeartbeat() { return this; }
	public CastingSession.Step onTick(boolean available) { return new CastingSession.Step(this, 0, CastingSession.Signal.NONE); }
	public CastingSession.Step onCancel() { return new CastingSession.Step(this, 0, CastingSession.Signal.NONE); }
}
