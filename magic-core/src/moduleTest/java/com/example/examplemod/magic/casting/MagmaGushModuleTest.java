package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.magma_gush.*;
class MagmaGushModuleTest {
	private static class Target implements MagmaGushHit.Target {
		List<String> calls=new ArrayList<>();boolean self;String reject="";boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean inside(){return pass("inside");}public boolean owner(){calls.add("owner");return self;}public boolean allowed(){return pass("allowed");}public boolean visible(){return pass("visible");}public boolean claim(){return pass("claim");}public boolean hurt(){return pass("hurt");}public void selfEffects(){calls.add("self");}public void otherEffects(){calls.add("other");}public void afterHit(){calls.add("hook");}
	}
	@Test void exactlyOneEruptionAndEightResidualPulses(){int big=0,small=0;for(int age=-1;age<500;age++){float damage=MagmaGushRules.damage(age);if(damage==10)big++;if(damage==2)small++;if(age>160)assertEquals(0,damage);}assertEquals(1,big);assertEquals(8,small);assertEquals(2,MagmaGushRules.damage(160));}
	@Test void bodyCenterCylinderKeepsVerticalAndHorizontalBoundaries(){assertTrue(MagmaGushRules.contains(4,0,0));assertTrue(MagmaGushRules.contains(0,5,4));assertFalse(MagmaGushRules.contains(4,.1,.001));assertFalse(MagmaGushRules.contains(0,-.001,0));assertFalse(MagmaGushRules.contains(0,5.001,0));}
	@Test void failedDamageStillClaimsCadenceBeforeRefusal(){var t=new Target();t.reject="hurt";MagmaGushHit.apply(t);assertEquals(List.of("inside","owner","allowed","visible","claim","hurt"),t.calls);}
	@Test void selfHitBypassesPermissionAndUsesSelfEffectsOnly(){var t=new Target();t.self=true;MagmaGushHit.apply(t);assertEquals(List.of("inside","owner","visible","claim","hurt","owner","self","hook"),t.calls);}
	@Test void foreignHitAppliesNormalEffectsBeforeHook(){var t=new Target();MagmaGushHit.apply(t);assertEquals("other",t.calls.get(t.calls.size()-2));assertEquals("hook",t.calls.getLast());}
	@Test void effectsFailureCannotUndoDamageOrNotifyHook(){var t=new Target(){public void otherEffects(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->MagmaGushHit.apply(t));assertTrue(t.calls.contains("hurt"));assertFalse(t.calls.contains("hook"));}
	@Test void processedStateClaimsEveryFrameBeforeDamageAndDoesNotReplay(){var s=new MagmaGushState(100,-1);assertTrue(s.begin(0));assertFalse(s.begin(0));assertTrue(s.begin(1));assertEquals(0,MagmaGushRules.damage(1));assertEquals(1,s.processed());assertTrue(s.begin(20));assertEquals(100,s.start());var loaded=new MagmaGushState(Long.MIN_VALUE/2,160);assertFalse(loaded.begin(160));}
	private static class World implements MagmaGushTick.World {
		List<String> calls=new ArrayList<>();String reject="";float damage=2;boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean statePresent(){return pass("state");}public boolean ownerPresent(){return pass("owner");}public boolean alive(){return pass("alive");}public boolean sameLevel(){return pass("level");}public boolean expired(){return !pass("expiry");}public boolean adopt(){return pass("adopt");}public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public boolean begin(){return pass("begin");}public float damage(){calls.add("damage");return damage;}public void hitTargets(float value){assertEquals(damage,value);calls.add("hit");}
	}
	@Test void tickInvalidationPreservesRemoveBeforeDiscard(){var order=List.of("state","owner","alive","level","expiry","adopt");for(int i=0;i<order.size();i++){var w=new World();w.reject=order.get(i);MagmaGushTick.tick(w);var expected=new ArrayList<>(order.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}}
	@Test void zeroDamageFrameIsProcessedButDoesNotQueryTargets(){var w=new World();w.damage=0;MagmaGushTick.tick(w);assertTrue(w.calls.contains("begin"));assertTrue(w.calls.contains("damage"));assertFalse(w.calls.contains("hit"));}
}
