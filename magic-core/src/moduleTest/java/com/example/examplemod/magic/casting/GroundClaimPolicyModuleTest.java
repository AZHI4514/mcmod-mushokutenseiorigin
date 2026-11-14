package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.spell.fire.flame_storm.FlameStormPolicy;
import com.example.examplemod.magic.spell.fire.apocalypse_flame.ApocalypseFlamePolicy;
import com.example.examplemod.magic.spell.earth.continental_sink.ContinentalSinkPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class GroundClaimPolicyModuleTest {
	@Test void wallClaimsAreCappedAcrossChangingPulseIndices() {
		var policy=new FlameStormPolicy();Integer ledger=null;
		for(int pulse=0;pulse<6;pulse++){assertTrue(policy.mayClaim(ledger,pulse));assertTrue(policy.cadenceRequired(pulse));ledger=policy.claimedValue(ledger,pulse);}
		assertFalse(policy.mayClaim(ledger,9));assertTrue(policy.continuousContacts());assertTrue(policy.perTickAttack());
	}
	@Test void openingExplosionIsNotPeriodicFireDamage() {
		var policy=new ApocalypseFlamePolicy();assertEquals(48F,policy.damage(0,99));assertFalse(policy.cadenceRequired(0));
		assertEquals(4F,policy.damage(1,99));assertTrue(policy.cadenceRequired(1));assertFalse(policy.mayClaim(1,1));assertTrue(policy.mayClaim(0,1));
	}
	@Test void sinkRetainsContinuousContactWithoutRepeatedDamage() {
		var policy=new ContinentalSinkPolicy();assertTrue(policy.continuousContacts());assertFalse(policy.perTickAttack());
		assertTrue(policy.mayClaim(null,0));assertFalse(policy.mayClaim(0,0));assertFalse(policy.mayClaim(null,-1));
	}
}
