package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.terrain.ColumnGrowth;
class ColumnGrowthModuleTest {
	private static final class World implements ColumnGrowth.Placement<Integer> {
		final List<Integer> placed=new ArrayList<>();final List<Integer> leases=new ArrayList<>();int blocked=2;
		public boolean available(Integer cell){return cell!=0;}
		public boolean occupied(Integer cell){return cell==blocked;}
		public void place(Integer cell,int remaining){placed.add(cell);leases.add(remaining);}
	}
	@Test void invalidCellsSkipButFirstOccupiedCellPausesTheWholeOrderedTail(){
		var source=new ArrayList<>(List.of(0,1,2,3));var job=new ColumnGrowth<>(source,200);source.clear();var world=new World();
		assertFalse(job.tick(1,world));assertEquals(List.of(1),world.placed);assertEquals(List.of(199),world.leases);
		assertFalse(job.tick(50,world));assertEquals(List.of(1),world.placed);
		world.blocked=-1;assertTrue(job.tick(60,world));assertEquals(List.of(1,2,3),world.placed);assertEquals(List.of(199,140,140),world.leases);
	}
	@Test void placementExceptionRetriesOnlyUnfinishedCellsWithUpdatedLease(){
		var events=new ArrayList<String>();var fail=new boolean[]{true};var job=new ColumnGrowth<>(List.of(1,2,3),200);
		var world=new ColumnGrowth.Placement<Integer>() {
			public boolean available(Integer cell){return true;}
			public boolean occupied(Integer cell){return false;}
			public void place(Integer cell,int ticks){events.add(cell+":"+ticks);if(cell==2&&fail[0])throw new IllegalStateException("partial placement");}
		};
		assertThrows(IllegalStateException.class,()->job.tick(10,world));
		assertEquals(List.of("1:190","2:190"),events);fail[0]=false;
		assertTrue(job.tick(30,world));assertEquals(List.of("1:190","2:190","2:170","3:170"),events);
		assertTrue(job.tick(40,world));assertEquals(4,events.size());
	}
	@Test void expiryTouchesNoWorldAndJobsAreIndependent(){
		var world=new World();assertTrue(new ColumnGrowth<>(List.of(1,2),200).tick(200,world));assertTrue(world.placed.isEmpty());
		var a=new ColumnGrowth<>(List.of(2),200);var b=new ColumnGrowth<>(List.of(1),200);assertFalse(a.tick(0,world));assertTrue(b.tick(0,world));assertEquals(List.of(1),world.placed);
	}
}
