package com.example.examplemod.magic.casting;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.SweptProjectileHit;

class IntermediateHitModuleTest {
	static class Target implements SweptProjectileHit.Target {
		final List<String> events = new ArrayList<>(); boolean hurt = true; boolean knockback;
		public boolean hurt() { events.add("hurt"); return hurt; }
		public boolean hasKnockback() { events.add("check"); return knockback; }
		public void knockback() { events.add("knockback"); }
		public void effect() { events.add("effect"); }
		public void afterHit() { events.add("after"); }
	}
	@Test void successPreservesOrder() { var t = new Target(); t.knockback = true; SweptProjectileHit.apply(t); assertEquals(List.of("hurt","check","knockback","effect","after"), t.events); }
	@Test void rejectedHurtStops() { var t = new Target(); t.hurt = false; SweptProjectileHit.apply(t); assertEquals(List.of("hurt"), t.events); }
	@Test void noKnockbackStillChecksThenEffects() { var t = new Target(); SweptProjectileHit.apply(t); assertEquals(List.of("hurt","check","effect","after"), t.events); }
	@Test void effectExceptionStopsAfterHook() { var t = new Target(){ public void effect(){ events.add("effect"); throw new IllegalStateException(); } }; assertThrows(IllegalStateException.class, () -> SweptProjectileHit.apply(t)); assertEquals(List.of("hurt","check","effect"), t.events); }
	@Test void damageCanChangeKnockbackBeforeRead() { var t = new Target(){ public boolean hurt(){ knockback = true; return super.hurt(); } }; SweptProjectileHit.apply(t); assertEquals(List.of("hurt","check","knockback","effect","after"), t.events); }
	@Test void knockbackExceptionPreventsEffects() { var t = new Target(){ public void knockback(){ super.knockback(); throw new IllegalStateException(); } }; t.knockback = true; assertThrows(IllegalStateException.class, () -> SweptProjectileHit.apply(t)); assertEquals(List.of("hurt","check","knockback"), t.events); }
	@Test void damageExceptionPreventsAllLaterActions() { var t = new Target(){ public boolean hurt(){ super.hurt(); throw new IllegalStateException(); } }; assertThrows(IllegalStateException.class, () -> SweptProjectileHit.apply(t)); assertEquals(List.of("hurt"), t.events); }
	@Test void hookExceptionDoesNotRepeatEarlierEffects() { var t = new Target(){ public void afterHit(){ super.afterHit(); throw new IllegalStateException(); } }; assertThrows(IllegalStateException.class, () -> SweptProjectileHit.apply(t)); assertEquals(List.of("hurt","check","effect","after"), t.events); }
}
