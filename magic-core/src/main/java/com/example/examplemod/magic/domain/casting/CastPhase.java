package com.example.examplemod.magic.domain.casting;

/** State contract: each phase owns its tick/heartbeat/cancel transition; cancellation never releases. */
public interface CastPhase {
	CastPhase onHeartbeat();
	CastingSession.Step onTick(boolean available);
	CastingSession.Step onCancel();
}
