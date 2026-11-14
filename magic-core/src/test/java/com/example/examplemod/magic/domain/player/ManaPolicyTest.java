package com.example.examplemod.magic.domain.player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManaPolicyTest {
	@Test void boundsAndRestorationDoNotOverflow() {
		assertEquals(1, ManaPolicy.maximum(Integer.MIN_VALUE));
		assertEquals(1000000, ManaPolicy.maximum(Integer.MAX_VALUE));
		assertEquals(100, ManaPolicy.restore(80, 100, Integer.MAX_VALUE));
		assertEquals(80, ManaPolicy.restore(80, 100, -1));
		assertEquals(0, ManaPolicy.balance(-1, 100));
	}
}
