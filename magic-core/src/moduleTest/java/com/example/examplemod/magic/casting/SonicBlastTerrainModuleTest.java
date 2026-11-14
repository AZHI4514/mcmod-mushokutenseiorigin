package com.example.examplemod.magic.casting;
import java.util.*;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.sonic_blast.SonicBlastTerrain;
class SonicBlastTerrainModuleTest {
	static class World implements SonicBlastTerrain.World<Integer> {
		final List<String> events=new ArrayList<>();final Set<Integer> hidden=new HashSet<>(),rejected=new HashSet<>();final List<Integer> broken=new ArrayList<>();
		public boolean visible(Integer b){events.add("v"+b);return !hidden.contains(b);}
		public boolean breakBlock(Integer b){events.add("b"+b);if(rejected.contains(b))return false;broken.add(b);return true;}
	}
	@Test void snapshotsVisibilityBeforeMutationAndRetainsInputOrder(){var w=new World(){public boolean breakBlock(Integer b){hidden.add(2);return super.breakBlock(b);}};SonicBlastTerrain.apply(List.of(3,1,2),w);assertEquals(List.of("v3","v1","v2","b3","b1","b2"),w.events);assertEquals(List.of(3,1,2),w.broken);}
	@Test void failedBreaksContinueAndOnlySuccessfulBreaksCountToward128(){var w=new World();w.rejected.add(0);SonicBlastTerrain.apply(IntStream.range(0,140).boxed().toList(),w);assertEquals(128,w.broken.size());assertEquals(128,w.broken.getLast());assertEquals("b128",w.events.getLast());assertEquals(140,w.events.stream().filter(x->x.startsWith("v")).count());}
	@Test void invisibleBlocksNeverReachPermissionAndBreakAdapter(){var w=new World();w.hidden.add(2);SonicBlastTerrain.apply(List.of(1,2,3),w);assertEquals(List.of(1,3),w.broken);assertFalse(w.events.contains("b2"));}
	@Test void visibilityExceptionPreventsAllDestruction(){var w=new World(){public boolean visible(Integer b){if(b==2)throw new IllegalStateException();return super.visible(b);}};assertThrows(IllegalStateException.class,()->SonicBlastTerrain.apply(List.of(1,2,3),w));assertTrue(w.broken.isEmpty());}
	@Test void breakExceptionPreservesEarlierDestructionAndStops(){var w=new World(){public boolean breakBlock(Integer b){if(b==2)throw new IllegalStateException();return super.breakBlock(b);}};assertThrows(IllegalStateException.class,()->SonicBlastTerrain.apply(List.of(1,2,3),w));assertEquals(List.of(1),w.broken);assertFalse(w.events.contains("b3"));}
	@Test void emptyCandidatesPerformNoWorldCalls(){var w=new World();SonicBlastTerrain.apply(List.of(),w);assertTrue(w.events.isEmpty());}
}
