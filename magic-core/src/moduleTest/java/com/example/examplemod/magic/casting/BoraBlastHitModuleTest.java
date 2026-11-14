package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.mixed.bora_blast.BoraBlastHit;
class BoraBlastHitModuleTest {
	static class Target implements BoraBlastHit.Target {
		final List<String> events=new ArrayList<>();boolean accepted=true,immune,warm,wet,freezable=true,useAim;double strength;
		public boolean hurt(){events.add("hurt");return accepted;}
		public boolean impulseImmune(){events.add("immune");return immune;}
		public void impulse(double s,boolean a){events.add("impulse");strength=s;useAim=a;}
		public boolean warm(){events.add("warm");return warm;}
		public void removeWarm(){events.add("removeWarm");warm=false;}
		public boolean canFreeze(){events.add("canFreeze");return freezable;}
		public void freeze(int ticks){assertEquals(300,ticks);events.add("freeze");}
		public boolean wet(){events.add("wet");return wet;}
		public void slow(int ticks,int amplifier){assertEquals(100,ticks);assertEquals(1,amplifier);events.add("slow");}
		public void afterHit(){events.add("after");}
	}
	@Test void wetColdHitFreezesSlowsAndThenHooks(){var t=new Target();t.wet=true;BoraBlastHit.apply(t,1);assertEquals(List.of("hurt","immune","impulse","warm","canFreeze","freeze","wet","slow","after"),t.events);assertEquals(.7,t.strength);assertFalse(t.useAim);}
	@Test void rejectedDamageDoesNotReadStatuses(){var t=new Target();t.accepted=false;BoraBlastHit.apply(t,0);assertEquals(List.of("hurt"),t.events);}
	@Test void warmIsConsumedWithoutFreezeButWetSlowRemains(){var t=new Target();t.warm=true;t.wet=true;BoraBlastHit.apply(t,0);assertFalse(t.warm);assertEquals(List.of("hurt","immune","impulse","warm","removeWarm","wet","slow","after"),t.events);}
	@Test void impulseImmunityDoesNotBlockFreezeOrHook(){var t=new Target();t.immune=true;BoraBlastHit.apply(t,0);assertEquals(List.of("hurt","immune","warm","canFreeze","freeze","wet","after"),t.events);}
	@Test void cannotFreezeStillAllowsWetSlow(){var t=new Target();t.freezable=false;t.wet=true;BoraBlastHit.apply(t,0);assertFalse(t.events.contains("freeze"));assertTrue(t.events.contains("slow"));assertEquals("after",t.events.getLast());}
	@Test void statesAreReadAfterDamageAndImpulse(){var t=new Target(){public boolean hurt(){warm=true;return super.hurt();}public void impulse(double s,boolean a){super.impulse(s,a);wet=true;}};BoraBlastHit.apply(t,0);assertTrue(t.events.contains("removeWarm"));assertTrue(t.events.contains("slow"));assertFalse(t.events.contains("freeze"));}
	@Test void wetIsReadAgainAfterWarmthConsumption(){var t=new Target(){public void removeWarm(){super.removeWarm();wet=true;}};t.warm=true;BoraBlastHit.apply(t,0);assertTrue(t.events.contains("slow"));}
	@Test void nearZeroImpulseHasIndependentStrictSquaredThreshold(){for(double sq:new double[]{0,Math.nextDown(1E-6),1E-6,Double.NaN}){var t=new Target();BoraBlastHit.apply(t,sq);assertEquals(sq<1E-6,t.useAim);}}
	@Test void consumedWarmthExceptionStopsWetAndHook(){var t=new Target(){public void removeWarm(){super.removeWarm();throw new IllegalStateException();}};t.warm=true;assertThrows(IllegalStateException.class,()->BoraBlastHit.apply(t,0));assertFalse(t.warm);assertEquals("removeWarm",t.events.getLast());}
	@Test void slowExceptionKeepsEarlierFreezeButStopsHook(){var t=new Target(){public void slow(int n,int a){super.slow(n,a);throw new IllegalStateException();}};t.wet=true;assertThrows(IllegalStateException.class,()->BoraBlastHit.apply(t,0));assertTrue(t.events.contains("freeze"));assertEquals("slow",t.events.getLast());}
}
