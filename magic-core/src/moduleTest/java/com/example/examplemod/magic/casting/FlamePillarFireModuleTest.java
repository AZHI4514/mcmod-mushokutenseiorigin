package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarFirePlacement;
class FlamePillarFireModuleTest {
	private static final class World implements FlamePillarFirePlacement.World<Integer> {
		final List<String> events=new ArrayList<>();Set<Integer> denied=Set.of(),fire=Set.of(),solid=Set.of(),failed=Set.of();
		public boolean mayChange(Integer cell){events.add("allow"+cell);return !denied.contains(cell);}
		public boolean existingFire(Integer cell){events.add("fire"+cell);return fire.contains(cell);}
		public boolean air(Integer cell){events.add("air"+cell);return !solid.contains(cell);}
		public boolean placeSurvivingFire(Integer cell){events.add("place"+cell);return !failed.contains(cell);}
	}
	@Test void deniedFaceFallsBackToTopWithoutReadingOrWritingDeniedCell(){var w=new World();w.denied=Set.of(1);assertTrue(FlamePillarFirePlacement.apply(List.of(1,2),w));assertEquals(List.of("allow1","allow2","fire2","air2","place2"),w.events);}
	@Test void existingFireSucceedsWithoutRewriteOrFallback(){var w=new World();w.fire=Set.of(1);assertTrue(FlamePillarFirePlacement.apply(List.of(1,2),w));assertEquals(List.of("allow1","fire1"),w.events);}
	@Test void failedFirstPlacementTriesTopAndAllFailuresReturnFalse(){var w=new World();w.failed=Set.of(1,2);assertFalse(FlamePillarFirePlacement.apply(List.of(1,2),w));assertEquals(List.of("allow1","fire1","air1","place1","allow2","fire2","air2","place2"),w.events);}
	@Test void nonAirCellsAreNeverOverwrittenAndDuplicateTopCandidateIsNotDeduplicated(){var w=new World();w.solid=Set.of(1);assertFalse(FlamePillarFirePlacement.apply(List.of(1,1),w));assertEquals(List.of("allow1","fire1","air1","allow1","fire1","air1"),w.events);}
}
