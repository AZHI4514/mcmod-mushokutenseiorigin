package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactTerrain;
class TornadoImpactTerrainModuleTest {
	static class World implements TornadoImpactTerrain.World<Integer> {
		int count;final List<Integer> attempts=new ArrayList<>();int denied=-1;
		public int broken(){return count;}
		public boolean breakBlock(Integer contact){attempts.add(contact);return contact!=denied;}
		public void incrementBroken(){count++;}
	}
	@Test void capStopsBeforeAttemptingSixtyFifthBlock(){var w=new World();w.count=63;assertEquals(2,TornadoImpactTerrain.firstObstacle(List.of(1,2,3),w));assertEquals(64,w.count);assertEquals(List.of(1),w.attempts);}
	@Test void failedBreakIsFirstObstacleAndDoesNotConsumeBudget(){var w=new World();w.denied=2;assertEquals(2,TornadoImpactTerrain.firstObstacle(List.of(1,2,3),w));assertEquals(1,w.count);assertEquals(List.of(1,2),w.attempts);}
	@Test void fullyBrokenPathHasNoObstacle(){var w=new World();assertNull(TornadoImpactTerrain.firstObstacle(List.of(4,2,3),w));assertEquals(List.of(4,2,3),w.attempts);assertEquals(3,w.count);}
	@Test void breakExceptionKeepsEarlierCommittedCount(){var w=new World(){public boolean breakBlock(Integer c){if(c==2)throw new IllegalStateException();return super.breakBlock(c);}};assertThrows(IllegalStateException.class,()->TornadoImpactTerrain.firstObstacle(List.of(1,2,3),w));assertEquals(1,w.count);assertEquals(List.of(1),w.attempts);}
	@Test void restoredCapNeverCallsWorldBreak(){var w=new World();w.count=64;assertEquals(7,TornadoImpactTerrain.firstObstacle(List.of(7),w));assertTrue(w.attempts.isEmpty());assertEquals(64,w.count);}
	@Test void emptyPathDoesNotReadOrMutateBudget(){var w=new World(){public int broken(){fail();return 0;}};assertNull(TornadoImpactTerrain.firstObstacle(List.of(),w));}
}
