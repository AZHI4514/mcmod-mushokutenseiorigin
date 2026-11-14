package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules;
import com.example.examplemod.magic.mechanic.projectile.WaterPassage;
class IcicleBreakFlightTest {
	@Test void freshAndSavedBudgetsExpireBeforeNextAgeIncrement(){
		for(int limit:new int[]{0,1,37,120,Integer.MAX_VALUE})for(int age:new int[]{0,1,36,37,119,120,Integer.MAX_VALUE})assertEquals(age>=limit,PiercingFlightRules.expired(age,limit));
	}
	@Test void candidateAndFinalContactPreserveStrictWallTiesAndInclusiveStepEnd(){
		assertTrue(PiercingFlightRules.candidate(2,3,4,false));assertFalse(PiercingFlightRules.candidate(3,3,4,false));assertFalse(PiercingFlightRules.candidate(2,3,2,false));assertFalse(PiercingFlightRules.candidate(1,3,4,true));
		assertTrue(PiercingFlightRules.contactDue(3,3,false));assertFalse(PiercingFlightRules.contactDue(Math.nextUp(3D),3,false));assertFalse(PiercingFlightRules.contactDue(1,3,true));
	}
	@Test void waterloggedCapabilityPassesEvenDryButLavaAndSolidDoNot(){
		assertTrue(WaterPassage.passes(true,true,false));assertTrue(WaterPassage.passes(true,false,true));assertTrue(WaterPassage.passes(true,true,true));assertFalse(WaterPassage.passes(true,false,false));assertFalse(WaterPassage.passes(false,true,true));
	}
}
