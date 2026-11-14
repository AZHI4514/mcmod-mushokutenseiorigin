package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.tornado_impact.*;
class TornadoImpactFlightModuleTest {
	@Test void budgetAndMotionBoundariesStayStrict(){assertFalse(TornadoImpactFlight.expired(179,180,1E-8));assertTrue(TornadoImpactFlight.expired(180,180,9));assertTrue(TornadoImpactFlight.expired(0,180,Math.nextDown(1E-8)));assertFalse(TornadoImpactFlight.expired(0,180,Double.NaN));}
	@Test void candidatesMustPrecedeWallAndFluid(){assertFalse(TornadoImpactFlight.candidate(2,2,3));assertFalse(TornadoImpactFlight.candidate(2,3,2));assertTrue(TornadoImpactFlight.candidate(1,2,3));assertFalse(TornadoImpactFlight.candidate(Double.NaN,2,3));}
	@Test void restoredHitAndCrossTickBudgetRemainIndependent(){var victim=UUID.randomUUID();var history=new HashSet<>(Set.of(victim));assertTrue(history.contains(victim));var world=new TornadoImpactTerrainModuleTest.World();world.count=62;assertNull(TornadoImpactTerrain.firstObstacle(List.of(1),world));assertEquals(63,world.count);assertEquals(3,TornadoImpactTerrain.firstObstacle(List.of(2,3),world));assertEquals(64,world.count);var restored=new TornadoImpactTerrainModuleTest.World();restored.count=world.count;assertEquals(4,TornadoImpactTerrain.firstObstacle(List.of(4),restored));assertTrue(restored.attempts.isEmpty());assertTrue(new HashSet<>(history).contains(victim));}
	@Test void frozenCandidateArithmeticMatchesTenThousandInputs(){var r=new Random(38180);for(int i=0;i<10000;i++){double c=r.nextDouble()*8,w=r.nextDouble()*8,f=r.nextDouble()*8;assertEquals(c<Math.min(w,f),TornadoImpactFlight.candidate(c,w,f));}}
}
