package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarStone;
class EarthPillarStoneModuleTest {
	@Test void allTwentySevenChecksPrecedeEveryPlacement(){var cells=java.util.stream.IntStream.range(0,27).boxed().toList();var events=new ArrayList<String>();assertTrue(EarthPillarStone.place(cells,new EarthPillarStone.Placement<Integer>(){public boolean available(Integer c){events.add("c"+c);return true;}public void place(Integer c){events.add("p"+c);}}));assertEquals(54,events.size());for(int i=0;i<27;i++){assertEquals("c"+i,events.get(i));assertEquals("p"+i,events.get(i+27));}}
	@Test void everyBlockedCellPreventsAllPlacement(){var cells=java.util.stream.IntStream.range(0,27).boxed().toList();for(int blocked=0;blocked<27;blocked++){final int b=blocked;assertFalse(EarthPillarStone.place(cells,new EarthPillarStone.Placement<Integer>(){public boolean available(Integer c){return c!=b;}public void place(Integer c){fail();}}));}}
	@Test void partialPlacementExceptionPropagatesWithoutPretendRollback(){var placed=new ArrayList<Integer>();assertThrows(IllegalStateException.class,()->EarthPillarStone.place(List.of(0,1,2),new EarthPillarStone.Placement<Integer>(){public boolean available(Integer c){return true;}public void place(Integer c){if(c==1)throw new IllegalStateException();placed.add(c);}}));assertEquals(List.of(0),placed);}
}
