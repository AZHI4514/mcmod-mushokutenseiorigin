package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.ice_lance.IceLanceRules;
class IceLanceRulesTest {
	@Test void hitOccursOnlyOnFirstIncrementedAgeIncludingReloadedAges() {
		for(int age=-1;age<200;age++)assertEquals(age==1,IceLanceRules.damageDue(age));
		assertFalse(IceLanceRules.damageDue(Integer.MAX_VALUE));
	}
	@Test void freezingPreservesThresholdImmunityWarmthAndNonFiniteRolls() {
		assertEquals(200,IceLanceRules.freezeTicks(Math.nextDown(.8F),true,false));
		assertEquals(0,IceLanceRules.freezeTicks(.8F,true,false));
		assertEquals(0,IceLanceRules.freezeTicks(0,false,false));assertEquals(0,IceLanceRules.freezeTicks(0,true,true));
		assertEquals(0,IceLanceRules.freezeTicks(Float.NaN,true,false));
	}
}
