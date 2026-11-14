package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.CarrierContactTraversal;
class PhaseTwoContactTraversalModuleTest {
	static class World implements CarrierContactTraversal.World<Integer> {
		final List<String> events=new ArrayList<>();final Set<Integer> hits=new HashSet<>();boolean explosion,piercing=true;
		public double contact(Integer t){events.add("contact"+t);return t;}
		public boolean skip(Integer t,double c){events.add("skip"+t);return hits.contains(t);}
		public void claim(Integer t){events.add("claim"+t);hits.add(t);}
		public boolean explosion(){events.add("explosion?");return explosion;}
		public void position(double c){events.add("position"+c);}
		public boolean piercing(){events.add("piercing?");return piercing;}
		public void explode(boolean keep){events.add("explode"+keep);}
		public void effect(Integer t){events.add("effect"+t);}
		public void burst(double c){events.add("burst"+c);}
		public void discard(){events.add("discard");}
	}
	@Test void ordinaryOrder(){var w=new World();assertFalse(CarrierContactTraversal.apply(List.of(1),w));assertEquals(List.of("contact1","skip1","claim1","explosion?","effect1","burst1.0","piercing?"),w.events);}
	@Test void nonPiercingStopsAndDiscards(){var w=new World();w.piercing=false;assertTrue(CarrierContactTraversal.apply(List.of(1,2),w));assertEquals("discard",w.events.getLast());assertFalse(w.hits.contains(2));}
	@Test void explosionHasOwnPresentation(){var w=new World();w.explosion=true;assertFalse(CarrierContactTraversal.apply(List.of(1),w));assertEquals(List.of("contact1","skip1","claim1","explosion?","position1.0","piercing?","explodetrue","piercing?"),w.events);}
	@Test void explosionRereadsPiercingAfterEffect(){var w=new World(){public void explode(boolean keep){super.explode(keep);piercing=false;}};w.explosion=true;assertTrue(CarrierContactTraversal.apply(List.of(1,2),w));assertTrue(w.events.contains("explodetrue"));assertFalse(w.events.contains("discard"));assertFalse(w.hits.contains(2));}
	@Test void nonPiercingExplosionDoesNotExtraDiscard(){var w=new World();w.explosion=true;w.piercing=false;assertTrue(CarrierContactTraversal.apply(List.of(1),w));assertFalse(w.events.contains("discard"));assertTrue(w.events.contains("explodefalse"));}
	@Test void skipDoesNotClaimOrEffect(){var w=new World();w.hits.add(1);assertFalse(CarrierContactTraversal.apply(List.of(1),w));assertEquals(List.of("contact1","skip1"),w.events);}
	@Test void burstCanAffectLaterHistory(){var w=new World(){public void effect(Integer t){super.effect(t);hits.add(2);}};assertFalse(CarrierContactTraversal.apply(List.of(1,2),w));assertFalse(w.events.contains("effect2"));}
	@Test void failureKeepsClaimAndSkipsPresentation(){var w=new World(){public void effect(Integer t){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CarrierContactTraversal.apply(List.of(1,2),w));assertTrue(w.hits.contains(1));assertFalse(w.events.contains("burst1.0"));assertFalse(w.events.contains("discard"));}
	@Test void ordinaryPiercingIsReadAfterBurst(){var w=new World(){public void burst(double c){super.burst(c);piercing=false;}};assertTrue(CarrierContactTraversal.apply(List.of(1,2),w));assertEquals("discard",w.events.getLast());}
	@Test void emptyDoesNotReadWorld(){var w=new World();assertFalse(CarrierContactTraversal.apply(List.of(),w));assertTrue(w.events.isEmpty());}
}
