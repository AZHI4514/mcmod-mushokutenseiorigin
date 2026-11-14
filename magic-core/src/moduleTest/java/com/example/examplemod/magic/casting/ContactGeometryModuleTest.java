package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.geometry.ContactGeometry;
import com.example.examplemod.magic.domain.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ContactGeometryModuleTest {
	@Test void bodyInflationRetainsOpenFaceAndFullHeightContact(){var body=new Box3d(-.3,0,-.3,.3,1.8,.3);assertTrue(ContactGeometry.body(body,new Box3d(.4,1.7,-.2,.8,2,.2)));assertFalse(ContactGeometry.body(body,new Box3d(.42,0,-.2,.8,1,.2)));}
	@Test void ringKeepsVerticalSlabAndNearFarIntervals(){var at=Vec3d.ZERO;assertTrue(ContactGeometry.ring(new Box3d(-3,0,-3,3,4,3),at,2));assertFalse(ContactGeometry.ring(new Box3d(-.3,0,-.3,.3,1.8,.3),at,2));assertFalse(ContactGeometry.ring(new Box3d(1.8,.650001,-.2,2.2,2,.2),at,2));assertTrue(ContactGeometry.ring(new Box3d(1.8,.65,-.2,2.2,2,.2),at,2));}
}
