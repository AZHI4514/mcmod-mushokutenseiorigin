package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import com.example.examplemod.magic.spell.wind.wind_slice.WindSliceHit;
class WindSliceHitModuleTest {
	private static WindSliceHit.Targets<UUID> targets(UUID direct,List<UUID> candidates,List<UUID> damage){
		return new WindSliceHit.Targets<UUID>(){
			public Iterable<UUID> candidates(){return candidates;}
			public UUID identity(UUID target){return target;}
			public boolean direct(UUID target){return target.equals(direct);}
			public void damage(UUID target){damage.add(target);}
		};
	}
	@Test void preclaimedDirectVictimStillDamagesAndAreaVictimsShareFlightHistory(){
		var a=UUID.randomUUID();var b=UUID.randomUUID();var history=new ImpactHistory();var damage=new ArrayList<UUID>();
		assertTrue(history.claim(a));WindSliceHit.apply(history,targets(a,List.of(b,a),damage));
		assertEquals(List.of(b,a),damage);assertFalse(PiercingFlightRules.contactDue(1,2,history.contains(b)));assertEquals(Set.of(a,b),history.snapshot());
	}
	@Test void overlappingLaterContactSkipsPreviousAreaVictimsButAllowsNewVictims(){
		var a=UUID.randomUUID();var b=UUID.randomUUID();var c=UUID.randomUUID();var d=UUID.randomUUID();var history=new ImpactHistory();var damage=new ArrayList<UUID>();
		history.claim(a);WindSliceHit.apply(history,targets(a,List.of(a,b),damage));
		var restored=new ImpactHistory(history.snapshot());assertTrue(restored.claim(c));WindSliceHit.apply(restored,targets(c,List.of(b,c,d),damage));
		assertEquals(List.of(a,b,c,d),damage);assertEquals(Set.of(a,b,c,d),restored.snapshot());
	}
	@Test void failedDamageAttemptClaimsBeforeExceptionAndStopsRemainingCandidates(){
		var a=UUID.randomUUID();var b=UUID.randomUUID();var c=UUID.randomUUID();var history=new ImpactHistory();history.claim(a);
		assertThrows(IllegalStateException.class,()->WindSliceHit.apply(history,new WindSliceHit.Targets<UUID>(){
			public Iterable<UUID> candidates(){return List.of(b,c);}
			public UUID identity(UUID target){return target;}
			public boolean direct(UUID target){return false;}
			public void damage(UUID target){assertTrue(history.contains(target));throw new IllegalStateException("hurt hook");}
		}));assertTrue(history.contains(b));assertFalse(history.contains(c));
	}
	@Test void radiusIsInclusiveAndDoesNotAcceptNanOrOutsideSphere(){
		assertTrue(WindSliceHit.inRadius(9));assertTrue(WindSliceHit.inRadius(0));assertFalse(WindSliceHit.inRadius(9.000001));assertFalse(WindSliceHit.inRadius(Double.NaN));assertFalse(WindSliceHit.inRadius(Double.POSITIVE_INFINITY));
	}
	@Test void expiredSavedBudgetOrEarlierAreaClaimPreventsAnotherDirectTrigger(){
		assertFalse(PiercingFlightRules.expired(119,120));assertTrue(PiercingFlightRules.expired(120,120));assertTrue(PiercingFlightRules.expired(101,100));
		assertFalse(PiercingFlightRules.candidate(1,1,2,false));assertFalse(PiercingFlightRules.candidate(1,2,1,false));assertFalse(PiercingFlightRules.contactDue(1,2,true));
	}
	@Test void frozenLegacySequenceMatchesRandomOverlappingContactsAndRestoration(){
		var random=new Random(12112026);
		for(int sample=0;sample<2000;sample++){
			var ids=new ArrayList<UUID>();for(int n=0;n<12;n++)ids.add(new UUID(sample,n));
			var legacy=new HashSet<UUID>();var current=new ImpactHistory();
			var oldDamage=new ArrayList<UUID>();var newDamage=new ArrayList<UUID>();
			for(int contact=0;contact<10;contact++){
				var direct=ids.get(random.nextInt(ids.size()));var candidates=new ArrayList<UUID>();
				for(var id:ids)if(random.nextBoolean())candidates.add(id);Collections.shuffle(candidates,random);
				boolean oldDue=!legacy.contains(direct),newDue=PiercingFlightRules.contactDue(1,2,current.contains(direct));assertEquals(oldDue,newDue);
				if(oldDue){legacy.add(direct);for(var id:candidates)if(id.equals(direct)||legacy.add(id))oldDamage.add(id);}
				if(newDue){current.claim(direct);WindSliceHit.apply(current,targets(direct,candidates,newDamage));}
				assertEquals(oldDamage,newDamage);assertEquals(legacy,current.snapshot());
				if(contact==4)current=new ImpactHistory(current.snapshot());
			}
		}
	}

}
