package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.*;
import com.example.examplemod.magic.spell.wind.vacuum_storm.VacuumStormPolicy;
class VacuumStormModuleTest {
	private static class Target implements WindFieldPulse.Target {
		List<String> calls=new ArrayList<>();String reject="";boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean claim(){return pass("claim");}public boolean hurt(){return pass("hurt");}public void fireEffects(){fail();}public void afterHit(){calls.add("hook");}public void slow(){calls.add("slow");}
	}
	@Test void cadenceRefusalStillAppliesSlow(){var t=new Target();t.reject="claim";WindFieldPulse.apply(false,true,t);assertEquals(List.of("claim","slow"),t.calls);}
	@Test void hurtRefusalStillAppliesSlow(){var t=new Target();t.reject="hurt";WindFieldPulse.apply(false,true,t);assertEquals(List.of("claim","hurt","slow"),t.calls);}
	@Test void successfulHitNotifiesBeforeApplyingSlow(){var t=new Target();WindFieldPulse.apply(false,true,t);assertEquals(List.of("claim","hurt","hook","slow"),t.calls);}
	@Test void hookExceptionDoesNotApplySlow(){var t=new Target(){public void afterHit(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WindFieldPulse.apply(false,true,t));assertFalse(t.calls.contains("slow"));}
	@Test void policyIsFixedCylinderWithNoMotionOrDispersal(){var p=new VacuumStormPolicy();assertEquals(12,p.radius());assertEquals(12,p.height());assertTrue(p.vacuum());assertFalse(p.excludesOwner());assertFalse(p.moving());assertFalse(p.disperses());assertFalse(p.movesTarget(true));assertFalse(p.movesTarget(false));assertEquals(new WindFieldRules.Velocity(1,2,3),p.impulse(4,5,1,2,3,true));}
}
