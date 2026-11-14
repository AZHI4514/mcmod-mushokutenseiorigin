package com.example.examplemod.magic.domain.runtime;

/** Instance-owned operation quota; failed world attempts still consume their reserved work. */
public final class TickQuota {
	private final int limit;
	private long tick = Long.MIN_VALUE;
	private int used;
	public TickQuota(int limit) { if (limit < 0) throw new IllegalArgumentException("limit"); this.limit = limit; }
	public int available(long now) { if (tick != now) { tick = now; used = 0; } return limit - used; }
	public void consume(int amount) { if (amount < 0) throw new IllegalArgumentException("amount"); used += amount; }
	public boolean reserve(long now, int amount) { if (amount < 0 || amount > available(now)) return false; consume(amount); return true; }
}
