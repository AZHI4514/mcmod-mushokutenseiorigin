package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.burning_place.BurningPlaceContact;
class BurningPlaceModuleTest {
	static class Target implements BurningPlaceContact.Target {
		final List<String> events=new ArrayList<>();boolean claimed,contact=true,allowed=true,visible=true,hurt=true;
		public boolean alreadyContacted(){events.add("history");return claimed;}
		public boolean touches(){events.add("touches");return contact;}
		public boolean allowed(){events.add("allowed");return allowed;}
		public boolean visible(){events.add("visible");return visible;}
		public void claim(){events.add("claim");claimed=true;}
		public boolean hurt(){events.add("hurt");return hurt;}
		public void effects(){events.add("effects");}
		public void afterHit(){events.add("hook");}
	}
	@Test void successfulOrder(){var t=new Target();BurningPlaceContact.apply(t);assertEquals(List.of("history","touches","allowed","visible","claim","hurt","effects","hook"),t.events);}
	@Test void historySkipsGeometryAndPermission(){var t=new Target();t.claimed=true;BurningPlaceContact.apply(t);assertEquals(List.of("history"),t.events);}
	@Test void noTouchSkipsPermission(){var t=new Target();t.contact=false;BurningPlaceContact.apply(t);assertEquals(List.of("history","touches"),t.events);}
	@Test void deniedPvpSkipsVisibility(){var t=new Target();t.allowed=false;BurningPlaceContact.apply(t);assertEquals(List.of("history","touches","allowed"),t.events);}
	@Test void obstructionDoesNotConsumeAttempt(){var t=new Target();t.visible=false;BurningPlaceContact.apply(t);assertFalse(t.claimed);assertEquals("visible",t.events.getLast());}
	@Test void rejectedDamageStillConsumesAttempt(){var t=new Target();t.hurt=false;BurningPlaceContact.apply(t);assertTrue(t.claimed);assertEquals("hurt",t.events.getLast());}
	@Test void damageExceptionPreservesClaim(){var t=new Target(){public boolean hurt(){super.hurt();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->BurningPlaceContact.apply(t));assertTrue(t.claimed);assertFalse(t.events.contains("effects"));}
	@Test void effectExceptionSuppressesHook(){var t=new Target(){public void effects(){super.effects();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->BurningPlaceContact.apply(t));assertEquals("effects",t.events.getLast());}
}
