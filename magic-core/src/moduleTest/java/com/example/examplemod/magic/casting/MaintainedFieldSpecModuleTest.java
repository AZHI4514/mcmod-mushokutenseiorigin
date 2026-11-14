package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.mechanic.field.MaintainedFieldSpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MaintainedFieldSpecModuleTest {
	@Test void followingCylinderAndFixedCylinderKeepDifferentOriginsAndUpkeep(){
		var atmosphere=MaintainedFieldSpec.ATMOSPHERE;var gravity=MaintainedFieldSpec.GRAVITY;
		assertTrue(atmosphere.followsOwner());assertFalse(gravity.followsOwner());
		assertEquals(30,atmosphere.upkeep());assertEquals(8,gravity.upkeep());
		assertEquals(48,atmosphere.radius(3000));assertEquals(64,atmosphere.above(3000));assertEquals(0,atmosphere.below(3000));
		assertEquals(6,gravity.radius(3000));assertEquals(12,gravity.above(3000));assertEquals(0,gravity.below(3000));
	}
	@Test void nuclearSpherePreservesFullExtraRangeAndIsNotChargedAgain(){
		var nuclear=MaintainedFieldSpec.NUCLEAR;assertEquals(0,nuclear.upkeep());assertFalse(nuclear.followsOwner());
		for(int extra=0;extra<=3000;extra++){
			double expected=24+extra*8.0/3000;
			assertEquals(expected,nuclear.radius(extra));assertEquals(expected,nuclear.below(extra));assertEquals(expected,nuclear.above(extra));
		}
	}
}
