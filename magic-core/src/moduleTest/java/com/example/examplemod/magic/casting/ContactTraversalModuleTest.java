package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ContactTraversal;
class ContactTraversalModuleTest {
 static class World implements ContactTraversal.World<Integer> {
  final List<String> events=new ArrayList<>();final Set<Integer> history=new HashSet<>();boolean exodus;double distance=3;
  public double contact(Integer t){events.add("contact"+t);return t;}
  public boolean skip(Integer t,double c){events.add("skip"+t);return c>distance||(exodus&&history.contains(t));}
  public void claim(Integer t){events.add("claim"+t);if(!exodus)history.add(t);}
  public void position(double c){events.add("position"+c);}
  public void effect(Integer t){events.add("effect"+t);}
  public void particles(){events.add("particles");}
  public void discard(){events.add("discard");}
 }
 @Test void piercingRetainsOrderAndClaimsBeforeEffect(){var w=new World();assertFalse(ContactTraversal.apply(List.of(2,1),true,w));assertEquals(List.of("contact2","skip2","claim2","position2.0","effect2","particles","contact1","skip1","claim1","position1.0","effect1","particles"),w.events);assertEquals(Set.of(1,2),w.history);}
 @Test void nonpiercingPositionsTwiceThenDiscardsAndStops(){var w=new World();assertTrue(ContactTraversal.apply(List.of(1,2),false,w));assertEquals(List.of("contact1","skip1","claim1","position1.0","effect1","position1.0","particles","discard"),w.events);}
 @Test void skippedContactsNeverClaimOrApply(){var w=new World();assertFalse(ContactTraversal.apply(List.of(4),false,w));assertEquals(List.of("contact4","skip4"),w.events);assertTrue(w.history.isEmpty());}
 @Test void laterContactsAreRecomputedAfterEarlierEffect(){var w=new World(){boolean moved;public double contact(Integer t){return moved?4:super.contact(t);}public void effect(Integer t){super.effect(t);moved=true;}};assertFalse(ContactTraversal.apply(List.of(1,2),true,w));assertEquals(Set.of(1),w.history);assertFalse(w.events.contains("effect2"));}
 @Test void burstHistoryCanClaimLaterCandidateBeforeItsTurn(){var w=new World(){public void effect(Integer t){super.effect(t);history.addAll(List.of(1,2));}};w.exodus=true;assertFalse(ContactTraversal.apply(List.of(1,2),true,w));assertTrue(w.events.contains("effect1"));assertFalse(w.events.contains("effect2"));}
 @Test void effectFailureRetainsClaimAndPositionWithoutParticlesOrDiscard(){var w=new World(){public void effect(Integer t){super.effect(t);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ContactTraversal.apply(List.of(1,2),false,w));assertEquals(Set.of(1),w.history);assertEquals("effect1",w.events.getLast());}
 @Test void particleFailureDoesNotDiscardOrVisitNextCandidate(){var w=new World(){public void particles(){super.particles();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ContactTraversal.apply(List.of(1,2),false,w));assertEquals("particles",w.events.getLast());assertFalse(w.events.contains("contact2"));}
 @Test void emptyInputDoesNotCallWorld(){var w=new World();assertFalse(ContactTraversal.apply(List.of(),true,w));assertTrue(w.events.isEmpty());}
}
