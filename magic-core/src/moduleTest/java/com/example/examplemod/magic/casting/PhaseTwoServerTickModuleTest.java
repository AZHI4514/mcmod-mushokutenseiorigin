package com.example.examplemod.magic.casting;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.CarrierServerTick;

class PhaseTwoServerTickModuleTest {
	static class World implements CarrierServerTick.World {
		final List<String> events=new ArrayList<>();
		boolean below, current=true, owner=true, stationary, ahead=true, expired, shield, rock;
		int age;
		public boolean belowMinimum(){events.add("below");return below;}
		public void discard(){events.add("discard");}
		public boolean loadedCurrent(){events.add("current");return current;}
		public void resolveOwner(){events.add("owner");}
		public boolean stationary(){events.add("stationary?");return stationary;}
		public boolean hasOwner(){events.add("hasOwner");return owner;}
		public boolean loadedAhead(){events.add("ahead");return ahead;}
		public boolean expired(){events.add("expired");return expired;}
		public void advanceAge(){events.add("age");age++;}
		public boolean shield(){events.add("shield?");return shield;}
		public void tickShield(){events.add("shield");}
		public void tickStationary(){events.add("stationary");}
		public boolean fallingRock(){events.add("rock?");return rock;}
		public void fall(){events.add("fall");}
		public void fly(){events.add("fly");}
	}
	@Test void movingOrderAndRepeatedStationaryReads(){
		var w=new World();CarrierServerTick.apply(w);
		assertEquals(List.of("below","current","owner","stationary?","stationary?","ahead","expired","age","shield?","stationary?","rock?","fly"),w.events);assertEquals(1,w.age);
	}
	@Test void belowWorldDiscardsWithoutQueryingChunks(){var w=new World();w.below=true;CarrierServerTick.apply(w);assertEquals(List.of("below","discard"),w.events);assertEquals(0,w.age);}
	@Test void unloadedCurrentDoesNotResolveOwner(){var w=new World();w.current=false;CarrierServerTick.apply(w);assertEquals(List.of("below","current"),w.events);assertEquals(0,w.age);}
	@Test void ownerlessStationaryPausesBeforeExpiry(){var w=new World();w.stationary=true;w.owner=false;CarrierServerTick.apply(w);assertEquals(List.of("below","current","owner","stationary?","hasOwner"),w.events);assertEquals(0,w.age);}
	@Test void ownerlessMovingStillFlies(){var w=new World();w.owner=false;CarrierServerTick.apply(w);assertFalse(w.events.contains("hasOwner"));assertEquals("fly",w.events.getLast());}
	@Test void unloadedAheadPausesBeforeExpiryAndAge(){var w=new World();w.ahead=false;CarrierServerTick.apply(w);assertEquals("ahead",w.events.getLast());assertFalse(w.events.contains("expired"));assertEquals(0,w.age);}
	@Test void expiredDiscardsWithoutAgeOrDispatch(){var w=new World();w.expired=true;CarrierServerTick.apply(w);assertEquals("discard",w.events.getLast());assertEquals(0,w.age);assertFalse(w.events.contains("shield?"));}
	@Test void shieldTakesPriorityOverStationary(){var w=new World();w.stationary=true;w.shield=true;CarrierServerTick.apply(w);assertEquals(1,w.age);assertEquals("shield",w.events.getLast());assertFalse(w.events.contains("ahead"));assertFalse(w.events.contains("stationary"));}
	@Test void stationarySkipsAheadAndRock(){var w=new World();w.stationary=true;CarrierServerTick.apply(w);assertEquals("stationary",w.events.getLast());assertFalse(w.events.contains("ahead"));assertFalse(w.events.contains("rock?"));assertEquals(1,w.age);}
	@Test void fallingDispatchOccursAfterAge(){var w=new World();w.rock=true;CarrierServerTick.apply(w);assertEquals("fall",w.events.getLast());assertEquals(1,w.age);assertFalse(w.events.contains("fly"));}
	@Test void expiryExceptionDoesNotAdvanceAge(){var w=new World(){public boolean expired(){super.expired();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CarrierServerTick.apply(w));assertEquals(0,w.age);assertEquals("expired",w.events.getLast());}
	@Test void worldFailureAfterAgeKeepsIncrement(){var w=new World(){public void fly(){super.fly();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->CarrierServerTick.apply(w));assertEquals(1,w.age);assertEquals("fly",w.events.getLast());assertFalse(w.events.contains("discard"));}
}
