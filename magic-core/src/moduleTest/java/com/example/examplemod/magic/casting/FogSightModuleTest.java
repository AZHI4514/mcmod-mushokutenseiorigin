package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.FogSight;
class FogSightModuleTest {
	static class State implements FogSight.State {int left=20,range=6,color=1,linger=20;StringBuilder log=new StringBuilder();public int remaining(){return left;}public int range(){return range;}public void range(int v){log.append('r');range=v;}public void color(int v){log.append('c');color=v;}public void linger(int v){log.append('l');linger=v;}public void remaining(int v){log.append('t');left=v;}}
	@Test void weakerFogOnlyExtendsLifetime(){var s=new State();FogSight.apply(s,12,40,2);assertEquals(6,s.range);assertEquals(1,s.color);assertEquals(20,s.linger);assertEquals(40,s.left);assertEquals("t",s.log.toString());}
	@Test void equalStrengthReplacesColorAndLinger(){var s=new State();FogSight.apply(s,6,10,3);assertEquals(3,s.color);assertEquals(10,s.linger);assertEquals(20,s.left);assertEquals("rclt",s.log.toString());}
	@Test void expiredFogAllowsWeakerReplacement(){var s=new State();s.left=0;FogSight.apply(s,12,40,2);assertEquals(12,s.range);assertEquals(40,s.left);}
	static class Target implements FogSight.Target {boolean retained;int left=10,range=6;double distance=36;public boolean attackerPresent(){return true;}public boolean targetPresent(){return true;}public boolean retainedTarget(){return retained;}public int remaining(){return left;}public int range(){return range;}public double squaredDistance(){return distance;}}
	@Test void existingAggroBypassesFogRead(){var t=new Target(){public int remaining(){fail();return 0;}};t.retained=true;assertTrue(FogSight.mayAcquire(t));}
	@Test void closedRangeAndExpiredFog(){var t=new Target();assertTrue(FogSight.mayAcquire(t));t.distance=Math.nextUp(36d);assertFalse(FogSight.mayAcquire(t));t.left=0;assertTrue(FogSight.mayAcquire(t));}
	@Test void nullAttackerSkipsTargetAndAttachments(){assertTrue(FogSight.mayAcquire(new Target(){public boolean attackerPresent(){return false;}public boolean targetPresent(){fail();return false;}public int remaining(){fail();return 0;}}));}
}
