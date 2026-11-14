package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.AreaRegistry;
class AreaRegistryModuleTest {
	@Test void identicalOwnersRemainIsolated(){var a=new AreaRegistry<Object>();var b=new AreaRegistry<Object>();var id=UUID.randomUUID();var first=new Object();var second=new Object();a.entries(id).put("water_shield",first);b.entries(id).put("water_shield",second);assertSame(first,a.get(id).get("water_shield"));assertSame(second,b.get(id).get("water_shield"));a.clear(id,v->{});assertNull(a.get(id));assertSame(second,b.get(id).get("water_shield"));}
	@Test void entriesAreLiveAndSameSpellReplaces(){var r=new AreaRegistry<Integer>();var id=UUID.randomUUID();var entries=r.entries(id);entries.put("a",1);entries.put("a",2);assertSame(entries,r.entries(id));assertEquals(2,r.get(id).get("a"));}
	@Test void pruneRemovesEmptyOwners(){var r=new AreaRegistry<Integer>();var id=UUID.randomUUID();r.entries(id).put("a",1);r.prune(v->true);assertNull(r.get(id));}
	@Test void clearRemovesBeforeDiscardException(){var r=new AreaRegistry<Integer>();var id=UUID.randomUUID();r.entries(id).put("a",1);assertThrows(IllegalStateException.class,()->r.clear(id,v->{throw new IllegalStateException();}));assertNull(r.get(id));}
	@Test void clearAllExceptionPreservesIndex(){var r=new AreaRegistry<Integer>();var id=UUID.randomUUID();r.entries(id).put("a",1);assertThrows(IllegalStateException.class,()->r.clearAll(v->{throw new IllegalStateException();}));assertEquals(1,r.get(id).get("a"));}
	@Test void clearAllDiscardsThenClears(){var r=new AreaRegistry<Integer>();var id=UUID.randomUUID();r.entries(id).put("a",1);var values=new ArrayList<Integer>();r.clearAll(v->{assertNotNull(r.get(id));values.add(v);});assertNull(r.get(id));assertEquals(List.of(1),values);}
}
