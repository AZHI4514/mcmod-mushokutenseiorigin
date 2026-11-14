package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.holy.exorcist_ray.ExorcistRayPolicy;
class ExorcistRayModuleTest {
	@Test void singleTargetVisualRemainsNarrowBeam(){var p=new ExorcistRayPolicy();assertTrue(p.singleTarget());assertFalse(p.endpointBurst());assertTrue(p.visibleBeam());assertEquals(.035,p.beamSpread());assertEquals(.4,p.burstSpread());}
	@Test void nearestStrictComparisonKeepsFirstTieAndRejectsNaN(){assertTrue(ExorcistRayPolicy.nearer(1,2));assertFalse(ExorcistRayPolicy.nearer(2,2));assertFalse(ExorcistRayPolicy.nearer(Double.NaN,2));assertFalse(ExorcistRayPolicy.nearer(1,Double.NaN));}
}
