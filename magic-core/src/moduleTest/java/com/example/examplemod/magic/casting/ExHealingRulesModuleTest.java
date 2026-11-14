package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import com.example.examplemod.magic.spell.healing.ex_healing.ExHealingRules;
class ExHealingRulesModuleTest {
	@Test void frozenHealingMatchesTenThousandHealthPairs(){var r=new Random(3700);for(int i=0;i<10000;i++){float h=r.nextFloat()*200-20,m=r.nextFloat()*200;float old=h<=0?0:Math.min(8F,Math.max(0,m-h));assertEquals(old,ExHealingRules.amount(h,m));}}
	@Test void fullDeadInvalidAndMissingHealthBoundaries(){assertEquals(0,ExHealingRules.amount(20,20));assertEquals(0,ExHealingRules.amount(0,20));assertEquals(0,ExHealingRules.amount(Float.NaN,20));assertEquals(0,ExHealingRules.amount(1,Float.POSITIVE_INFINITY));assertEquals(3,ExHealingRules.amount(17,20));assertEquals(8,ExHealingRules.amount(1,20));}
}
