package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.feature.input.InputSessions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
class InputSessionsModuleTest {
	@Test void serversWithSamePlayerAreIndependent() {
		var id=UUID.randomUUID(); var a=new InputSessions<String>();var b=new InputSessions<String>();
		assertTrue(a.get(id).accept(1));assertTrue(b.get(id).accept(1));
		a.get(id).blocked=false;a.get(id).dimension="nether";a.clear();
		assertTrue(b.get(id).blocked);assertNull(b.get(id).dimension);assertFalse(b.get(id).accept(1));
	}
	@Test void limitAndSequenceCommitPreserveOrder() {
		var session=new InputSessions<String>().get(UUID.randomUUID());
		assertFalse(session.accept(-1));for(int i=0;i<8;i++)assertTrue(session.accept(i));
		assertFalse(session.accept(8));session.age++;assertFalse(session.accept(8));assertTrue(session.accept(9));
	}
	@Test void lifecycleResetAndRemovalStartBlocked() {
		var store=new InputSessions<String>();var id=UUID.randomUUID();var old=store.get(id);
		old.blocked=false;old.accept(100);store.reset(id);assertNotSame(old,store.get(id));
		assertTrue(store.get(id).blocked);assertTrue(store.get(id).accept(0));
		store.remove(id);assertTrue(store.get(id).accept(0));
	}
}
