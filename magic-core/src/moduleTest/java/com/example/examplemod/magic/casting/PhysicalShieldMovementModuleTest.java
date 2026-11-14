package com.example.examplemod.magic.casting;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldMovement;
class PhysicalShieldMovementModuleTest {
	@Test void originalSideCanEscapeButCannotCross(){assertTrue(PhysicalShieldMovement.canEscape(2,3));assertTrue(PhysicalShieldMovement.canEscape(-2,-3));assertFalse(PhysicalShieldMovement.canEscape(2,-3));assertFalse(PhysicalShieldMovement.canEscape(-2,3));assertTrue(PhysicalShieldMovement.canEscape(2,0));}
	@Test void planeAndExactToleranceStillResolveOverlap(){for(double side:new double[]{0,1E-6,-1E-6})assertFalse(PhysicalShieldMovement.canEscape(side,side));assertTrue(PhysicalShieldMovement.canEscape(Math.nextUp(1E-6),1));}
	@Test void clearanceClampsBeforeScaling(){assertTrue(PhysicalShieldMovement.clips(2,4));assertEquals(1.999/4,PhysicalShieldMovement.scale(2,4));assertEquals(0,PhysicalShieldMovement.scale(0.0005,4));assertEquals(0,PhysicalShieldMovement.scale(-1,4));assertFalse(PhysicalShieldMovement.clips(4,4));}
	@Test void ieeeComparisonsAreNotReplacedWithFiniteValidation(){assertFalse(PhysicalShieldMovement.canEscape(Double.NaN,1));assertFalse(PhysicalShieldMovement.canEscape(Double.POSITIVE_INFINITY,0));assertFalse(PhysicalShieldMovement.clips(Double.NaN,3));assertFalse(PhysicalShieldMovement.clips(Double.POSITIVE_INFINITY,3));assertTrue(PhysicalShieldMovement.clips(Double.NEGATIVE_INFINITY,3));assertEquals(0,PhysicalShieldMovement.scale(Double.NEGATIVE_INFINITY,3));}
	@Test void frozenMovementArithmeticMatchesTwentyThousandInputs(){var r=new Random(3938);for(int i=0;i<20000;i++){double side=r.nextDouble()*8-4,toward=r.nextDouble()*10-5,first=r.nextDouble()*14-2,distance=r.nextDouble()*10+.00001;assertEquals(Math.abs(side)>1E-6&&side*toward>=0,PhysicalShieldMovement.canEscape(side,toward));assertEquals(first<distance,PhysicalShieldMovement.clips(first,distance));assertEquals(Math.max(0,first-.001)/distance,PhysicalShieldMovement.scale(first,distance));}}
}
