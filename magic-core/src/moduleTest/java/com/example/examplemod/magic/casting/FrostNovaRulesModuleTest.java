package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaRules;
class FrostNovaRulesModuleTest {
	@Test void wetDamageBonusDoesNotChangeDryDamage(){assertEquals(10,FrostNovaRules.damage(false));assertEquals(14,FrostNovaRules.damage(true));}
	@Test void playerControlIsHalfMobControl(){assertEquals(20,FrostNovaRules.controlTicks(true));assertEquals(40,FrostNovaRules.controlTicks(false));assertEquals(300,FrostNovaRules.FREEZE_TICKS);}
	@Test void radiusIncludesBoundaryAndPreservesNegatedOutsideComparison(){assertTrue(FrostNovaRules.inside(64));assertFalse(FrostNovaRules.inside(Math.nextUp(64.0)));assertTrue(FrostNovaRules.inside(Double.NaN));assertFalse(FrostNovaRules.inside(Double.POSITIVE_INFINITY));}
}
