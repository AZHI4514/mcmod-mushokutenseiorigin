package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.AreaTick;
class AreaTickModuleTest {
	private static class World implements AreaTick.World {
		final List<String> calls=new ArrayList<>();String reject="";int remaining=3;boolean fixed=true;double distance=0;
		boolean pass(String name){calls.add(name);return !reject.equals(name);}
		public boolean ownerPresent(){return pass("present");}
		public boolean ownerRemoved(){return !pass("removed");}
		public boolean ownerAlive(){return pass("alive");}
		public boolean sameLevel(){return pass("level");}
		public int decrementRemaining(){calls.add("remaining");return --remaining;}
		public boolean loaded(){return pass("loaded");}
		public void discard(){calls.add("discard");}
		public boolean stationary(){calls.add("stationary");return fixed;}
		public double displacementSquared(){calls.add("distance");return distance;}
		public void breakMaintenance(){calls.add("break");}
		public void followOwner(){calls.add("follow");}
		public void advancePulse(){calls.add("pulse");}
		public void pruneContacts(){calls.add("prune");}
		public void applyContacts(){calls.add("effects");}
	}
	@Test void invalidOwnerShortCircuitsBeforeLifetimeAndChunks(){for(String reject:List.of("present","removed","alive","level")){var w=new World();w.reject=reject;AreaTick.tick(w);assertEquals(3,w.remaining);assertFalse(w.calls.contains("loaded"));assertEquals("discard",w.calls.getLast());assertFalse(w.calls.contains("pulse"));}}
	@Test void expiryDecrementsButSkipsChunkQuery(){var w=new World();w.remaining=1;AreaTick.tick(w);assertEquals(List.of("present","removed","alive","level","remaining","discard"),w.calls);}
	@Test void unloadedAreaStillConsumesOneLifetimeTick(){var w=new World();w.reject="loaded";AreaTick.tick(w);assertEquals(2,w.remaining);assertEquals("discard",w.calls.getLast());assertFalse(w.calls.contains("pulse"));}
	@Test void stationaryAreaPrunesBeforeContactEffects(){var w=new World();AreaTick.tick(w);assertEquals(List.of("present","removed","alive","level","remaining","loaded","stationary","distance","stationary","pulse","prune","effects"),w.calls);}
	@Test void anchorThresholdIsInclusiveAndNaNDoesNotBreak(){for(double d:new double[]{0,Math.nextDown(1E-6),1E-6,Math.nextUp(1E-6),Double.NaN}){var w=new World();w.distance=d;AreaTick.tick(w);assertEquals(d>=1E-6,w.calls.contains("break"));assertFalse(w.calls.contains("follow"));}}
	@Test void movingFieldFollowsBeforePulseAndDoesNotReadAnchor(){var w=new World();w.fixed=false;AreaTick.tick(w);assertFalse(w.calls.contains("distance"));assertTrue(w.calls.indexOf("follow")<w.calls.indexOf("pulse"));}
	@Test void contactExceptionRetainsLifetimeAndDoesNotDiscard(){var w=new World(){public void applyContacts(){super.applyContacts();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->AreaTick.tick(w));assertEquals(2,w.remaining);assertFalse(w.calls.contains("discard"));assertEquals("effects",w.calls.getLast());}
	@Test void maintenanceFailureStopsBeforeFollowOrPulse(){var w=new World(){public void breakMaintenance(){throw new IllegalStateException();}};w.distance=1;assertThrows(IllegalStateException.class,()->AreaTick.tick(w));assertEquals(2,w.remaining);assertFalse(w.calls.contains("pulse"));}
}
