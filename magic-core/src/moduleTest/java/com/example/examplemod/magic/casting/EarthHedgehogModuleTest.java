package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_hedgehog.*;
class EarthHedgehogModuleTest {
	private static class World implements EarthHedgehogTick.World {
		List<String> calls=new ArrayList<>();String reject="";
		boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean statePresent(){return pass("state");}public boolean ownerPresent(){return pass("owner");}public boolean alive(){return pass("alive");}public boolean sameLevel(){return pass("level");}public boolean expired(){return !pass("expiry");}public boolean adopt(){return pass("adopt");}public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public boolean begin(){return pass("begin");}public void erupt(){calls.add("erupt");}
	}
	@Test void invalidStateAndOwnerShortCircuitInOriginalOrder(){var checks=List.of("state","owner","alive","level","expiry","adopt");for(int i=0;i<checks.size();i++){var w=new World();w.reject=checks.get(i);EarthHedgehogTick.tick(w);var expected=new ArrayList<>(checks.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}}
	@Test void processedTimeIsNotReplayed(){var s=new EarthHedgehogState(100,-1,Map.of());assertTrue(s.begin(0));assertFalse(s.begin(0));assertFalse(s.begin(-1));assertTrue(s.begin(2));assertEquals(2,s.processed());}
	@Test void missedEruptionTimeIsNotCaughtUp(){assertTrue(EarthHedgehogRules.erupt(4,2));assertFalse(EarthHedgehogRules.erupt(5,2));assertTrue(EarthHedgehogRules.active(5,2));assertFalse(EarthHedgehogRules.active(400,2));}
	@Test void rejectedDamageAttemptsStillUseTwoAttemptBudget(){var s=new EarthHedgehogState(100,-1,Map.of());for(int i=0;i<2;i++){int count=s.hits().getOrDefault("id",0);assertTrue(EarthHedgehogRules.mayHit(count));s.claim("id",count);}assertFalse(EarthHedgehogRules.mayHit(s.hits().get("id")));assertFalse(EarthHedgehogRules.mayHit(-1));}
	@Test void loadedStateAndInstancesDoNotShareVictimMap(){var source=new HashMap<String,Integer>();source.put("id",1);var a=new EarthHedgehogState(20,4,source);var b=new EarthHedgehogState(20,4,source);a.claim("id",1);assertEquals(1,b.hits().get("id"));assertEquals(1,source.get("id"));assertFalse(a.begin(4));assertEquals(20,a.start());}
	@Test void twelveOffsetsMatchOriginalFloatingPointFormulaExactly(){for(int i=0;i<12;i++){double angle=i*2.3999632297,radius=1+4.4*Math.sqrt((i+.5)/12);var point=EarthHedgehogRules.offset(i);assertEquals(Math.cos(angle)*radius,point.x());assertEquals(Math.sin(angle)*radius,point.z());assertTrue(Math.hypot(point.x(),point.z())<=6);}}
	@Test void processingIsClaimedBeforeWorldEffectsAndExceptionDoesNotDiscard(){var s=new EarthHedgehogState(0,-1,Map.of());var w=new World(){public boolean begin(){return s.begin(2);}public void erupt(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->EarthHedgehogTick.tick(w));assertEquals(2,s.processed());assertFalse(w.calls.contains("remove"));}
	@Test void sameProcessedFrameSkipsWorldQuery(){var w=new World();w.reject="begin";EarthHedgehogTick.tick(w);assertFalse(w.calls.contains("erupt"));assertFalse(w.calls.contains("discard"));}
}
