package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.icicle_break.IcicleBreakSpec;
class IcicleBreakRulesTest {
	@Test void freezingMatchesExistingIcyRuleAcrossSamplesAndEligibility(){
		for(int i=0;i<=1000;i++)for(boolean canFreeze:new boolean[]{true,false})for(boolean warm:new boolean[]{true,false}) {
			float roll=i/1000F;assertEquals(roll<.8F&&canFreeze&&!warm?200:0,IcicleBreakSpec.freezeTicks(roll,canFreeze,warm));
		}
		assertEquals(0,IcicleBreakSpec.freezeTicks(Float.NaN,true,false));
	}
}
