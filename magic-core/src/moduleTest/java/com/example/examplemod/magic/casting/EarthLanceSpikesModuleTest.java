package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceSpikes;
class EarthLanceSpikesModuleTest {
	static class World implements EarthLanceSpikes.Placement {
		final List<String> events=new ArrayList<>();int blocked=-1,fail=-1;
		public boolean available(int y){events.add("check"+y);return y!=blocked;}
		public void begin(){events.add("begin");}
		public void place(int y,int duration){assertEquals(200,duration);if(y==fail)throw new IllegalStateException();events.add("place"+y);}
		public void finish(){events.add("dirty");}
	}
	@Test void allCellsCheckedBeforeSavedDataAndBottomUpPlacement(){var w=new World();assertTrue(EarthLanceSpikes.raise(w));assertEquals(List.of("check0","check1","check2","begin","place0","place1","place2","dirty"),w.events);}
	@Test void anyRejectedCellPreventsEveryMutation(){for(int y=0;y<3;y++){var w=new World();w.blocked=y;assertFalse(EarthLanceSpikes.raise(w));assertEquals(y+1,w.events.size());assertTrue(w.events.stream().allMatch(e->e.startsWith("check")));}}
	@Test void partialPlacementFailureIsNotReportedAsReversibleFalse(){var w=new World();w.fail=1;assertThrows(IllegalStateException.class,()->EarthLanceSpikes.raise(w));assertEquals(List.of("check0","check1","check2","begin","place0"),w.events);}
	@Test void frozenThreeCellPreflightMatchesAllAvailabilityMasks(){for(int mask=0;mask<8;mask++){final int bits=mask;var events=new ArrayList<Integer>();boolean result=EarthLanceSpikes.raise(new EarthLanceSpikes.Placement(){public boolean available(int y){return (bits&(1<<y))!=0;}public void begin(){}public void place(int y,int duration){events.add(y);}public void finish(){}});assertEquals(mask==7,result);assertEquals(mask==7?List.of(0,1,2):List.of(),events);}}
}
