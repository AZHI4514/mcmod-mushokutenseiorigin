package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.mixed.quagmire.*;
class QuagmireModuleTest {
	private static class Target implements QuagmireContact.Target {
		List<String> calls=new ArrayList<>();boolean self,boss;String reject="";
		boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean inside(){return pass("inside");}public boolean owner(){calls.add("owner");return self;}public boolean allowed(){return pass("allowed");}public boolean visible(){return pass("visible");}public boolean immune(){calls.add("immune");return boss;}public void slow(boolean boss){calls.add("slow:"+boss);}public void hold(){calls.add("hold");}
	}
	@Test void surfaceBoundsRemainInclusiveAndAirborneTargetsExcluded(){assertTrue(QuagmireRules.contains(6,-.2,0));assertTrue(QuagmireRules.contains(0,.65,6));assertFalse(QuagmireRules.contains(6.001,0,0));assertFalse(QuagmireRules.contains(0,Math.nextUp(.65),0));assertFalse(QuagmireRules.contains(0,Math.nextDown(-.2),0));assertFalse(QuagmireRules.contains(Double.NaN,0,0));}
	@Test void upwardCapDoesNotPullFallingTargets(){assertEquals(-.5,QuagmireRules.jumpVelocity(-.5));assertEquals(0,QuagmireRules.jumpVelocity(0));assertEquals(.1,QuagmireRules.jumpVelocity(.1));assertEquals(.115,QuagmireRules.jumpVelocity(.42));assertTrue(Double.isNaN(QuagmireRules.jumpVelocity(Double.NaN)));}
	@Test void contactSlowPrecedesHold(){var t=new Target();QuagmireContact.apply(t);assertEquals(List.of("inside","owner","allowed","visible","immune","slow:false","hold"),t.calls);}
	@Test void bossesReceiveOnlyWeakerSlow(){var t=new Target();t.boss=true;QuagmireContact.apply(t);assertEquals("slow:true",t.calls.getLast());assertFalse(t.calls.contains("hold"));}
	@Test void ownerBypassesPermissionButNotVisibility(){var t=new Target();t.self=true;t.reject="visible";QuagmireContact.apply(t);assertFalse(t.calls.contains("allowed"));assertFalse(t.calls.contains("immune"));}
	@Test void slowFailureDoesNotApplyHold(){var t=new Target(){public void slow(boolean boss){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->QuagmireContact.apply(t));assertFalse(t.calls.contains("hold"));}
	private static class World implements QuagmireTick.World {
		List<String> calls=new ArrayList<>();String reject="";boolean pass(String s){calls.add(s);return !reject.equals(s);}
		public boolean ownerPresent(){return pass("owner");}public boolean alive(){return pass("alive");}public boolean sameLevel(){return pass("level");}public boolean expired(){return !pass("expiry");}public boolean adopt(){return pass("adopt");}public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public void contacts(){calls.add("contacts");}
	}
	@Test void expiryValidationAndStateRemovalOrderRemainExact(){var checks=List.of("owner","alive","level","expiry","adopt");for(int i=0;i<checks.size();i++){var w=new World();w.reject=checks.get(i);QuagmireTick.tick(w);var expected=new ArrayList<>(checks.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}}
	@Test void contactsRunOnlyAfterSuccessfulAdoption(){var w=new World();QuagmireTick.tick(w);assertEquals(List.of("owner","alive","level","expiry","adopt","contacts"),w.calls);}
}
