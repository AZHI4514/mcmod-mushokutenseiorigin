package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.channel.*;
class ChannelCostTest {
	@Test void frozenLegacyDebitAndDebtMatchForEverySavedCreditAndBothSpellRates() {
		for(int initial=-19;initial<=0;initial++)for(int rate:new int[]{4,6}) {
			int debt=initial,paid=0;
			for(int tick=1;tick<=200;tick++) {
				int oldDebit=(debt+rate+19)/20,oldDebt=debt+rate-oldDebit*20;
				var cost=ChannelCost.calculate(debt,rate);assertEquals(oldDebit,cost.debit());assertEquals(oldDebt,cost.debt());
				paid+=cost.debit();debt=cost.debt();assertEquals(initial+tick*rate,paid*20+debt);
			}
		}
	}
	@Test void switchingRatesPreservesPrepaidCreditAndNeverCreatesWholeMana() {
		int debt=0,paid=0,units=0;
		for(int i=0;i<200;i++) { int rate=i%2==0?4:6;units+=rate;var cost=ChannelCost.calculate(debt,rate);paid+=cost.debit();debt=cost.debt();assertEquals(units,paid*20+debt);assertTrue(debt>=-19&&debt<=0); }
		assertEquals(50,paid);assertEquals(0,debt);
	}
	@Test void maximumRateDoesNotOverflowAndInvalidSavedDebtIsRejected() {
		var cost=ChannelCost.calculate(0,Integer.MAX_VALUE);assertEquals(107374183,cost.debit());assertEquals(-13,cost.debt());
		assertThrows(IllegalArgumentException.class,()->ChannelCost.calculate(-20,4));assertThrows(IllegalArgumentException.class,()->ChannelCost.calculate(1,4));assertThrows(IllegalArgumentException.class,()->ChannelCost.calculate(0,-1));
	}
	@Test void heartbeatExpiresOnSeventhTickAndGenerationEveryTwentyPaidTicks() {
		var session=new ChannelSession();for(int i=0;i<6;i++)assertFalse(session.expired(true));assertTrue(session.expired(true));session.heartbeat();assertFalse(session.expired(true));assertTrue(session.expired(false));
		for(int i=1;i<=200;i++)assertEquals(i%20==0,session.generationDue());
		var fresh=new ChannelSession();assertFalse(fresh.generationDue());
	}
}
