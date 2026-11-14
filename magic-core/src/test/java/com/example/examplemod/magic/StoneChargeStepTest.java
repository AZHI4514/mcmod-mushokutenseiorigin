package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.stone_cannon.ChargeStep;
import com.example.examplemod.magic.mechanic.channel.ChargeCurve;
class StoneChargeStepTest {
	@Test void hundredManaExhaustsAtOriginalSixtyEightTickBoundary() {
		int mana=90,reserved=10,ticks=0;
		while(mana>0) { var step=ChargeStep.advance(ticks,mana,reserved);mana-=step.debit();reserved=step.reserved();ticks=step.ticks();assertEquals(mana==0,step.release());assertEquals(100,mana+reserved); }
		assertEquals(68,ticks);assertEquals(100,reserved);assertEquals(19F,ChargeCurve.stoneDamage(reserved));
	}
	@Test void everyWholeManaDebitMatchesIntegralUntilExhaustion() {
		int mana=100000,reserved=10;
		for(int tick=0;tick<1000;tick++) { var step=ChargeStep.advance(tick,mana,reserved);assertEquals(ChargeCurve.extraAfterTicks(tick+1)-ChargeCurve.extraAfterTicks(tick),step.debit());mana-=step.debit();reserved=step.reserved(); }
	}
	@Test void zeroBalanceAndIntegerSaturationCannotOverflow() {
		assertTrue(ChargeStep.advance(0,0,10).release());
		var step=ChargeStep.advance(Integer.MAX_VALUE,100,Integer.MAX_VALUE);assertEquals(Integer.MAX_VALUE,step.ticks());assertEquals(0,step.debit());assertEquals(Integer.MAX_VALUE,step.reserved());assertFalse(step.release());
		var last=ChargeStep.advance(100,10,Integer.MAX_VALUE-1);assertEquals(1,last.debit());assertEquals(Integer.MAX_VALUE,last.reserved());
		assertThrows(IllegalArgumentException.class,()->ChargeStep.advance(-1,10,10));
	}
}
