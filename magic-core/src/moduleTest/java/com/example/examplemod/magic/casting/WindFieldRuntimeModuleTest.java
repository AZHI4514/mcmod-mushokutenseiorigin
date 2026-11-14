package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.*;
class WindFieldRuntimeModuleTest {
	private static class World implements WindFieldTick.World {
		List<String> calls=new ArrayList<>();String reject="";boolean suppress;
		boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean statePresent(){return pass("state");}public boolean expired(){return !pass("expiry");}public boolean ownerPresent(){return pass("owner");}public boolean alive(){return pass("alive");}public boolean sameLevel(){return pass("level");}public boolean adopt(){return pass("adopt");}public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public boolean move(){return pass("move");}public boolean pulse(){return pass("pulse");}public boolean suppressed(){calls.add("suppress");return suppress;}public void contacts(boolean pulse){calls.add("contacts:"+pulse);}public void interact(boolean pulse){calls.add("interact:"+pulse);}
	}
	@Test void expiryPrecedesOwnerAndInvalidationRemovesBeforeDiscard(){var checks=List.of("state","expiry","owner","alive","level","adopt");for(int i=0;i<checks.size();i++){var w=new World();w.reject=checks.get(i);WindFieldTick.tick(w);var expected=new ArrayList<>(checks.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}}
	@Test void unloadedMovementDiscardsWithoutRemovingWeakState(){var w=new World();w.reject="move";WindFieldTick.tick(w);assertEquals("discard",w.calls.getLast());assertFalse(w.calls.contains("remove"));assertFalse(w.calls.contains("pulse"));}
	@Test void pulseIsConsumedBeforeOverlapSuppression(){var w=new World();w.suppress=true;WindFieldTick.tick(w);assertTrue(w.calls.contains("pulse"));assertFalse(w.calls.contains("contacts:true"));assertFalse(w.calls.contains("interact:true"));}
	@Test void contactsPrecedeOtherFieldInteractions(){var w=new World();WindFieldTick.tick(w);assertEquals("contacts:true",w.calls.get(w.calls.size()-2));assertEquals("interact:true",w.calls.getLast());}
	@Test void nonPulseStillRunsContactsAndInteractionsWithFalse(){var w=new World();w.reject="pulse";WindFieldTick.tick(w);assertEquals("contacts:false",w.calls.get(w.calls.size()-2));assertEquals("interact:false",w.calls.getLast());}
	@Test void contactExceptionDoesNotRunFieldInteraction(){var w=new World(){public void contacts(boolean pulse){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WindFieldTick.tick(w));assertFalse(w.calls.contains("interact:true"));}
	@Test void pulseSchedulePreservesFirstFrameAndTwentyTickCadence(){var s=new WindFieldState(100,500,100);assertTrue(s.pulse(100));assertEquals(120,s.next());assertFalse(s.pulse(119));assertTrue(s.pulse(120));assertEquals(140,s.next());assertEquals(100,s.start());assertEquals(500,s.expiry());}
	@Test void impulsesKeepPullSwirlOutwardAndPlayerVerticalCap(){var pull=WindFieldRules.impulse(2,0,0,.34,0,true,false,true);assertEquals(-.12,pull.x());assertEquals(.35,pull.y());var mob=WindFieldRules.impulse(2,0,0,.34,0,true,false,false);assertEquals(.42,mob.y(),1E-15);var swirl=WindFieldRules.impulse(2,0,0,0,0,false,true,false);assertEquals(.08,swirl.z());assertEquals(.04,swirl.y());var push=WindFieldRules.impulse(2,0,0,0,0,false,false,false);assertEquals(.2,push.x());}
	@Test void zeroOffsetAndSmallVectorMatchOriginalNormalizationThreshold(){var v=WindFieldRules.impulse(.000001,0,1,2,3,true,false,false);assertEquals(1,v.x());assertEquals(2.08,v.y());assertEquals(3,v.z());assertTrue(WindFieldRules.contains(4,0,0,4,5));assertFalse(WindFieldRules.contains(0,5.01,0,4,5));}
}
