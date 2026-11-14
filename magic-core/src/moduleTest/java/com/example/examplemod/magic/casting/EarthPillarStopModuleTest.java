package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarStop;
class EarthPillarStopModuleTest {
	static class World implements EarthPillarStop.World {
		final List<String> events=new ArrayList<>();boolean owner=true,failStone;
		public void move(double d){assertEquals(.25,d);events.add("move/update");}
		public void show(){events.add("show");}public boolean hasOwner(){return owner;}
		public void stone(){events.add("stone");if(failStone)throw new IllegalStateException();}
		public void discard(){events.add("discard");}
	}
	@Test void moveAndBoundsBeforeVisualStoneAndDiscard(){var w=new World();EarthPillarStop.apply(.25,w);assertEquals(List.of("move/update","show","stone","discard"),w.events);}
	@Test void absentOwnerStillShowsAndDiscards(){var w=new World();w.owner=false;EarthPillarStop.apply(.25,w);assertEquals(List.of("move/update","show","discard"),w.events);}
	@Test void stoneExceptionKeepsPriorEffectsAndDoesNotInventFinallyDiscard(){var w=new World();w.failStone=true;assertThrows(IllegalStateException.class,()->EarthPillarStop.apply(.25,w));assertEquals(List.of("move/update","show","stone"),w.events);}
}
