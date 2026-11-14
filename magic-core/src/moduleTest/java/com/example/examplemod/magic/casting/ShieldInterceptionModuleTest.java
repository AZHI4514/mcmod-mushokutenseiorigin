package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ShieldInterception;
class ShieldInterceptionModuleTest {
	static class Contact implements ShieldInterception.Contact {
		final List<String> events=new ArrayList<>(); boolean clear=true, spear;
		public boolean terrainClear(){events.add("terrain");return clear;}
		public float incomingDamage(){events.add("damage");return 7;}
		public void absorb(float damage){assertEquals(7,damage);events.add("absorb");}
		public boolean trident(){events.add("type");return spear;}
		public void rebound(){events.add("rebound");}
		public void discard(){events.add("discard");}
	}
	@Test void terrainObstructionDoesNotComputeOrApplyDamage(){var c=new Contact();c.clear=false;assertFalse(ShieldInterception.apply(c));assertEquals(List.of("terrain"),c.events);}
	@Test void ordinaryProjectileAbsorbsBeforeDiscard(){var c=new Contact();assertTrue(ShieldInterception.apply(c));assertEquals(List.of("terrain","damage","absorb","type","discard"),c.events);}
	@Test void tridentReboundsAfterAbsorption(){var c=new Contact();c.spear=true;assertTrue(ShieldInterception.apply(c));assertEquals(List.of("terrain","damage","absorb","type","rebound"),c.events);}
	@Test void typeIsReadAfterAbsorption(){var c=new Contact(){public void absorb(float d){super.absorb(d);spear=true;}};assertTrue(ShieldInterception.apply(c));assertEquals("rebound",c.events.getLast());}
	@Test void absorptionFailureDoesNotInventProjectileCleanup(){var failure=new IllegalStateException();var c=new Contact(){public void absorb(float d){super.absorb(d);throw failure;}};assertSame(failure,assertThrows(IllegalStateException.class,()->ShieldInterception.apply(c)));assertEquals(List.of("terrain","damage","absorb"),c.events);}
}
