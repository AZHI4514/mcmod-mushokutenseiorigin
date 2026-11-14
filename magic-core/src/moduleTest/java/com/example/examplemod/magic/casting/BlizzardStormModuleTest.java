package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.blizzard_storm.*;
class BlizzardStormModuleTest {
	private static class World implements BlizzardStormTick.World {
		List<String> calls=new ArrayList<>();String rejected="";int age;
		boolean read(String key){calls.add(key);return !rejected.equals(key);}
		public boolean ownerPresent(){return read("present");}public boolean alive(){return read("alive");}public boolean removed(){return !read("removed");}public boolean sameLevel(){return read("level");}public int pulse(){calls.add("pulse");return age;}
		public void discard(){calls.add("discard");}public void follow(){calls.add("follow");}public boolean adopt(){return read("adopt");}public void hitTargets(){calls.add("hit");}public void advance(){calls.add("advance");age++;}
	}
	@Test void validatesBeforeFollowingAndAdoption(){for(String key:List.of("present","alive","removed","level")){var w=new World();w.rejected=key;BlizzardStormTick.tick(w);assertEquals("discard",w.calls.getLast());assertFalse(w.calls.contains("follow"));assertEquals(0,w.age);}}
	@Test void expiryDoesNotFollowOrAdopt(){var w=new World();w.age=80;BlizzardStormTick.tick(w);assertFalse(w.calls.contains("adopt"));assertEquals(80,w.age);assertEquals("discard",w.calls.getLast());}
	@Test void followPrecedesAdoptAndRefusalDoesNotAge(){var w=new World();w.rejected="adopt";BlizzardStormTick.tick(w);assertTrue(w.calls.indexOf("follow")<w.calls.indexOf("adopt"));assertFalse(w.calls.contains("hit"));assertEquals(0,w.age);}
	@Test void pulseZeroHitsImmediatelyAndEightyTicksYieldEightPulses(){var w=new World();int hits=0;for(int t=0;t<80;t++){w.calls.clear();BlizzardStormTick.tick(w);if(w.calls.contains("hit"))hits++;assertEquals(t+1,w.age);assertEquals("advance",w.calls.getLast());}assertEquals(8,hits);}
	@Test void hitExceptionKeepsPulse(){var w=new World(){public void hitTargets(){super.hitTargets();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->BlizzardStormTick.tick(w));assertEquals(0,w.age);assertFalse(w.calls.contains("advance"));}
	@Test void savedAgeNeverRewindsAndExpiryBoundsRemaining(){for(int age:new int[]{Integer.MIN_VALUE,-1,0,35,79,80,Integer.MAX_VALUE})for(long left:new long[]{-100,0,1,30,80,1000}){int expected=Math.max(Math.clamp(age,0,80),80-(int)Math.clamp(left,0,80));assertEquals(expected,BlizzardStormRules.restore(age,1000+left,1000));}assertEquals(80,BlizzardStormRules.restore(80,0,1000));}
	@Test void expiryUsesOriginalLongAndIntOverflowSemantics(){for(long now:new long[]{0,1000,Long.MAX_VALUE})for(int age:new int[]{0,79,80,Integer.MIN_VALUE,Integer.MAX_VALUE})assertEquals(now+Math.max(0,80-age),BlizzardStormRules.expiry(now,age));}
	@Test void sphereIsInclusiveAndPreservesNaNComparison(){assertTrue(BlizzardStormRules.within(64));assertFalse(BlizzardStormRules.within(Math.nextUp(64.0)));assertTrue(BlizzardStormRules.within(Double.NaN));}
}
