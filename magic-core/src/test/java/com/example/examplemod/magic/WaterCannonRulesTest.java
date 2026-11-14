package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.water_cannon.*;
import com.example.examplemod.magic.domain.model.Vec3d;
class WaterCannonRulesTest {
	@Test void strictWetProbabilityAndIndependentFlightLimit() {
		assertEquals(200,WaterCannonRules.wetTicks(Math.nextDown(.6F)));assertEquals(0,WaterCannonRules.wetTicks(.6F));assertEquals(0,WaterCannonRules.wetTicks(Float.NaN));
		assertFalse(WaterCannonRules.expired(119,4));assertTrue(WaterCannonRules.expired(120,4));assertTrue(WaterCannonRules.expired(0,0));
		assertEquals(240,WaterCannonSpec.FLIGHT_TICKS*WaterCannonSpec.PROJECTILE.speed());
	}
	@Test void wallOrFluidWinsTiesAndHistoryPreventsRepeatedDirectHits() {
		assertTrue(WaterCannonRules.directContact(.5,1,2,false));assertFalse(WaterCannonRules.directContact(1,1,2,false));assertFalse(WaterCannonRules.directContact(1,2,1,false));assertFalse(WaterCannonRules.directContact(.5,1,2,true));assertFalse(WaterCannonRules.directContact(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,false));
	}
	@Test void blockBurstIsThreeWideNotThreeRadius() {
		var box=WaterCannonRules.blockImpactBounds(new Vec3d(3,4,5));assertEquals(1.5,box.minX);assertEquals(4.5,box.maxX);assertEquals(2.5,box.minY);assertEquals(5.5,box.maxY);assertEquals(3.5,box.minZ);assertEquals(6.5,box.maxZ);
	}
}
