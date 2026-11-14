package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.barrier.*;
import com.example.examplemod.magic.spell.barrier.dual_shield.DualShieldRules;
import com.example.examplemod.magic.spell.barrier.spirit_sealing_barrier.SpiritSealingBarrierRules;
import com.example.examplemod.magic.spell.barrier.confinement_barrier.ConfinementBarrierRules;
class BarrierCapacityModuleTest {
	@Test void poolsAreIndependentAndBreakAtZero(){var c=new BarrierCapacity(160,24,24);assertFalse(c.absorbPhysical(10));assertEquals(24,c.magical());assertEquals(160,c.shell());assertTrue(c.absorbPhysical(14));assertTrue(c.absorbMagical(25));assertEquals(-1,c.magical());}
	@Test void noNewClampChangesNegativeOrNanInputs(){var c=new BarrierCapacity(10,24,24);assertFalse(c.absorbShell(-5));assertEquals(15,c.shell());assertFalse(c.absorbShell(Float.NaN));assertTrue(Float.isNaN(c.shell()));}
	@Test void dualModeKeepsUnknownModeCompatibility(){assertTrue(DualShieldRules.intercepts(0,false));assertFalse(DualShieldRules.intercepts(0,true));assertTrue(DualShieldRules.intercepts(1,true));assertFalse(DualShieldRules.intercepts(1,false));assertTrue(DualShieldRules.intercepts(7,true));assertTrue(DualShieldRules.intercepts(7,false));}
	@Test void spiritShellOpenDistanceAndMinimumStrike(){assertFalse(SpiritSealingBarrierRules.strikesShell(7));assertTrue(SpiritSealingBarrierRules.strikesShell(8));assertFalse(SpiritSealingBarrierRules.strikesShell(9));assertEquals(1,SpiritSealingBarrierRules.strikeCost(0));assertEquals(4,SpiritSealingBarrierRules.strikeCost(4));}
	@Test void confinementRetainsPlayerDurationAndBackoff(){assertEquals(100,ConfinementBarrierRules.lifetime(true,500));assertEquals(500,ConfinementBarrierRules.lifetime(false,500));assertEquals(0,ConfinementBarrierRules.clippedFraction(.0005));assertEquals(.499,ConfinementBarrierRules.clippedFraction(.5));}
	static class Field implements BarrierDamage.Field {StringBuilder log=new StringBuilder();boolean active=true,accepts=true,crosses=true,blocks=true,broken;public boolean active(){log.append('a');return active;}public boolean acceptsKind(){log.append('k');return accepts;}public boolean crosses(){log.append('c');return crosses;}public boolean blocksDamage(){log.append('b');return blocks;}public boolean absorb(){log.append('s');return broken;}public void discard(){log.append('d');}public void visual(){log.append('v');}}
	@Test void crossingAbsorbsThenDrawsEvenWhenBroken(){var f=new Field();f.broken=true;assertTrue(BarrierDamage.intercept(f));assertEquals("akcbsdv",f.log.toString());}
	@Test void spiritCrossingDoesNotGiveDamageImmunity(){var f=new Field();f.blocks=false;assertFalse(BarrierDamage.intercept(f));assertEquals("akcb",f.log.toString());}
	@Test void rejectedKindDoesNotTrace(){var f=new Field();f.accepts=false;assertFalse(BarrierDamage.intercept(f));assertEquals("ak",f.log.toString());}
}
