package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.heat_island.HeatIslandRules;
class HeatIslandModuleTest {
	@Test void inclusiveRadiusAndIeee(){assertTrue(HeatIslandRules.inside(3,0));assertFalse(HeatIslandRules.inside(Math.nextUp(3.0),0));assertFalse(HeatIslandRules.inside(Double.NaN,0));assertFalse(HeatIslandRules.inside(Double.POSITIVE_INFINITY,0));}
	@Test void frozenRadiusDifferential(){var r=new Random(902);for(int i=0;i<10000;i++){double x=r.nextDouble()*12-6,z=r.nextDouble()*12-6;assertEquals(x*x+z*z<=3*3,HeatIslandRules.inside(x,z));}}
	@Test void removesWetBeforeWarm(){var events=new ArrayList<String>();HeatIslandRules.apply(new HeatIslandRules.Target(){public void removeWet(){events.add("remove");}public void warm(){events.add("warm");}});assertEquals(List.of("remove","warm"),events);}
	@Test void removeFailureDoesNotWarm(){HeatIslandRules.Target t=new HeatIslandRules.Target(){public void removeWet(){throw new IllegalStateException();}public void warm(){fail();}};assertThrows(IllegalStateException.class,()->HeatIslandRules.apply(t));}
}
