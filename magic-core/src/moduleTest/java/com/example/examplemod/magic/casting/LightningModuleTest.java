package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.lightning.*;
class LightningModuleTest {
	private static class Target implements LightningImpact.Target {
		List<String> calls=new ArrayList<>();String reject="";boolean self;
		boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean inside(){return pass("inside");}public boolean skyVisible(){return pass("sky");}public boolean owner(){calls.add("owner");return self;}public boolean allowed(){return pass("allowed");}public boolean visible(){return pass("visible");}public boolean hurt(){return pass("hurt");}public void control(){calls.add("control");}public void afterHit(){calls.add("hook");}
	}
	@Test void damageAndControlFollowSkyPermissionVisibility(){var t=new Target();LightningImpact.apply(t);assertEquals(List.of("inside","sky","owner","allowed","visible","hurt","control","hook"),t.calls);}
	@Test void ownerBypassesOnlyPermission(){var t=new Target();t.self=true;t.reject="allowed";LightningImpact.apply(t);assertFalse(t.calls.contains("allowed"));assertTrue(t.calls.contains("sky"));assertTrue(t.calls.contains("visible"));assertTrue(t.calls.contains("hurt"));}
	@Test void lazyRejectionsHaveNoControlOrHooks(){for(String key:List.of("inside","sky","allowed","visible","hurt")){var t=new Target();t.reject=key;LightningImpact.apply(t);assertFalse(t.calls.contains("control"));assertFalse(t.calls.contains("hook"));assertEquals(key,t.calls.getLast());}}
	@Test void controlExceptionDoesNotNotifyHit(){var t=new Target(){public void control(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->LightningImpact.apply(t));assertTrue(t.calls.contains("hurt"));assertFalse(t.calls.contains("hook"));}
	@Test void lockAndWarningRemainOnOriginalPreparationTicks(){for(int remaining=0;remaining<1000;remaining++){assertEquals(remaining==15,LightningRules.lockNow(remaining));assertEquals(remaining%3==0,LightningRules.warn(remaining));}}
	@Test void pointSamplesAndRangeKeepExactBoundaries(){assertEquals(1,LightningRules.steps(0));assertEquals(1,LightningRules.steps(.1));assertEquals(65,LightningRules.steps(64.001));assertTrue(LightningRules.inRange(4096.01));assertFalse(LightningRules.inRange(Math.nextUp(4096.01)));assertFalse(LightningRules.inRange(Double.NaN));}
	@Test void cylinderUsesHorizontalInclusiveRadius(){assertTrue(LightningRules.inside(2,0));assertFalse(LightningRules.inside(2,.001));assertTrue(LightningRules.inside(Double.NaN,0));}
}
