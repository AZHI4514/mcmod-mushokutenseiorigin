package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceLift;
class EarthLanceLiftModuleTest {
	static class World implements EarthLanceLift.World<String> {
		final List<String> events=new ArrayList<>();String fail="";
		public void launch(String t){events.add(t);if(t.equals(fail))throw new IllegalStateException();}
		public void growPermanent(){events.add("grow");}
		public void placePermanent(){events.add("place");}
		public void show(){events.add("show");}
	}
	@Test void launchesPreserveWorldOrderAndPlayerLastBeforeGrowing(){var w=new World();EarthLanceLift.apply(List.of("mobA","mobB","player"),w);assertEquals(List.of("mobA","mobB","player","grow","show"),w.events);}
	@Test void emptyTargetsPlaceImmediatelyWithoutGrowth(){var w=new World();EarthLanceLift.apply(List.of(),w);assertEquals(List.of("place","show"),w.events);}
	@Test void partialLaunchExceptionNeverSchedulesOrPublishes(){var w=new World();w.fail="second";assertThrows(IllegalStateException.class,()->EarthLanceLift.apply(List.of("first","second","player"),w));assertEquals(List.of("first","second"),w.events);}
	@Test void repeatedCastsCreateIndependentJobsAndDoNotChangeCallerList(){var a=new World();var b=new World();var targets=List.of("player");EarthLanceLift.apply(targets,a);EarthLanceLift.apply(targets,b);assertEquals(List.of("player","grow","show"),a.events);assertEquals(a.events,b.events);assertEquals(List.of("player"),targets);}
}
