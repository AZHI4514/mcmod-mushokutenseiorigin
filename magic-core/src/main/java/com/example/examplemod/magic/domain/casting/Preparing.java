package com.example.examplemod.magic.domain.casting;

/** Immutable concrete State, no spell ID dispatch or game state. */
public record Preparing(int remaining, int heartbeat) implements CastPhase {
	public Preparing { if (remaining < 1 || heartbeat < 0) throw new IllegalArgumentException(); }
	public CastPhase onHeartbeat() { return new Preparing(remaining, 0); }
	public CastingSession.Step onTick(boolean available) {
		if (!available || heartbeat + 1 > 10) return onCancel();
		if (remaining == 1) return new CastingSession.Step(new Idle(), 0, CastingSession.Signal.READY);
		return new CastingSession.Step(new Preparing(remaining - 1, heartbeat + 1), remaining - 1, CastingSession.Signal.NONE);
	}
	public CastingSession.Step onCancel() { return new CastingSession.Step(new Idle(), 0, CastingSession.Signal.CANCELLED); }
}
