package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.explosion.ExplosionImpact;
import com.example.examplemod.magic.mechanic.projectile.ImpactHistory;
import com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules;
class ExplosionImpactModuleTest {
	private static final class World implements ExplosionImpact.World {
		final List<String> events=new ArrayList<>();
		public void burst(){events.add("burst");}
		public void explode(float power){events.add("TNT:"+power);}
		public void discard(){events.add("discard");}
	}
	@Test void piercingContactBurstsBeforeVanillaAndKeepsCarrier(){var w=new World();ExplosionImpact.apply(true,w);assertEquals(List.of("burst","TNT:4.0"),w.events);}
	@Test void terminalContactDiscardsOnlyAfterVanilla(){var w=new World();ExplosionImpact.apply(false,w);assertEquals(List.of("burst","TNT:4.0","discard"),w.events);}
	@Test void fluidWinsTiesAndOnlyRealSolidContactTriggersSolidStop(){
		assertEquals(ExplosionImpact.Stop.FLUID,ExplosionImpact.stop(2,1,1,true));
		assertEquals(ExplosionImpact.Stop.FLUID,ExplosionImpact.stop(2,3,2,false));
		assertEquals(ExplosionImpact.Stop.SOLID,ExplosionImpact.stop(2,2,3,true));
		assertEquals(ExplosionImpact.Stop.NONE,ExplosionImpact.stop(2,2,3,false));
		assertEquals(ExplosionImpact.Stop.NONE,ExplosionImpact.stop(2,3,4,true));
	}
	@Test void failedVanillaExplosionDoesNotPretendToFinishOrRollback(){
		var events=new ArrayList<String>();
		assertThrows(IllegalStateException.class,()->ExplosionImpact.apply(false,new ExplosionImpact.World(){
			public void burst(){events.add("burst");}
			public void explode(float power){events.add("TNT");throw new IllegalStateException("world event");}
			public void discard(){fail("old ordering must remain");}
		}));assertEquals(List.of("burst","TNT"),events);
	}
	@Test void restoredHistorySuppressesSameVictimButAllowsNextVictim(){
		var a=UUID.randomUUID();var b=UUID.randomUUID();var history=new ImpactHistory();var w=new World();
		assertTrue(history.claim(a));ExplosionImpact.apply(true,w);
		var restored=new ImpactHistory(history.snapshot());
		assertFalse(PiercingFlightRules.candidate(.5,3,4,restored.contains(a)));
		assertFalse(restored.claim(a));assertTrue(PiercingFlightRules.candidate(1,3,4,restored.contains(b)));
		assertTrue(PiercingFlightRules.contactDue(1,2,false));assertTrue(restored.claim(b));ExplosionImpact.apply(true,w);
		assertEquals(List.of("burst","TNT:4.0","burst","TNT:4.0"),w.events);assertEquals(Set.of(a,b),restored.snapshot());
	}
	@Test void expirationAndBlockingBoundaryExcludeVictimExplosions(){
		assertFalse(PiercingFlightRules.expired(119,120));assertTrue(PiercingFlightRules.expired(120,120));
		assertTrue(PiercingFlightRules.expired(21,20));
		assertFalse(PiercingFlightRules.candidate(1,1,3,false));assertFalse(PiercingFlightRules.candidate(1,3,1,false));
		assertTrue(PiercingFlightRules.candidate(2,3,4,false));assertTrue(PiercingFlightRules.contactDue(2,2,false));
		assertFalse(PiercingFlightRules.contactDue(2.01,2,false));
	}
}
