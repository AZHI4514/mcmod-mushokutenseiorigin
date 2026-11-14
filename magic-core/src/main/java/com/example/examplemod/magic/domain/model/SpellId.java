package com.example.examplemod.magic.domain.model;

import java.util.Objects;

/** Stable business ID; independent of registries and save codecs. */
public record SpellId(String value) {
	public SpellId {
		Objects.requireNonNull(value, "spell ID");
		if (!value.matches("[a-z][a-z0-9_]*")) throw new IllegalArgumentException("Invalid spell ID: " + value);
	}
}
