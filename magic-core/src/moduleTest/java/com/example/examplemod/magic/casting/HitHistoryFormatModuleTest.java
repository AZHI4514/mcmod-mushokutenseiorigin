package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.HitHistoryFormat;
class HitHistoryFormatModuleTest {
 @Test void restoreClearsBeforeReadAndKeepsSameSet(){var old=UUID.randomUUID();var next=UUID.randomUUID();var hits=new HashSet<UUID>();hits.add(old);HitHistoryFormat.restore(hits,()->{assertTrue(hits.isEmpty());return List.of(next.toString(),next.toString(),"invalid");});assertEquals(Set.of(next),hits);}
 @Test void missingListClearsOldHistory(){var hits=new HashSet<>(Set.of(UUID.randomUUID()));HitHistoryFormat.restore(hits,List::of);assertTrue(hits.isEmpty());}
 @Test void readerFailureLeavesClearedHistory(){var hits=new HashSet<>(Set.of(UUID.randomUUID()));assertThrows(IllegalStateException.class,()->HitHistoryFormat.restore(hits,()->{throw new IllegalStateException();}));assertTrue(hits.isEmpty());}
 @Test void unexpectedNullDoesNotHideEarlierValidEntries(){var id=UUID.randomUUID();var hits=new HashSet<UUID>();assertThrows(NullPointerException.class,()->HitHistoryFormat.restore(hits,()->Arrays.asList(id.toString(),null)));assertEquals(Set.of(id),hits);}
 @Test void savePreservesIterationOrderAndRoundTripsWithoutAliasing(){var a=UUID.randomUUID();var b=UUID.randomUUID();var original=new LinkedHashSet<>(List.of(a,b));var saved=HitHistoryFormat.save(original);original.clear();assertEquals(List.of(a.toString(),b.toString()),saved);var restored=new HashSet<UUID>();HitHistoryFormat.restore(restored,()->saved);assertEquals(Set.of(a,b),restored);}
}
