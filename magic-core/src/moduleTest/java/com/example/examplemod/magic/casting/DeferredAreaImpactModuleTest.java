package com.example.examplemod.magic.casting;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.DeferredAreaImpact;

class DeferredAreaImpactModuleTest {
	static class World implements DeferredAreaImpact.World<Integer> {
		final List<String> events = new ArrayList<>();
		List<Integer> targets = List.of(1, 2);
		boolean affects = true, wet = true, hurt = true;
		public Iterable<Integer> targets() { events.add("targets"); return targets; }
		public boolean affects(Integer t) { events.add("affects" + t); return affects; }
		public boolean wet(Integer t) { events.add("wet" + t); return wet; }
		public boolean hurt(Integer t) { events.add("hurt" + t); return hurt; }
		public boolean applyEffects(Integer t, boolean wasWet) { events.add("effect" + t + ":" + wasWet); return wasWet; }
		public void afterHit(Integer t) { events.add("hook" + t); }
		public void encase(Integer t) { events.add("encase" + t); }
		public void burst() { events.add("burst"); }
	}
	@Test void allHitsBeforeAnyTerrain() {
		var w = new World(); DeferredAreaImpact.apply(w);
		assertEquals(List.of("targets","affects1","wet1","hurt1","effect1:true","hook1","affects2","wet2","hurt2","effect2:true","hook2","encase1","encase2","burst"), w.events);
	}
	@Test void rejectedSelectionNeverReadsWetOrHurts() {
		var w = new World(); w.affects = false; DeferredAreaImpact.apply(w);
		assertEquals(List.of("targets","affects1","affects2","burst"), w.events);
	}
	@Test void rejectedDamageNeverConsumesWetOrFiresHook() {
		var w = new World(); w.hurt = false; DeferredAreaImpact.apply(w);
		assertEquals(List.of("targets","affects1","wet1","hurt1","affects2","wet2","hurt2","burst"), w.events);
	}
	@Test void damageMutationDoesNotReplaceWetSnapshot() {
		var w = new World() { public boolean hurt(Integer t) { wet = false; return super.hurt(t); } };
		DeferredAreaImpact.apply(w);
		assertTrue(w.events.contains("effect1:true")); assertTrue(w.events.contains("effect2:false"));
		assertTrue(w.events.contains("encase1")); assertFalse(w.events.contains("encase2"));
	}
	@Test void effectsDecideQueueIndependentlyOfSnapshot() {
		var w = new World() { public boolean applyEffects(Integer t, boolean wet) { super.applyEffects(t, wet); return false; } };
		DeferredAreaImpact.apply(w); assertFalse(w.events.stream().anyMatch(s -> s.startsWith("encase"))); assertEquals("burst", w.events.getLast());
	}
	@Test void emptyTargetsStillBurst() {
		var w = new World(); w.targets = List.of(); DeferredAreaImpact.apply(w); assertEquals(List.of("targets","burst"), w.events);
	}
	@Test void hookExceptionDoesNotFlushQueuedTerrain() {
		var w = new World() { public void afterHit(Integer t) { super.afterHit(t); throw new IllegalStateException(); } };
		assertThrows(IllegalStateException.class, () -> DeferredAreaImpact.apply(w));
		assertEquals(List.of("targets","affects1","wet1","hurt1","effect1:true","hook1"), w.events);
	}
	@Test void terrainFailureKeepsPriorPlacementAndStopsBurst() {
		var w = new World() { public void encase(Integer t) { super.encase(t); if (t == 2) throw new IllegalStateException(); } };
		assertThrows(IllegalStateException.class, () -> DeferredAreaImpact.apply(w));
		assertEquals(List.of("encase1","encase2"), w.events.subList(w.events.size() - 2, w.events.size())); assertFalse(w.events.contains("burst"));
	}
	@Test void effectFailureDoesNotRunHookOrTerrain() {
		var w = new World() { public boolean applyEffects(Integer t, boolean wet) { super.applyEffects(t, wet); throw new IllegalStateException(); } };
		assertThrows(IllegalStateException.class, () -> DeferredAreaImpact.apply(w));
		assertEquals("effect1:true", w.events.getLast()); assertFalse(w.events.contains("encase1"));
	}
}
