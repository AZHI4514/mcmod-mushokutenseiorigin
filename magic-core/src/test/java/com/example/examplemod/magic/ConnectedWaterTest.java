package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import com.example.examplemod.magic.spell.water.waterfall.ConnectedWater;
import com.example.examplemod.magic.spell.water.waterfall.ConnectedWater.Cell;
class ConnectedWaterTest {
	@Test void disconnectedWaterAndUnavailableCellsCannotBridge() {
		var source=new Cell(0,0,0);var queried=new HashSet<Cell>();
		var found=ConnectedWater.find(source,c->{ queried.add(c);return c.y()==0&&c.z()==0&&c.x()>0&&c.x()!=3; });
		assertEquals(Set.of(source,new Cell(1,0,0),new Cell(2,0,0)),found);
		assertFalse(queried.contains(new Cell(4,0,0)));
	}
	@Test void radiusIncludesSixteenButNeverQueriesSeventeen() {
		var found=ConnectedWater.find(new Cell(0,0,0),c->{
			assertTrue(Math.abs(c.x())<=16&&Math.abs(c.y())<=16&&Math.abs(c.z())<=16);
			return c.y()==0&&c.z()==0;
		});
		assertEquals(33,found.size());assertTrue(found.contains(new Cell(-16,0,0)));
	}
	@Test void denseSearchPreservesLegacyExpansionBoundaryAndDirectionOrder() {
		var expected=new HashSet<Cell>();var queue=new ArrayDeque<Cell>();var source=new Cell(0,0,0);
		expected.add(source);queue.add(source);
		int[][] directions={{0,-1,0},{0,1,0},{0,0,-1},{0,0,1},{-1,0,0},{1,0,0}};
		while(!queue.isEmpty()&&expected.size()<4096) {
			var at=queue.removeFirst();for(var d:directions) {
				var next=new Cell(at.x()+d[0],at.y()+d[1],at.z()+d[2]);
				if(Math.abs(next.x())>16||Math.abs(next.y())>16||Math.abs(next.z())>16||expected.contains(next))continue;
				expected.add(next);queue.add(next);
			}
		}
		assertEquals(expected,ConnectedWater.find(source,c->true));
		assertTrue(expected.size()>=4096&&expected.size()<=4101);
	}
	@Test void wetRefreshUsesWorldTimeRatherThanChannelAge() {
		for(long tick=0;tick<200;tick++)assertEquals(tick%10==0,ConnectedWater.refreshDue(tick));
	}
}
