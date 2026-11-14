package com.example.examplemod.magic.domain.spec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CastingTimingTest {
	@Test void fullChantKeepsAllSevenRankBoundaries() {
		int[] prepare={100,160,240,400,600,900,1800}, recovery={0,10,20,60,100,160,600};
		for(int rank=1;rank<=7;rank++) {
			var timing=CastingTiming.of(rank,CastingTiming.Mode.FULL);
			assertEquals(prepare[rank-1],timing.preparationTicks());
			assertEquals(recovery[rank-1],timing.recoveryTicks());
			assertEquals(rank==7,timing.requiresCircle());
		}
		assertFalse(CastingTiming.unlocked(CastingTiming.Mode.SILENT));
		assertFalse(CastingTiming.unlocked(CastingTiming.Mode.SHORTENED));
		assertThrows(IllegalArgumentException.class,()->CastingTiming.of(7,CastingTiming.Mode.SILENT));
	}
}
