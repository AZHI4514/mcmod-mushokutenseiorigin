package com.example.examplemod.magic.feature.input;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** One server owns one instance; no global player state. */
public final class InputSessions<D> {
	private final Map<UUID, Session<D>> sessions = new HashMap<>();
	public Session<D> get(UUID player) { return sessions.computeIfAbsent(player, id -> new Session<>()); }
	public void reset(UUID player) { sessions.put(player, new Session<>()); }
	public void remove(UUID player) { sessions.remove(player); }
	public void clear() { sessions.clear(); }
	public static final class Session<D> {
		public D dimension;
		public int age;
		public boolean blocked = true;
		private int lastSequence = -1;
		private int requestTick = -1;
		private int requests;
		/** Sequence commits even when the per-tick allowance is exceeded. */
		public boolean accept(int sequence) {
			if (sequence <= lastSequence) return false;
			lastSequence = sequence;
			if (requestTick != age) { requestTick = age; requests = 0; }
			return ++requests <= 8;
		}
	}
}
