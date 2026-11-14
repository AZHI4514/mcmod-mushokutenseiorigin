package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.mixed.blast.BlastRules;
class BlastRulesModuleTest {
	@Test void rangeAndForwardDirection() {
		assertTrue(BlastRules.contains(0,0,10,0,0,7));
		assertFalse(BlastRules.contains(0,0,Math.nextUp(10.0),0,0,1));
		assertFalse(BlastRules.contains(0,0,-1,0,0,1));
		assertTrue(BlastRules.contains(Math.sin(Math.toRadians(29.999)),0,Math.cos(Math.toRadians(29.999)),0,0,1));
		assertFalse(BlastRules.contains(Math.sin(Math.toRadians(30.001)),0,Math.cos(Math.toRadians(30.001)),0,0,1));
	}
	@Test void nearOriginAndVanillaDirectionThresholdRemainDistinct() {
		assertTrue(BlastRules.contains(Math.nextDown(1E-6),0,0,Double.NaN,0,0));
		assertFalse(BlastRules.contains(1E-6,0,0,0,0,0));
		assertFalse(BlastRules.contains(1,0,0,Math.nextDown(1E-5),0,0));
		assertTrue(BlastRules.contains(1,0,0,1E-5,0,0));
		assertFalse(BlastRules.contains(Double.NaN,0,0,1,0,0));
		assertFalse(BlastRules.contains(Double.POSITIVE_INFINITY,0,0,1,0,0));
	}
	@Test void frozenLegacyArithmeticMatchesRandomAndExceptionalInputs() {
		var random=new java.util.Random(1113);
		for(int i=0;i<10000;i++) {
			double x=random.nextDouble()*40-20,y=random.nextDouble()*40-20,z=random.nextDouble()*40-20;
			double dx=random.nextDouble()*2-1,dy=random.nextDouble()*2-1,dz=random.nextDouble()*2-1;
			assertEquals(legacy(x,y,z,dx,dy,dz),BlastRules.contains(x,y,z,dx,dy,dz));
		}
		double[] edges={0,-0.0,1E-6,1E-5,Double.NaN,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY};
		for(double x:edges) for(double dx:edges)
			assertEquals(legacy(x,0,0,dx,0,0),BlastRules.contains(x,0,0,dx,0,0));
	}
	private static boolean legacy(double x,double y,double z,double dx,double dy,double dz) {
		double length=Math.sqrt(x*x+y*y+z*z);
		if(!(length<=10))return false;
		if(length<1E-6)return true;
		double n=Math.sqrt(dx*dx+dy*dy+dz*dz);
		double nx=n<1E-5?0:dx/n,ny=n<1E-5?0:dy/n,nz=n<1E-5?0:dz/n;
		return (x*nx+y*ny+z*nz)/length>=Math.cos(Math.PI/6);
	}
}
