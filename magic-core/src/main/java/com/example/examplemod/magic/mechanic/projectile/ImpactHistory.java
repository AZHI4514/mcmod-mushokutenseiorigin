package com.example.examplemod.magic.mechanic.projectile;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
/** Per-projectile identity history. Claim precedes damage, including an immune/blocked attempt. */
public final class ImpactHistory {
	private final Set<UUID> victims;
	public ImpactHistory() { this(Set.of()); }
	public ImpactHistory(Set<UUID> restored) { victims = new HashSet<>(restored); }
	public boolean contains(UUID id) { return victims.contains(id); }
	public boolean claim(UUID id) { return victims.add(java.util.Objects.requireNonNull(id)); }
	public Set<UUID> snapshot() { return Set.copyOf(victims); }
}
