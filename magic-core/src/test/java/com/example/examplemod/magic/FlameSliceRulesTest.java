package com.example.examplemod.magic;
import java.util.List;
import com.example.examplemod.magic.domain.geometry.BladeSectionVolume;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import com.example.examplemod.magic.spell.fire.flame_slice.*;
class FlameSliceRulesTest {
	@Test void rangeAndAgeAreIndependentLimitsAndInvalidSavedDistanceFailsClosed() {
		var flight=new FlameSliceFlightState(0);
		for(int tick=0;tick<60;tick++) { assertFalse(FlameSliceRules.expired(tick,flight,4));flight=flight.advance(2); }
		assertTrue(flight.exhausted());assertTrue(FlameSliceRules.expired(60,flight,4));
		assertTrue(FlameSliceRules.expired(60,new FlameSliceFlightState(0),4));
		assertFalse(new FlameSliceFlightState(119.99).exhausted());assertTrue(new FlameSliceFlightState(Double.NaN).exhausted());
		assertEquals(0,new FlameSliceFlightState(-9).travelled());assertEquals(17,new FlameSliceFlightState(17).advance(-5).travelled());
		assertTrue(FlameSliceRules.expired(0,new FlameSliceFlightState(0),0));
	}
	@Test void incompleteCrossSectionPassesButUnionOfAdjacentBlocksStops() {
		var volume=new BladeSectionVolume(new Vec3d(0,0,1),0);var origin=new Vec3d(0,1,0);
		var left=new Box3d(-1.5,.5,-.5,0,1.5,.5);var right=new Box3d(0,.5,-.5,1.5,1.5,.5);
		assertFalse(volume.fullyBlocked(origin,List.of(left)));assertTrue(volume.fullyBlocked(origin,List.of(left,right)));
		assertFalse(volume.fullyBlocked(origin,List.of(left,new Box3d(.01,.5,-.5,1.5,1.5,.5))));
		assertEquals(Double.POSITIVE_INFINITY,volume.blockingDistance(origin,2,List.of(left)));
		assertTrue(volume.blockingDistance(origin,2,List.of(new Box3d(-2,0,.7,2,2,1.7)))<.3);
	}
	@Test void sweptEdgeAndVerticalFlightAreNotReplacedByCenterRay() {
		var volume=new BladeSectionVolume(new Vec3d(0,0,1),0);var origin=new Vec3d(0,1,0);
		assertEquals(.1,volume.contact(new Box3d(1.35,.2,.6,1.65,1.8,.9),origin,.75),1E-9);
		assertFalse(Double.isFinite(volume.contact(new Box3d(1.6,0,.6,1.8,2,.9),origin,.75)));
		var up=new BladeSectionVolume(new Vec3d(0,1,0),0);
		assertTrue(Double.isFinite(up.contact(new Box3d(-.2,2,-.2,.2,3,.2),origin,1)));
		assertTrue(up.fullyBlocked(new Vec3d(0,2,0),List.of(new Box3d(-2,2,-1,2,3,1))));
	}
	@Test void waterloggedCapabilityPassesEvenWithoutWaterButSubmergedFullCubeDoesNot() {
		assertTrue(WaterPassage.passes(true,true,false)); // dry slab/stair capability
		assertTrue(WaterPassage.passes(true,true,true));
		assertTrue(WaterPassage.passes(true,false,true)); // water cell/flowing water
		assertFalse(WaterPassage.passes(true,false,false)); // full cube or lava
		assertFalse(WaterPassage.passes(false,true,true));
		assertFalse(WaterPassage.passes(false,false,true));
	}
	@Test void targetClaimIsPerProjectileAndSurvivesSnapshotRestoreEvenOnFailedDamage() {
		var id=UUID.randomUUID();var other=UUID.randomUUID();var history=new ImpactHistory();
		assertTrue(history.claim(id));assertFalse(history.claim(id));assertFalse(history.contains(other));
		var saved=history.snapshot();var reloaded=new ImpactHistory(saved);assertFalse(reloaded.claim(id));assertTrue(reloaded.claim(other));
		assertEquals(Set.of(id),saved);assertTrue(new ImpactHistory().claim(id));
		assertThrows(UnsupportedOperationException.class,()->saved.add(other));
	}
	@Test void wallWinsEqualContactAndAoeKeepsTargetBodyHeight() {
		assertTrue(FlameSliceRules.canTrigger(.9,1,2,false));assertFalse(FlameSliceRules.canTrigger(1,1,2,false));
		assertFalse(FlameSliceRules.canTrigger(2.1,Double.POSITIVE_INFINITY,2,false));assertFalse(FlameSliceRules.canTrigger(.2,1,2,true));
		assertFalse(FlameSliceRules.canTrigger(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,2,false));
		var box=FlameSliceRules.impactArea(7,-2,9,1.7);assertEquals(5.5,box.minX);assertEquals(8.5,box.maxX);assertEquals(7.5,box.minZ);assertEquals(10.5,box.maxZ);assertEquals(-2,box.minY);assertEquals(1.7,box.maxY);
	}
	@Test void ignitionKeepsStrictFloatThresholdAndImmunities() {
		assertEquals(100,FlameSliceRules.ignitionTicks(Math.nextDown(.8F),false,false));assertEquals(0,FlameSliceRules.ignitionTicks(.8F,false,false));
		assertEquals(0,FlameSliceRules.ignitionTicks(0,true,false));assertEquals(0,FlameSliceRules.ignitionTicks(0,false,true));assertEquals(0,FlameSliceRules.ignitionTicks(Float.NaN,false,false));
	}
}
