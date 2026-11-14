package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.flood_flush.FloodFlushSpec;
class FloodFlushRulesTest {
	@Test void wetThresholdPreservesSampleBoundaryAndDuration() {
		assertEquals(200,FloodFlushSpec.wetTicks(Math.nextDown(.6F)));assertEquals(0,FloodFlushSpec.wetTicks(.6F));assertEquals(0,FloodFlushSpec.wetTicks(Float.NaN));
		assertEquals(1.2,FloodFlushSpec.KNOCKBACK);
	}
}
