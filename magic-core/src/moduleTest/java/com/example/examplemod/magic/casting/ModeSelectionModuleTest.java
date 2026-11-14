package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.ModeSelection;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
import com.example.examplemod.magic.spell.wind.atmosphere_dominion.AtmosphereDominionRules;
import com.example.examplemod.magic.spell.special.gravity_magic.GravityMagicRules;
class ModeSelectionModuleTest {
	@Test void modeClampAndCyclingRetainAllChoices(){for(int count=2;count<=4;count++){assertEquals(0,ModeSelection.normalized(-4,count));assertEquals(count-1,ModeSelection.normalized(99,count));for(int i=0;i<count;i++)assertEquals((i+1)%count,ModeSelection.next(i,count));}}
	@Test void nuclearExtraClampsWithoutChangingBaseCost(){assertEquals(3000,NuclearExplosionRules.cost(-1));assertEquals(6000,NuclearExplosionRules.cost(9000));assertEquals(4500,NuclearExplosionRules.cost(1500));}
	@Test void nuclearInterpolationPreservesFloatOrder(){for(int extra=0;extra<=3000;extra++){assertEquals(24+extra*8.0/3000,NuclearExplosionRules.radius(extra));assertEquals(60+extra*20F/3000,NuclearExplosionRules.damage(extra));}}
	@Test void detonationOnlyAfterWarningAndOnce(){assertFalse(NuclearExplosionRules.detonationDue(99,false));assertTrue(NuclearExplosionRules.detonationDue(100,false));assertFalse(NuclearExplosionRules.detonationDue(100,true));}
	@Test void geometryRetainsComparisonIncludingNan(){assertTrue(AtmosphereDominionRules.inside(2304,64));assertFalse(AtmosphereDominionRules.inside(Math.nextUp(2304d),64));assertTrue(GravityMagicRules.inside(36,12));assertFalse(GravityMagicRules.inside(36,-.01));assertTrue(GravityMagicRules.inside(Double.NaN,Double.NaN));}
}
