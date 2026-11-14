package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.CarrierHit;
class PhaseTwoHitModuleTest {
	static class Target implements CarrierHit.Target {
		final List<String> events=new ArrayList<>();boolean allowed=true,hurt=true;
		public boolean allowed(){events.add("allowed");return allowed;}
		public boolean hurt(){events.add("hurt");return hurt;}
		public void effects(){events.add("effects");}
		public void afterHit(){events.add("hook");}
		public void postHit(){events.add("post");}
	}
	@Test void orderedHit(){var t=new Target();CarrierHit.apply(t);assertEquals(List.of("allowed","hurt","effects","hook","post"),t.events);}
	@Test void deniedPermissionNeverDamages(){var t=new Target();t.allowed=false;CarrierHit.apply(t);assertEquals(List.of("allowed"),t.events);}
	@Test void rejectedDamageNeverAppliesEffects(){var t=new Target();t.hurt=false;CarrierHit.apply(t);assertEquals(List.of("allowed","hurt"),t.events);}
	@Test void hookFailurePreventsPostHit(){var t=new Target(){public void afterHit(){super.afterHit();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CarrierHit.apply(t));assertEquals(List.of("allowed","hurt","effects","hook"),t.events);}
	@Test void effectFailurePreventsHook(){var t=new Target(){public void effects(){super.effects();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CarrierHit.apply(t));assertEquals(List.of("allowed","hurt","effects"),t.events);}
	@Test void postHitObservesHookChanges(){var t=new Target(){boolean changed;public void afterHit(){super.afterHit();changed=true;}public void postHit(){assertTrue(changed);super.postHit();}};CarrierHit.apply(t);assertEquals("post",t.events.getLast());}
}
