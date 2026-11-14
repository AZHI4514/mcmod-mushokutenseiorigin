package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.ex_flame.*;
import com.example.examplemod.magic.domain.model.Vec3d;
class ExFlameRulesTest {
	@Test void ignitionUsesExactFloatThresholdAndVanillaImmunityInputs() {
		assertEquals(200,ExFlameRules.ignitionTicks(Math.nextDown(.8F),false,false));assertEquals(0,ExFlameRules.ignitionTicks(.8F,false,false));assertEquals(0,ExFlameRules.ignitionTicks(Float.NaN,false,false));assertEquals(0,ExFlameRules.ignitionTicks(0,true,false));assertEquals(0,ExFlameRules.ignitionTicks(0,false,true));
	}
	@Test void flightLimitAndBurstGeometryKeepDistinctSizes() {
		assertFalse(ExFlameRules.expired(119,4));assertTrue(ExFlameRules.expired(120,4));assertTrue(ExFlameRules.expired(0,0));
		assertEquals(240,ExFlameSpec.FLIGHT_TICKS*ExFlameSpec.PROJECTILE.speed());assertEquals(2,ExFlameSpec.BODY_SIZE);assertEquals(2,ExFlameSpec.TERRAIN_WIDTH);
		var box=ExFlameRules.blockImpactBounds(new Vec3d(2,3,4));assertEquals(.5,box.minX);assertEquals(3.5,box.maxX);assertEquals(1.5,box.minY);assertEquals(4.5,box.maxY);assertEquals(2.5,box.minZ);assertEquals(5.5,box.maxZ);
	}
	@Test void priorHitAndEqualWallOrFluidContactPreventDirectDamage() {
		assertTrue(ExFlameRules.directContact(.1,1,2,false));assertFalse(ExFlameRules.directContact(.1,1,2,true));assertFalse(ExFlameRules.directContact(1,1,2,false));assertFalse(ExFlameRules.directContact(1,2,1,false));
	}
}
