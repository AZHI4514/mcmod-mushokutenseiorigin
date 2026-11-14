package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.heat_hand.*;
class HeatHandModuleTest {
	static class World implements HeatHandMelt.World<Integer>{
		final List<String> events=new ArrayList<>();List<Integer> targets=List.of(1,2);boolean change=true,warmth=true;int frozen;
		public Iterable<Integer> targets(){events.add("targets");return targets;}
		public boolean melt(Integer t){events.add("melt"+t);return change;}
		public boolean hasWarmth(){events.add("warmth");return warmth;}
		public int frozenTicks(){events.add("frozen");return frozen;}
		public void warm(){events.add("warm");}
		public void particles(Integer t){events.add("particles"+t);}
	}
	@Test void everyMeltRunsBeforeWarmth(){var w=new World();assertTrue(HeatHandMelt.apply(w));assertEquals(List.of("targets","melt1","melt2","warmth","warm","particles1","particles2"),w.events);}
	@Test void unchangedAndWarmReturnsFalse(){var w=new World();w.change=false;assertFalse(HeatHandMelt.apply(w));assertEquals(List.of("targets","melt1","melt2","warmth","frozen"),w.events);}
	@Test void noTargetStillWarmsWhenCold(){var w=new World();w.targets=List.of();w.warmth=false;assertTrue(HeatHandMelt.apply(w));assertEquals(List.of("targets","warmth","frozen","warm"),w.events);}
	@Test void frozenRewarmsEvenWithExistingWarmth(){var w=new World();w.change=false;w.frozen=1;assertTrue(HeatHandMelt.apply(w));assertTrue(w.events.contains("warm"));}
	@Test void partialMeltExceptionDoesNotInventWarmthOrParticles(){var w=new World(){public boolean melt(Integer t){super.melt(t);if(t==2)throw new IllegalStateException();return true;}};assertThrows(IllegalStateException.class,()->HeatHandMelt.apply(w));assertEquals(List.of("targets","melt1","melt2"),w.events);}
	@Test void materialAndEvaporationRules(){assertTrue(HeatHandRules.becomesWater(true,false));assertFalse(HeatHandRules.becomesWater(true,true));assertFalse(HeatHandRules.becomesWater(false,false));}
	@Test void facePlansPreserveOrderedCells(){assertEquals(List.of(new HeatHandRules.Cell(0,0,0),new HeatHandRules.Cell(0,0,1),new HeatHandRules.Cell(1,0,0),new HeatHandRules.Cell(1,0,1)),HeatHandRules.targets(0,0,0,HeatHandRules.Axis.Y));assertEquals(new HeatHandRules.Cell(0,1,1),HeatHandRules.targets(0,0,0,HeatHandRules.Axis.X).getLast());assertEquals(new HeatHandRules.Cell(1,1,0),HeatHandRules.targets(0,0,0,HeatHandRules.Axis.Z).getLast());}
	@Test void warmthFailureSkipsParticleLoop(){var w=new World(){public void warm(){super.warm();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->HeatHandMelt.apply(w));assertEquals("warm",w.events.getLast());}
}
