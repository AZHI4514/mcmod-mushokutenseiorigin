package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.cumulonimbus.*;
class CumulonimbusModuleTest {
	private static class World implements CumulonimbusTick.World {
		final List<String> calls=new ArrayList<>();String reject="";
		boolean pass(String name){calls.add(name);return !reject.equals(name);}
		public boolean statePresent(){return pass("state");}public boolean expired(){return !pass("expiry");}public boolean ownerPresent(){return pass("owner");}public boolean alive(){return pass("alive");}public boolean sameLevel(){return pass("level");}public boolean adopt(){return pass("adopt");}
		public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public boolean refreshNeeded(){return pass("refresh?");}public void refreshExposure(){calls.add("refresh");}public boolean strikeReady(){return pass("ready");}public void beginStrike(){calls.add("begin");}public boolean selectStrike(){return pass("select");}public void hitTargets(){calls.add("hit");}public void burst(){calls.add("burst");}
	}
	@Test void stateAndExpiryPrecedeOwnerValidation(){var checks=List.of("state","expiry","owner","alive","level","adopt");for(int i=0;i<checks.size();i++){var w=new World();w.reject=checks.get(i);CumulonimbusTick.tick(w);var expected=new ArrayList<>(checks.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}}
	@Test void refreshPrecedesSchedulingAndHitsPrecedeBurst(){var w=new World();CumulonimbusTick.tick(w);assertEquals(List.of("state","expiry","owner","alive","level","adopt","refresh?","refresh","ready","begin","select","hit","burst"),w.calls);}
	@Test void notReadyStillRefreshesButDoesNotSchedule(){var w=new World();w.reject="ready";CumulonimbusTick.tick(w);assertTrue(w.calls.contains("refresh"));assertFalse(w.calls.contains("begin"));}
	@Test void absentStrikeConsumesScheduleButNoHitOrBurst(){var w=new World();w.reject="select";CumulonimbusTick.tick(w);assertTrue(w.calls.contains("begin"));assertFalse(w.calls.contains("hit"));assertFalse(w.calls.contains("burst"));}
	@Test void hitFailureDoesNotUndoScheduleOrEmitBurst(){var w=new World(){public void hitTargets(){super.hitTargets();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CumulonimbusTick.tick(w));assertTrue(w.calls.contains("begin"));assertFalse(w.calls.contains("burst"));}
	@Test void victimBoundaryPrunedAfterSchedulingAndInstancesAreIsolated(){var a=new CumulonimbusState(1200,0,Map.of("expired",100L,"live",101L));var b=new CumulonimbusState(1200,0,Map.of());a.beginStrike(100);assertEquals(140,a.next());assertFalse(a.victims().containsKey("expired"));assertTrue(a.victims().containsKey("live"));a.claim("new",100);assertEquals(180L,a.victims().get("new"));assertTrue(b.victims().isEmpty());assertEquals(0,b.next());}
	@Test void refreshAndStrikeBoundariesMatchOriginalSchedule(){for(long now=0;now<1500;now++)for(int age:new int[]{0,1,10,99}){assertEquals(age%10==0||now>=400,CumulonimbusRules.refresh(age,now,400));assertEquals(now<1200&&now>=400,CumulonimbusRules.strike(now,400,1200));}}
	@Test void expiryShorteningDoesNotChangeNextOrVictims(){var s=new CumulonimbusState(1200,100,Map.of("id",200L));s.shorten(1100,200);assertEquals(1100,s.expiry());assertEquals(100,s.next());assertEquals(Map.of("id",200L),s.victims());}
	@Test void radiusAndOldLoadDefaultsRemainUsable(){assertTrue(CumulonimbusRules.inside(24,0));assertFalse(CumulonimbusRules.inside(24,.001));assertTrue(CumulonimbusRules.inside(Double.NaN,0));var s=new CumulonimbusState(0,Long.MAX_VALUE,Map.of());assertFalse(CumulonimbusRules.strike(0,s.next(),s.expiry()));}
}
