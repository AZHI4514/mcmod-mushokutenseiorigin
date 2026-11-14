package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldRegistry;
class PhysicalShieldRegistryModuleTest {
	@Test void identicalOwnerIdsDoNotLeakBetweenServers(){var a=new PhysicalShieldRegistry<String>();var b=new PhysicalShieldRegistry<String>();var id=UUID.randomUUID();a.put(id,"first");b.put(id,"second");assertEquals("first",a.remove(id));assertEquals("second",b.get(id));assertTrue(a.values().isEmpty());}
	@Test void replacingAndRemovingOneOwnerKeepsOtherOwners(){var r=new PhysicalShieldRegistry<String>();var a=UUID.randomUUID();var b=UUID.randomUUID();r.put(a,"old");r.put(b,"other");r.put(a,"new");assertEquals("new",r.remove(a));assertNull(r.remove(a));assertEquals(List.of("other"),new ArrayList<>(r.values()));}
	@Test void freshServerHasNoOldRuntimeEntries(){var old=new PhysicalShieldRegistry<String>();old.put(UUID.randomUUID(),"shield");assertTrue(new PhysicalShieldRegistry<String>().values().isEmpty());}
}
