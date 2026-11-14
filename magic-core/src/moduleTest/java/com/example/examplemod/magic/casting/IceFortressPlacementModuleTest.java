package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.ice_fortress.IceFortressPlacement;
import com.example.examplemod.magic.mechanic.terrain.ColumnGrowth;
class IceFortressPlacementModuleTest {
	private static final class World implements IceFortressPlacement.World<Integer,Integer>,ColumnGrowth.Placement<Integer> {
		final List<String> events=new ArrayList<>();boolean occupied;ColumnGrowth<Integer> job;
		public List<Integer> targets(){return occupied?List.of(7,8):List.of();}
		public void launch(Integer id){events.add("launch"+id);}
		public void grow(List<Integer> cells){events.add("grow");job=new ColumnGrowth<>(cells,200);}
		public void place(Integer cell,int ticks){events.add(cell+":"+ticks);}
		public void burst(){events.add("burst");}
		public boolean available(Integer cell){return true;}
		public boolean occupied(Integer cell){return occupied;}
	}
	@Test void launchFailureDoesNotQueueOrBurstAfterPartialLaunch(){
		var events=new ArrayList<String>();
		var world=new IceFortressPlacement.World<Integer,Integer>() {
			public List<Integer> targets(){return List.of(7,8,9);}
			public void launch(Integer id){events.add("launch"+id);if(id==8)throw new IllegalStateException("launch");}
			public void grow(List<Integer> cells){fail("must not queue");}
			public void place(Integer cell,int ticks){fail("must not place");}
			public void burst(){fail("must not notify success");}
		};
		assertThrows(IllegalStateException.class,()->IceFortressPlacement.apply(List.of(1,2),world));assertEquals(List.of("launch7","launch8"),events);
	}
	@Test void immediatePlacementFailureLeavesEarlierWorldEffectsAndStopsTheTail(){
		var events=new ArrayList<String>();
		var world=new IceFortressPlacement.World<Integer,Integer>() {
			public List<Integer> targets(){return List.of();}
			public void launch(Integer id){fail();}
			public void grow(List<Integer> cells){fail();}
			public void place(Integer cell,int ticks){events.add(cell+":"+ticks);if(cell==2)throw new IllegalStateException("place");}
			public void burst(){fail("must not notify success");}
		};
		assertThrows(IllegalStateException.class,()->IceFortressPlacement.apply(List.of(1,2,3),world));assertEquals(List.of("1:200","2:200"),events);
	}
	@Test void emptyAreaPlacesImmediatelyThenBursts(){var w=new World();IceFortressPlacement.apply(List.of(1,2),w);assertEquals(List.of("1:200","2:200","burst"),w.events);assertNull(w.job);}
	@Test void occupiedAreaLaunchesThenQueuesAndLaterSharesOneExpiry(){
		var w=new World();w.occupied=true;IceFortressPlacement.apply(List.of(1,2),w);assertEquals(List.of("launch7","launch8","grow","burst"),w.events);
		assertFalse(w.job.tick(1,w));w.occupied=false;assertTrue(w.job.tick(40,w));assertEquals(List.of("launch7","launch8","grow","burst","1:160","2:160"),w.events);
	}
	@Test void expiredDelayedJobNeverPlacesEvenWhenOccupantLeaves(){var w=new World();w.occupied=true;IceFortressPlacement.apply(List.of(1),w);w.occupied=false;assertTrue(w.job.tick(200,w));assertEquals(List.of("launch7","launch8","grow","burst"),w.events);}
}
