package com.example.examplemod.magic.casting;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.PreparationRegistry;
import com.example.examplemod.magic.domain.casting.CastingSession;
class PreparationRegistryModuleTest {
	@Test void samePlayerIsIsolatedAcrossServers(){var id=UUID.randomUUID();var a=new PreparationRegistry<Object>();var b=new PreparationRegistry<Object>();var x=new Object();var y=new Object();a.put(id,x);b.put(id,y);assertSame(x,a.get(id));assertSame(y,b.get(id));assertSame(x,a.remove(id));assertFalse(a.contains(id));assertSame(y,b.get(id));}
	@Test void replacingAndRemovingRetainsExactSessionIdentity(){var id=UUID.randomUUID();var r=new PreparationRegistry<Object>();var old=new Object();var next=new Object();r.put(id,old);r.put(id,next);assertSame(next,r.get(id));assertSame(next,r.remove(id));assertNull(r.remove(id));assertNull(r.get(id));}
	@Test void clearingOneRuntimeCannotClearAnotherAndRecreationIsEmpty(){var id=UUID.randomUUID();var a=new PreparationRegistry<Object>();var b=new PreparationRegistry<Object>();a.put(id,new Object());b.put(id,new Object());a.clear();assertFalse(a.contains(id));assertTrue(b.contains(id));assertNull(new PreparationRegistry<Object>().get(id));}
	@Test void cancellationUsesRemovedSessionWithoutCancellingOtherServer(){var id=UUID.randomUUID();var a=new PreparationRegistry<CastingSession>();var b=new PreparationRegistry<CastingSession>();a.put(id,new CastingSession(2));b.put(id,new CastingSession(2));var removed=a.remove(id);removed.cancel();for(int i=0;i<4;i++)assertNotEquals(CastingSession.Signal.READY,removed.tick(true).signal());var active=b.get(id);active.heartbeat();assertEquals(CastingSession.Signal.NONE,active.tick(true).signal());active.heartbeat();assertEquals(CastingSession.Signal.READY,active.tick(true).signal());}
}
