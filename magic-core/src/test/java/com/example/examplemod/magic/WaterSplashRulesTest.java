package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.water_splash.*;
class WaterSplashRulesTest {
	@Test void missUsesTwelveBlockEndpointAndHitMovesTowardAir() {
		var origin=new Vec3d(1,2,3);var direction=new Vec3d(0,1,0);
		var miss=WaterSplashRules.center(origin,direction,null);assertEquals(14,miss.y);assertEquals(1,miss.x);assertEquals(3,miss.z);
		var hit=WaterSplashRules.center(origin,direction,new Vec3d(1,7,3));assertEquals(6.98,hit.y,1E-12);
	}
	@Test void sphereUsesBodyCenterIncludesBoundaryAndRejectsObscuredTargets() {
		assertTrue(WaterSplashRules.affects(Vec3d.ZERO,new Vec3d(6,0,0),true));
		assertFalse(WaterSplashRules.affects(Vec3d.ZERO,new Vec3d(Math.nextUp(6D),0,0),true));
		assertFalse(WaterSplashRules.affects(Vec3d.ZERO,new Vec3d(5,5,0),true));
		assertFalse(WaterSplashRules.affects(Vec3d.ZERO,Vec3d.ZERO,false));
		var box=WaterSplashRules.bounds(new Vec3d(1,2,3));assertEquals(-5,box.minX);assertEquals(7,box.maxX);assertEquals(-4,box.minY);assertEquals(8,box.maxY);
		assertEquals(2000,WaterSplashSpec.WET_TICKS);assertEquals(1F,WaterSplashSpec.DAMAGE);
	}
}
