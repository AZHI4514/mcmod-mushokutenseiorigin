package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameHit;
import com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameFlight;
class ExodusFlameHitModuleTest {
	static class Target implements ExodusFlameHit.Target {
		final List<String> events=new ArrayList<>();boolean seen,self,allowed=true,damage=true;double distance=16;
		public boolean previouslyHit(){events.add("history");return seen;}
		public double distanceSquared(){events.add("distance");return distance;}
		public boolean visible(){events.add("visible");return true;}
		public boolean owner(){events.add("owner");return self;}
		public boolean allowed(){events.add("allowed");return allowed;}
		public void claim(){events.add("claim");seen=true;}
		public boolean hurt(){events.add("hurt");return damage;}
		public void selfEffect(){events.add("self");}
		public void otherEffect(){events.add("other");}
		public void afterHit(){events.add("after");}
	}
	@Test void foreignTargetClaimsBeforeDamageAndEffects(){var t=new Target();ExodusFlameHit.apply(t);assertEquals(List.of("history","distance","visible","owner","allowed","claim","hurt","owner","other","after"),t.events);}
	@Test void ownerBypassesPermissionAndUsesSelfEffect(){var t=new Target();t.self=true;t.allowed=false;ExodusFlameHit.apply(t);assertEquals(List.of("history","distance","visible","owner","claim","hurt","owner","self","after"),t.events);}
	@Test void rejectedDamageStillConsumesTargetForWholeProjectile(){var t=new Target();t.damage=false;ExodusFlameHit.apply(t);assertTrue(t.seen);assertEquals("hurt",t.events.getLast());t.events.clear();ExodusFlameHit.apply(t);assertEquals(List.of("history"),t.events);}
	@Test void outOfRangeAndDeniedPermissionDoNotClaim(){var t=new Target();t.distance=Math.nextUp(16.0);ExodusFlameHit.apply(t);assertEquals(List.of("history","distance"),t.events);t=new Target();t.allowed=false;ExodusFlameHit.apply(t);assertFalse(t.seen);assertEquals("allowed",t.events.getLast());}
	@Test void invisibleTargetStopsBeforePermissionAndClaim(){var t=new Target(){public boolean visible(){events.add("visible");return false;}};ExodusFlameHit.apply(t);assertEquals(List.of("history","distance","visible"),t.events);assertFalse(t.seen);}
	@Test void damageExceptionPreservesClaimAndStopsEffects(){var failure=new IllegalStateException();var t=new Target(){public boolean hurt(){super.hurt();throw failure;}};assertSame(failure,assertThrows(IllegalStateException.class,()->ExodusFlameHit.apply(t)));assertTrue(t.seen);assertEquals("hurt",t.events.getLast());}
	@Test void effectFailureDoesNotRunAfterHitOrUndoClaim(){var t=new Target(){public void otherEffect(){super.otherEffect();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ExodusFlameHit.apply(t));assertTrue(t.seen);assertEquals("other",t.events.getLast());}
	@Test void ownerIdentityIsReevaluatedAfterDamage(){var t=new Target(){public boolean hurt(){self=true;return super.hurt();}};ExodusFlameHit.apply(t);assertTrue(t.events.contains("allowed"));assertEquals(List.of("self","after"),t.events.subList(t.events.size()-2,t.events.size()));}
	@Test void restoredHistoryAndNewRejectedHitRemainSharedAcrossBursts(){
		var old=UUID.randomUUID();var fresh=UUID.randomUUID();var saved=Set.of(old);var history=new HashSet<>(saved);
		class SharedTarget extends Target {
			final UUID id;SharedTarget(UUID id){this.id=id;}
			public boolean previouslyHit(){return history.contains(id);}
			public void claim(){history.add(id);}
		}
		var restored=new SharedTarget(old);ExodusFlameHit.apply(restored);assertTrue(restored.events.isEmpty());
		var rejected=new SharedTarget(fresh);rejected.damage=false;ExodusFlameHit.apply(rejected);assertEquals("hurt",rejected.events.getLast());
		assertTrue(ExodusFlameFlight.skipContact(1,2,history.contains(fresh)));
		var reloaded=new HashSet<>(history);assertEquals(Set.of(old,fresh),reloaded);
		var again=new SharedTarget(fresh);ExodusFlameHit.apply(again);assertTrue(again.events.isEmpty());assertEquals(Set.of(old),saved);
	}

}
