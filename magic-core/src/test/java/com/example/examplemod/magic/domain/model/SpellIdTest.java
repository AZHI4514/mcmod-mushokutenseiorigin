package com.example.examplemod.magic.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpellIdTest {
	@Test void preservesCompatibilityIdWithoutGameClasses() {
		assertEquals("phase_two_spell", new SpellId("phase_two_spell").value());
		assertEquals(new SpellId("water_ball"), new SpellId("water_ball"));
	}
	@Test void rejectsInvalidBusinessIds() {
		for (var id : new String[]{"", "WaterBall", "../water_ball", "magic:water_ball", "a b"})
			assertThrows(IllegalArgumentException.class, () -> new SpellId(id));
		assertThrows(NullPointerException.class, () -> new SpellId(null));
	}
}
