package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.stone_cannon.*;
import com.example.examplemod.magic.mechanic.channel.ChargeCurve;
import com.example.examplemod.magic.mechanic.projectile.WaterPassage;
class StoneCannonRulesTest {
	@Test void eachEligibilityConditionIndependentlyInterruptsCharging() {
		boolean[] facts={true,false,false,false,true,true,true,true};
		assertTrue(eligibility(facts).eligible());
		for(int i=0;i<facts.length;i++) { facts[i]=!facts[i];assertFalse(eligibility(facts).eligible(),"condition "+i);facts[i]=!facts[i]; }
	}
	private ChargeEligibility eligibility(boolean[] f) { return new ChargeEligibility(f[0],f[1],f[2],f[3],f[4],f[5],f[6],f[7]); }
	@Test void flightBoundaryRemains120TicksAndTheoretical360Blocks() {
		assertFalse(StoneCannonRules.expired(119,9));assertTrue(StoneCannonRules.expired(120,9));
		assertTrue(StoneCannonRules.expired(0,Math.nextDown(1E-8)));assertFalse(StoneCannonRules.expired(0,1E-8));
		assertEquals(360,StoneCannonSpec.FLIGHT_TICKS*StoneCannonSpec.PROJECTILE.speed());
	}
	@Test void contactTiesAndSavedVictimHistoryExcludeTargets() {
		assertTrue(StoneCannonRules.directContact(.5,1,2,false));
		assertFalse(StoneCannonRules.directContact(1,1,2,false));assertFalse(StoneCannonRules.directContact(1,2,1,false));
		assertFalse(StoneCannonRules.directContact(.5,1,2,true));
		assertFalse(StoneCannonRules.directContact(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,false));
	}
	@Test void persistedPaymentKeepsMinimumAndUncappedDamageSteps() {
		assertEquals(10,StoneCannonRules.paidMana(Integer.MIN_VALUE));assertEquals(Integer.MAX_VALUE,StoneCannonRules.paidMana(Integer.MAX_VALUE));
		assertEquals(10,ChargeCurve.stoneDamage(19));assertEquals(11,ChargeCurve.stoneDamage(20));assertEquals(19,ChargeCurve.stoneDamage(100));
		assertTrue(ChargeCurve.stoneDamage(1000)>19);
	}
	@Test void dryWaterloggableAndWaterPassWhileSolidAndLavaBlock() {
		assertTrue(WaterPassage.passes(true,true,false));assertTrue(WaterPassage.passes(true,true,true));
		assertTrue(WaterPassage.passes(true,false,true));assertFalse(WaterPassage.passes(true,false,false));
	}
}
