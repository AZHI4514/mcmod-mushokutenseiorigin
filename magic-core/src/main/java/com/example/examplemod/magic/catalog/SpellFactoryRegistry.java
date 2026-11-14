package com.example.examplemod.magic.catalog;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.*;

/** Explicit migrated-projectile assembly. The map is a registry, not itself Factory Method. */
public final class SpellFactoryRegistry {
	public record Binding(ProjectileSpec spec, SpellFactory factory) { }
	private final Map<SpellId, Binding> bindings;
	public SpellFactoryRegistry(Collection<Binding> entries) {
		var result = new LinkedHashMap<SpellId, Binding>();
		for (var binding : entries) {
			java.util.Objects.requireNonNull(binding.factory());
			if (result.putIfAbsent(binding.spec().id(), binding) != null) throw new IllegalArgumentException("Duplicate factory binding");
		}
		bindings = Map.copyOf(result);
	}
	public ProjectileBehavior create(SpellId id) {
		var binding = bindings.get(id);
		if (binding == null) throw new IllegalArgumentException("Missing spell factory: " + id.value());
		return binding.factory().create(binding.spec());
	}
}
