package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.electric.*;
class ElectricModuleTest {
	private static class Target implements ElectricImpact.Target {
		List<String> calls=new ArrayList<>();boolean accepted=true,immune;
		public boolean hurt(){calls.add("hurt");return accepted;}public boolean immune(){calls.add("immune");return immune;}public void slow(){calls.add("slow");}public void control(){calls.add("control");}public void afterHit(){calls.add("hook");}public void burst(){calls.add("burst");}
	}
	@Test void ordinaryHitControlsBeforeHookAndBurst(){var t=new Target();ElectricImpact.apply(t);assertEquals(List.of("hurt","immune","control","hook","burst"),t.calls);}
	@Test void immuneHitSlowsInsteadOfControl(){var t=new Target();t.immune=true;ElectricImpact.apply(t);assertEquals(List.of("hurt","immune","slow","hook","burst"),t.calls);}
	@Test void refusedDamageStillEmitsBurstWithoutImmuneRead(){var t=new Target();t.accepted=false;ElectricImpact.apply(t);assertEquals(List.of("hurt","burst"),t.calls);}
	@Test void hookExceptionDoesNotEmitBurst(){var t=new Target(){public void afterHit(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ElectricImpact.apply(t));assertTrue(t.calls.contains("control"));assertFalse(t.calls.contains("burst"));}
	@Test void nearestTiePreservesFirstAndNaNIsRejected(){assertFalse(ElectricRules.nearer(2,2));assertTrue(ElectricRules.nearer(Math.nextDown(2.0),2));assertFalse(ElectricRules.nearer(Double.NaN,2));assertFalse(ElectricRules.nearer(1,Double.NaN));}
	@Test void playersAndMobsRetainDifferentControlDuration(){assertEquals(40,ElectricRules.controlTicks(true));assertEquals(60,ElectricRules.controlTicks(false));}
}
