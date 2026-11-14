package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.absolute_zero.*;
class AbsoluteZeroModuleTest {
	private static class Target implements AbsoluteZeroContact.Target {
		List<String> calls=new ArrayList<>();boolean warm,wet=true,accepted=true,freezable=true;float damage;
		public boolean inside(){return true;}public boolean owner(){return true;}public boolean allowed(){fail();return false;}public boolean visible(){return true;}public boolean warm(){calls.add("warm");return warm;}public boolean wet(){calls.add("wet");return wet;}public boolean hurt(float value){damage=value;calls.add("hurt");return accepted;}public void removeWet(){calls.add("removeWet");}public void removeWarm(){calls.add("removeWarm");}public boolean canFreeze(){calls.add("canFreeze");return freezable;}public void freeze(){calls.add("freeze");}public void control(){calls.add("control");}public void afterHit(){calls.add("hook");}public boolean randomFreeze(){calls.add("random");return true;}
	}
	@Test void wetBonusAndWarmSnapshotPrecedeDamageThenRemovalAndFreeze(){var t=new Target();AbsoluteZeroContact.apply(true,t);assertEquals(34,t.damage);assertEquals(List.of("warm","wet","hurt","removeWet","canFreeze","freeze","control","hook"),t.calls);}
	@Test void warmthConsumesWarmthInsteadOfFreezingOrControl(){var t=new Target();t.warm=true;AbsoluteZeroContact.apply(true,t);assertEquals(List.of("warm","wet","hurt","removeWet","removeWarm","hook"),t.calls);}
	@Test void refusedDamageLeavesWetWarmAndNoFogFallback(){var t=new Target();t.accepted=false;AbsoluteZeroContact.apply(true,t);assertEquals(List.of("warm","wet","hurt"),t.calls);}
	@Test void freezeImmuneStillReceivesControlOnInitialRelease(){var t=new Target();t.freezable=false;t.wet=false;AbsoluteZeroContact.apply(true,t);assertEquals(28,t.damage);assertFalse(t.calls.contains("freeze"));assertTrue(t.calls.contains("control"));}
	@Test void coldMistOnlyRollsAfterWarmthAndFreezeEligibility(){var t=new Target();AbsoluteZeroContact.apply(false,t);assertEquals(List.of("warm","canFreeze","random","freeze"),t.calls);var warm=new Target();warm.warm=true;AbsoluteZeroContact.apply(false,warm);assertEquals(List.of("warm"),warm.calls);var immune=new Target();immune.freezable=false;AbsoluteZeroContact.apply(false,immune);assertEquals(List.of("warm","canFreeze"),immune.calls);}
	@Test void freezeExceptionStopsControlAndHookAfterWetConsumption(){var t=new Target(){public void freeze(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->AbsoluteZeroContact.apply(true,t));assertTrue(t.calls.contains("removeWet"));assertFalse(t.calls.contains("control"));assertFalse(t.calls.contains("hook"));}
	@Test void sphereAndControlBoundariesRemainExact(){assertTrue(AbsoluteZeroRules.contains(16,0,0));assertFalse(AbsoluteZeroRules.contains(16,0,.001));assertFalse(AbsoluteZeroRules.contains(Double.NaN,0,0));assertEquals(40,AbsoluteZeroRules.controlTicks(true));assertEquals(60,AbsoluteZeroRules.controlTicks(false));}
}
