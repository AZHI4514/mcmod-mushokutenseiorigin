package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.CarrierTermination;
class PhaseTwoTerminationModuleTest {
	static class World implements CarrierTermination.World {
		final List<String> events=new ArrayList<>();
		public void discard(){events.add("discard");}
		public void explosionPosition(double d){events.add("position"+d);}
		public void explode(){events.add("explode");}
		public void wallBurst(double d){events.add("burst"+d);}
	}
	@Test void fluidWinsEqualWall(){for(boolean explosion:List.of(false,true)){var w=new World();assertTrue(CarrierTermination.apply(explosion,2,1,1,true,w));assertEquals(List.of("discard"),w.events);}}
	@Test void ordinaryWallPresentsThenDiscards(){var w=new World();assertTrue(CarrierTermination.apply(false,2,1,3,true,w));assertEquals(List.of("burst1.0","discard"),w.events);}
	@Test void explosionWallOffsetsThenExplodesWithoutExtraDiscard(){var w=new World();assertTrue(CarrierTermination.apply(true,2,1,3,true,w));assertEquals(List.of("position0.98","explode"),w.events);}
	@Test void explosionOffsetNeverNegative(){var w=new World();assertTrue(CarrierTermination.apply(true,2,0.01,3,true,w));assertEquals("position0.0",w.events.getFirst());}
	@Test void noBlockDoesNotTerminateAtWall(){for(boolean explosion:List.of(false,true)){var w=new World();assertFalse(CarrierTermination.apply(explosion,2,1,3,false,w));assertTrue(w.events.isEmpty());}}
	@Test void beyondStepContinues(){var w=new World();assertFalse(CarrierTermination.apply(false,1,2,3,true,w));assertTrue(w.events.isEmpty());}
	@Test void wallEffectFailureDoesNotDiscard(){var w=new World(){public void wallBurst(double d){super.wallBurst(d);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CarrierTermination.apply(false,2,1,3,true,w));assertEquals(List.of("burst1.0"),w.events);}
	@Test void nanPreservesComparisons(){var w=new World();assertFalse(CarrierTermination.apply(false,2,Double.NaN,Double.NaN,true,w));assertTrue(w.events.isEmpty());}
}
