package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.exodus_flame.*;
class ExodusFlameFlightModuleTest {
	@Test void expiresAtBudgetAndStrictLowMotion(){assertFalse(ExodusFlameFlight.expired(179,180,1E-8));assertTrue(ExodusFlameFlight.expired(180,180,4));assertTrue(ExodusFlameFlight.expired(0,180,Math.nextDown(1E-8)));assertFalse(ExodusFlameFlight.expired(0,180,Double.NaN));}
	@Test void candidateMustPrecedeBothWallAndFluid(){assertTrue(ExodusFlameFlight.candidate(1,2,3));assertFalse(ExodusFlameFlight.candidate(2,2,3));assertFalse(ExodusFlameFlight.candidate(2,3,2));assertFalse(ExodusFlameFlight.candidate(Double.NaN,3,4));}
	@Test void finalContactIncludesStepBoundaryButRejectsClaimed(){assertFalse(ExodusFlameFlight.skipContact(2,2,false));assertTrue(ExodusFlameFlight.skipContact(Math.nextUp(2.0),2,false));assertTrue(ExodusFlameFlight.skipContact(1,2,true));}
	@Test void frozenBoundariesMatchTenThousandInputs(){var r=new Random(44180);for(int i=0;i<10000;i++){double c=r.nextDouble()*5,w=r.nextDouble()*5,f=r.nextDouble()*5,d=r.nextDouble()*5;boolean seen=r.nextBoolean();assertEquals(c<Math.min(w,f),ExodusFlameFlight.candidate(c,w,f));assertEquals(c>d||seen,ExodusFlameFlight.skipContact(c,d,seen));}}
}
