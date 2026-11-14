package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.*;
import com.example.examplemod.magic.spell.wind.violent_storm.ViolentStormPolicy;
class ViolentStormModuleTest {
	private static class Target implements WindFieldContact.Target {
		List<String> calls=new ArrayList<>();String reject="";boolean self,immune,excluded;
		boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean excluded(){calls.add("excluded");return excluded;}public boolean inside(){return pass("inside");}public boolean owner(){calls.add("owner");return self;}public boolean allowed(){return pass("allowed");}public boolean visible(){return pass("visible");}public void pulse(){calls.add("pulse");}public boolean immune(){calls.add("immune");return immune;}public boolean moves(){return pass("moves");}public void move(){calls.add("move");}
	}
	@Test void excludesOwnerBeforeReadingTargetGeometry(){var t=new Target();t.excluded=true;WindFieldContact.apply(true,t);assertEquals(List.of("excluded"),t.calls);}
	@Test void contactPulsePrecedesImmunityReadAndMovement(){var t=new Target();WindFieldContact.apply(true,t);assertEquals(List.of("excluded","inside","owner","allowed","visible","pulse","immune","moves","move"),t.calls);}
	@Test void immuneTargetsStillTakePulseDamage(){var t=new Target();t.immune=true;WindFieldContact.apply(true,t);assertTrue(t.calls.contains("pulse"));assertFalse(t.calls.contains("moves"));}
	@Test void noPulseStillReadsImmunityInOriginalPosition(){var t=new Target();t.reject="moves";WindFieldContact.apply(false,t);assertFalse(t.calls.contains("pulse"));assertEquals("moves",t.calls.getLast());assertTrue(t.calls.contains("immune"));}
	@Test void damageExceptionPreventsMotion(){var t=new Target(){public void pulse(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WindFieldContact.apply(true,t));assertFalse(t.calls.contains("immune"));}
	@Test void violentPolicyHasOwnGeometryOutwardMotionAndDispersal(){var p=new ViolentStormPolicy();assertEquals(16,p.radius());assertEquals(16,p.height());assertTrue(p.excludesOwner());assertTrue(p.disperses());assertTrue(p.violent());assertFalse(p.moving());assertTrue(p.movesTarget(true));assertFalse(p.movesTarget(false));var v=p.impulse(1,0,0,.5,0,true);assertEquals(.2,v.x());assertEquals(.5,v.y());}
}
