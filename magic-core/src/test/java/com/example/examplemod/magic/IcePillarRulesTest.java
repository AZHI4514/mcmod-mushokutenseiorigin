package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.spell.water.ice_pillar.*;
class IcePillarRulesTest {
	@Test void columnIsOneByThreeByOneAtNegativeAndPositiveBlockCorners() {
		for(int x:new int[]{-17,0,23})for(int y:new int[]{-64,5,319})for(int z:new int[]{-1,0,16}) {
			var box=IcePillarRules.bounds(new Vec3d(x,y,z));
			assertEquals(x,box.minX);assertEquals(y,box.minY);assertEquals(z,box.minZ);
			assertEquals(x+1,box.maxX);assertEquals(y+3,box.maxY);assertEquals(z+1,box.maxZ);
		}
	}
	@Test void strictFreezeThresholdKeepsWarmthAndImmunityExclusions() {
		assertEquals(200,IcePillarRules.freezeTicks(Math.nextDown(.8F),true,false));
		assertEquals(0,IcePillarRules.freezeTicks(.8F,true,false));assertEquals(0,IcePillarRules.freezeTicks(Float.NaN,true,false));
		assertEquals(0,IcePillarRules.freezeTicks(0,false,false));assertEquals(0,IcePillarRules.freezeTicks(0,true,true));
	}
	@Test void columnFacesAreNotRadialDamageAndTouchingFaceIsExcluded() {
		var column=IcePillarRules.bounds(Vec3d.ZERO);
		assertTrue(column.intersects(new Box3d(.9,2.9,.9,1.1,3.1,1.1)));
		assertFalse(column.intersects(new Box3d(1,0,0,2,3,1)));assertFalse(column.intersects(new Box3d(0,3,0,1,4,1)));
		assertFalse(column.intersects(new Box3d(-1,0,0,0,3,1)));
	}
}
