package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.geometry.SweptVolume;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.spell.wind.sonic_boom.SonicBoomSpec;
class SonicBoomGeometryTest {
	@Test void wideEdgeHitsButOutsideWidthAndHeightDoNot() {
		var direction=new Vec3d(0,0,1);var origin=Vec3d.ZERO;
		var body=new SweptVolume(direction,SonicBoomSpec.WIDTH,SonicBoomSpec.HEIGHT,SonicBoomSpec.DEPTH);
		assertEquals(.875,body.contact(new Box3d(1.4,-.1,1,1.6,.1,1.2),origin,direction,2),1E-9);
		assertFalse(Double.isFinite(body.contact(new Box3d(1.51,-.1,1,1.8,.1,1.2),origin,direction,2)));
		assertFalse(Double.isFinite(body.contact(new Box3d(-.1,.51,1,.1,.8,1.2),origin,direction,2)));
	}
	@Test void verticalSweepRetainsThinDepthAndFullWidth() {
		var up=new Vec3d(0,1,0);var body=new SweptVolume(up,3,1,.25);var box=body.bounds(Vec3d.ZERO);
		assertEquals(.25,box.maxY-box.minY,1E-9);
		assertTrue(Double.isFinite(body.contact(new Box3d(-.1,1,-.1,.1,1.2,.1),Vec3d.ZERO,up,2)));
		assertEquals(240,SonicBoomSpec.FLIGHT_TICKS*SonicBoomSpec.PROJECTILE.speed());
	}
}
