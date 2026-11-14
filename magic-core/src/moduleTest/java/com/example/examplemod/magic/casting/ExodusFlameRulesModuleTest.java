package com.example.examplemod.magic.casting;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameRules;
class ExodusFlameRulesModuleTest {
	@Test void sphereIncludesBoundaryButExcludesJustOutside(){assertTrue(ExodusFlameRules.insideBurst(16));assertTrue(ExodusFlameRules.insideBurst(0));assertFalse(ExodusFlameRules.insideBurst(Math.nextUp(16.0)));}
	@Test void originalNegatedComparisonRetainsIeeeBehavior(){assertTrue(ExodusFlameRules.insideBurst(Double.NaN));assertTrue(ExodusFlameRules.insideBurst(Double.NEGATIVE_INFINITY));assertFalse(ExodusFlameRules.insideBurst(Double.POSITIVE_INFINITY));}
	@Test void frozenRadiusTestMatchesTenThousandDistances(){var random=new Random(4418);for(int i=0;i<10000;i++){double d=random.nextDouble()*100;assertEquals(!(d>16),ExodusFlameRules.insideBurst(d));}}
}
