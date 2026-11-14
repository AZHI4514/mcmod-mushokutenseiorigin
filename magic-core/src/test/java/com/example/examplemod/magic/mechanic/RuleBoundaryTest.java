package com.example.examplemod.magic.mechanic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.combat.EffectRules;
import com.example.examplemod.magic.mechanic.channel.ChargeCurve;
import com.example.examplemod.magic.mechanic.field.FieldSchedule;

class RuleBoundaryTest {
	@Test void fieldReleaseAndExpiryAreDifferentBoundaries() {
		int pulses = 0;
		for (int age = -1; age <= 80; age++) if (FieldSchedule.pulse(age, 80, 10)) pulses++;
		assertEquals(8, pulses);
		assertTrue(FieldSchedule.pulse(0, 80, 10));
		assertFalse(FieldSchedule.pulse(80, 80, 10));
		assertFalse(FieldSchedule.pulse(0, 80, 0));
		assertTrue(FieldSchedule.strikeReady(40, 40, 1200));
		assertFalse(FieldSchedule.strikeReady(1200, 1200, 1200));
		assertTrue(FieldSchedule.cadenceReady(100, 90, 10));
		assertFalse(FieldSchedule.cadenceReady(99, 90, 10));
	}
	@Test void chargeIntegralMonotonicallyAccumulatesWholeMana() {
		long last = 0;
		for (int tick = 0; tick < 24000; tick++) {
			long current = ChargeCurve.extraAfterTicks(tick);
			assertTrue(current >= last);
			last = current;
		}
		assertEquals(0, ChargeCurve.extraAfterTicks(-1));
		assertEquals(15, ChargeCurve.extraAfterTicks(20));
		assertEquals(11, ChargeCurve.stoneDamage(20));
	}
	@Test void durationsClampAndRenewalSaturates() {
		assertEquals(60, EffectRules.flightTicks(-1));
		assertEquals(420, EffectRules.flightTicks(8));
		assertEquals(700, EffectRules.statusTicks(7));
		assertEquals(Integer.MAX_VALUE, EffectRules.extend(Integer.MAX_VALUE - 1, 80));
		assertEquals(80, EffectRules.extend(-10, 80));
		assertEquals(0, EffectRules.shieldAfter(8, 10));
		assertEquals(8, EffectRules.shieldAfter(8, -1));
	}
}
