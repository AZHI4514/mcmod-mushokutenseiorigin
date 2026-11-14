package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn;
import com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarIgnition;
class FlamePillarBurnModuleTest {
	private static final class World implements FlamePillarIgnition.World,FlamePillarBurn.Target {
		final List<String> events=new ArrayList<>();boolean alive=true,sameLevel=true;long now=100;FlamePillarBurn task;
		public void ignite(int ticks){events.add("ignite:"+ticks);}
		public void damage(){events.add("damage:"+now);}
		public void placeFire(){events.add("fire");}
		public void burst(){events.add("burst");}
		public void schedule(){events.add("schedule");task=new FlamePillarBurn(now);}
		public boolean alive(){return alive;}
		public boolean sameLevel(){return sameLevel;}
	}
	@Test void ignitionThenTwentyAndFortyTicksDamageWithoutRepeatingTerrainFire(){
		var w=new World();FlamePillarIgnition.apply(w);
		assertEquals(List.of("ignite:60","damage:100","fire","burst","schedule"),w.events);
		for(int age=0;age<=60;age++){w.now=100+age;assertEquals(age==60,w.task.tick(w.now,w));}
		assertEquals(List.of("ignite:60","damage:100","fire","burst","schedule","damage:120","damage:140"),w.events);
	}
	@Test void deathAndLevelMismatchEndBeforePulse(){
		var a=new World();a.alive=false;assertTrue(new FlamePillarBurn(100).tick(120,a));assertTrue(a.events.isEmpty());
		var b=new World();b.sameLevel=false;assertTrue(new FlamePillarBurn(100).tick(140,b));assertTrue(b.events.isEmpty());
	}
	@Test void skippedTimesAreNotCaughtUpAndBackwardsTimeDoesNotDamage(){
		var w=new World();var task=new FlamePillarBurn(100);
		for(long now:new long[]{80,99,100,119,121,139,141})assertFalse(task.tick(now,w));
		assertTrue(task.tick(160,w));assertTrue(w.events.isEmpty());
	}
	@Test void tasksAreIndependentAndRetainLegacySameTickPulseSemantics(){
		var a=new World();var b=new World();var first=new FlamePillarBurn(100);var second=new FlamePillarBurn(110);
		a.now=120;assertFalse(first.tick(120,a));assertFalse(first.tick(120,a));assertFalse(second.tick(120,b));
		assertEquals(List.of("damage:120","damage:120"),a.events);assertTrue(b.events.isEmpty());
		b.now=130;assertFalse(second.tick(130,b));assertEquals(List.of("damage:130"),b.events);
	}
	@Test void firstEffectExceptionDoesNotQueueOrRollBackEarlierEffects(){
		var events=new ArrayList<String>();
		assertThrows(IllegalStateException.class,()->FlamePillarIgnition.apply(new FlamePillarIgnition.World(){
			public void ignite(int ticks){events.add("ignite");}
			public void damage(){events.add("damage");throw new IllegalStateException("damage hook");}
			public void placeFire(){fail();}
			public void burst(){fail();}
			public void schedule(){fail();}
		}));assertEquals(List.of("ignite","damage"),events);
	}
	@Test void failedDelayedDamageRemainsRetryableAndDoesNotAddInitialFire(){
		var events=new ArrayList<String>();var task=new FlamePillarBurn(100);
		assertThrows(IllegalStateException.class,()->task.tick(120,new FlamePillarBurn.Target(){
			public boolean alive(){return true;}
			public boolean sameLevel(){return true;}
			public void damage(){events.add("failed");throw new IllegalStateException("hook");}
		}));var w=new World();w.now=120;assertFalse(task.tick(120,w));assertEquals(List.of("damage:120"),w.events);assertEquals(List.of("failed"),events);
	}
}
