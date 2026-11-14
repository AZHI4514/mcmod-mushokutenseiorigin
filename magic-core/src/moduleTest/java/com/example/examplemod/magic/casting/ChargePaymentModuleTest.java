package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.application.casting.ChargePayment;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.spell.earth.stone_cannon.ChargeStep;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class ChargePaymentModuleTest {
	private static final class Fixture implements PlayerStore,ManaReservation {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("stone_cannon");int paid,budget;
		public PlayerMagicState read() { return state; }public void write(PlayerMagicState s) { state=s; }
		public int reserved() { return paid; }public void write(int p,int b) { paid=p;budget=b; }
		ChargePayment use() { return new ChargePayment(this,this); }
	}
	@Test void reserveDebitAndRepeatedCancelConserveMana() {
		var f=new Fixture();assertTrue(f.use().begin());assertEquals(90,f.state.mana());assertEquals(10,f.paid);assertEquals(100,f.budget);assertFalse(f.use().begin());
		var step=ChargeStep.advance(20,f.state.mana(),f.paid);f.use().debit(step,f.budget);assertEquals(100,f.state.mana()+f.paid);
		f.use().cancel();f.use().cancel();assertEquals(100,f.state.mana());assertEquals(0,f.paid);assertEquals(0,f.budget);
	}
	@Test void failedSpawnRefundsOnceAndSuccessDoesNotChargeAgain() {
		var f=new Fixture();f.use().begin();assertFalse(f.use().release(p->{assertEquals(10,p);return false;},10));f.use().cancel();assertEquals(100,f.state.mana());assertEquals(0,f.state.cooldown());
		assertTrue(f.use().begin());assertTrue(f.use().release(p->{assertEquals(90,f.state.mana());return true;},10));assertEquals(90,f.state.mana());assertEquals(10,f.state.cooldown());f.use().cancel();assertEquals(90,f.state.mana());assertFalse(f.use().release(p->{fail();return true;},10));
	}
	@Test void reconnectCanRefundPersistedBalanceWithoutRuntimeSession() {
		var f=new Fixture();f.use().begin();f.use().cancel();assertEquals(100,f.state.mana());assertEquals(0,f.paid);
	}
	@Test void spawnExceptionKeepsReservationForExplicitRecovery() {
		var f=new Fixture();f.use().begin();assertThrows(IllegalStateException.class,()->f.use().release(p->{throw new IllegalStateException();},10));assertEquals(10,f.paid);f.use().cancel();assertEquals(100,f.state.mana());
	}

	@Test void depletedSessionLaunchesAllPaidManaWithoutAnotherDebit() {
		var f=new Fixture();var session=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
		assertTrue(f.use().begin());int ticks=0;
		while(f.state.mana()>0) {
			session.heartbeat();assertFalse(session.expired(true));
			var step=session.advance(f.state.mana(),f.paid);f.use().debit(step,f.budget);
			assertEquals(100,f.state.mana()+f.paid);assertTrue(++ticks<=68);
		}
		assertEquals(68,ticks);
		assertTrue(f.use().release(paid->{assertEquals(100,paid);return true;},10));
		assertEquals(0,f.state.mana());assertEquals(0,f.paid);assertEquals(10,f.state.cooldown());
	}
	@Test void heartbeatTimeoutRefundsAccumulatedChargeAndAllowsFreshSession() {
		var f=new Fixture();var session=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
		assertTrue(f.use().begin());
		for(int i=0;i<10;i++) {
			assertFalse(session.expired(true));f.use().debit(session.advance(f.state.mana(),f.paid),f.budget);
		}
		assertTrue(f.paid>10);assertTrue(session.expired(true));
		f.use().cancel();f.use().cancel();assertEquals(100,f.state.mana());assertEquals(0,f.paid);
		assertTrue(f.use().begin());var fresh=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
		assertEquals(1,fresh.advance(f.state.mana(),f.paid).ticks());
	}

	@Test void eachEligibilityFailureCancelsAccumulatedPaymentExactlyOnce() {
		for(int condition=0;condition<8;condition++) {
			var f=new Fixture();var session=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
			assertTrue(f.use().begin());
			for(int i=0;i<8;i++) { session.heartbeat();assertFalse(session.expired(true));f.use().debit(session.advance(f.state.mana(),f.paid),f.budget); }
			assertTrue(f.paid>10);
			boolean[] facts={true,false,false,false,true,true,true,true};facts[condition]=!facts[condition];
			var eligibility=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeEligibility(facts[0],facts[1],facts[2],facts[3],facts[4],facts[5],facts[6],facts[7]);
			assertTrue(session.expired(eligibility.eligible()));
			f.use().cancel();f.use().cancel();assertEquals(100,f.state.mana());assertEquals(0,f.paid);assertEquals(0,f.state.cooldown());
		}
	}
	@Test void manualReleaseAfterChargingCommitsOnceAndRejectsRetry() {
		var f=new Fixture();var session=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
		assertTrue(f.use().begin());
		var eligible=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeEligibility(true,false,false,false,true,true,true,true);
		for(int i=0;i<20;i++) { session.heartbeat();assertFalse(session.expired(eligible.eligible()));f.use().debit(session.advance(f.state.mana(),f.paid),f.budget); }
		int paid=f.paid;int remaining=f.state.mana();assertTrue(paid>10);assertTrue(remaining>0);
		int[] launches={0};PaidProjectile projectile=p->{assertEquals(paid,p);launches[0]++;return true;};
		assertTrue(f.use().release(projectile,10));assertFalse(f.use().release(projectile,10));f.use().cancel();
		assertEquals(1,launches[0]);assertEquals(remaining,f.state.mana());assertEquals(100,remaining+paid);assertEquals(10,f.state.cooldown());
	}
	@Test void cancellingOneChargedPlayerCannotRefundAnother() {
		var first=new Fixture();var second=new Fixture();assertTrue(first.use().begin());assertTrue(second.use().begin());
		var session=new com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession();
		for(int i=0;i<10;i++)first.use().debit(session.advance(first.state.mana(),first.paid),first.budget);
		first.use().cancel();assertEquals(100,first.state.mana());assertEquals(90,second.state.mana());assertEquals(10,second.paid);
		assertTrue(second.use().release(p->p==10,10));assertEquals(90,second.state.mana());assertEquals(100,first.state.mana());
	}
}
