package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.physical_shield.*;
class PhysicalShieldReleaseModuleTest {
	static class World implements PhysicalShieldRelease.World {
		final List<String> events=new ArrayList<>();boolean renew,spawn=true;
		public boolean renewPrevious(){events.add("renew");return renew;}
		public boolean spawn(){events.add("spawn");return spawn;}
		public void discardPrevious(){events.add("discard");}
		public void remember(){events.add("remember");}
	}
	@Test void renewalNeverConstructsReplacement(){var w=new World();w.renew=true;assertTrue(PhysicalShieldRelease.apply(w));assertEquals(List.of("renew"),w.events);}
	@Test void failedSpawnPreservesOldShield(){var w=new World();w.spawn=false;assertFalse(PhysicalShieldRelease.apply(w));assertEquals(List.of("renew","spawn"),w.events);}
	@Test void replacementSpawnsBeforeDiscardAndRegistryUpdate(){var w=new World();assertTrue(PhysicalShieldRelease.apply(w));assertEquals(List.of("renew","spawn","discard","remember"),w.events);}
	@Test void renewalEligibilityAndDurationKeepBoundary(){assertTrue(PhysicalShieldRules.canRenew(false,true,true,1E-6));assertFalse(PhysicalShieldRules.canRenew(false,true,true,Math.nextUp(1E-6)));assertFalse(PhysicalShieldRules.canRenew(true,true,true,0));assertFalse(PhysicalShieldRules.canRenew(false,false,true,0));assertFalse(PhysicalShieldRules.canRenew(false,true,false,0));assertEquals(320,PhysicalShieldRules.renewedDuration(160));assertEquals(16,PhysicalShieldRules.CAPACITY);}
}
