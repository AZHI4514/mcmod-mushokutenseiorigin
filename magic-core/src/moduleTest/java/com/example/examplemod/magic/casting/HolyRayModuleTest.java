package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.holy.holy_ray.HolyRayPolicy;
class HolyRayModuleTest {
	@Test void wideColumnHasBoundedLengthAndRadius(){var p=new HolyRayPolicy();assertTrue(p.shape(0,2,5,0,0,1,10));assertFalse(p.shape(0,2.001,5,0,0,1,10));assertFalse(p.shape(0,0,-.01,0,0,1,10));assertFalse(p.shape(0,0,10.01,0,0,1,10));}
	@Test void volumeVisualRetainsWideBeam(){var p=new HolyRayPolicy();assertFalse(p.singleTarget());assertFalse(p.endpointBurst());assertTrue(p.visibleBeam());assertEquals(.55,p.beamSpread());assertEquals(.4,p.burstSpread());}
}
