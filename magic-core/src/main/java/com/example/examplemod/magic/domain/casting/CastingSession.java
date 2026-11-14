package com.example.examplemod.magic.domain.casting;

/** State context; callers cannot drive a second release from Idle. */
public final class CastingSession {
	public enum Signal { NONE, READY, CANCELLED }
	public record Step(CastPhase next, int remaining, Signal signal) { }
	private CastPhase phase;
	public CastingSession(int preparationTicks) { phase = new Preparing(preparationTicks, 0); }
	private CastingSession(CastPhase phase) { this.phase=phase; }
	public static CastingSession channeling() { return new CastingSession(new Channeling(0)); }
	public void heartbeat() {
		phase = phase.onHeartbeat();
	}
	public Step tick(boolean available) { var result = phase.onTick(available); phase = result.next(); return result; }
	public Step cancel() { var result = phase.onCancel(); phase = result.next(); return result; }
}
