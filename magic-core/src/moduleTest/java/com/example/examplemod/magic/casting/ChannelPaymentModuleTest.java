package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.application.casting.ChannelPayment;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import com.example.examplemod.magic.mechanic.channel.ChannelSession;
class ChannelPaymentModuleTest {
	private static final class Fixture implements PlayerStore {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());int debt;
		public PlayerMagicState read() { return state; }public void write(PlayerMagicState value) { state=value; }
		// ChannelDebt has a distinct adapter because it exposes an integer, not PlayerStore.read().
		ChannelPayment payment() { return new ChannelPayment(this,new ChannelDebt() { public int read() { return debt; }public void write(int value) { debt=value; } }); }
		public void write(int value) { debt=value; }
	}
	@Test void firstEffectFailureRestoresExactStateAndPersistedCredit() {
		var f=new Fixture();f.debt=-3;var before=f.state;
		assertFalse(f.payment().begin(4,()->{assertEquals(99,f.state.mana());assertEquals(-19,f.debt);return false;}));assertEquals(before,f.state);assertEquals(-3,f.debt);
	}
	@Test void twoHundredPaymentsCostFortyManaAndGenerationTicksDoNotChargeTwice() {
		var f=new Fixture();int[] generations={0};assertTrue(f.payment().begin(4,()->{generations[0]++;return true;}));var session=new ChannelSession();
		for(int i=1;i<200;i++) { session.heartbeat();assertFalse(session.expired(true));assertTrue(f.payment().pay(4));if(session.generationDue())generations[0]++; }
		assertEquals(60,f.state.mana());assertEquals(0,f.debt);assertEquals(10,generations[0]);
	}
	@Test void prepaidCreditStillWorksAtZeroManaThenUnaffordablePaymentWritesNothing() {
		var f=new Fixture();f.state=f.state.spendMana(100);f.debt=-16;
		for(int i=0;i<4;i++)assertTrue(f.payment().pay(4));assertEquals(0,f.debt);var before=f.state;
		assertFalse(f.payment().begin(4,()->{fail();return true;}));assertEquals(before,f.state);assertEquals(0,f.debt);
	}
	@Test void restartingPaymentUseCaseAndChangingRateRetainFractionalBalance() {
		var f=new Fixture();assertTrue(f.payment().begin(4,()->true));assertEquals(-16,f.debt);assertEquals(99,f.state.mana());
		assertTrue(f.payment().begin(6,()->true));assertEquals(-10,f.debt);assertEquals(99,f.state.mana());
		var other=new Fixture();assertEquals(0,other.debt);assertEquals(100,other.state.mana());
	}
	@Test void unknownPartialEffectExceptionDoesNotGrantFreeWorldEffects() {
		var f=new Fixture();assertThrows(IllegalStateException.class,()->f.payment().begin(4,()->{throw new IllegalStateException();}));assertEquals(99,f.state.mana());assertEquals(-16,f.debt);
	}
}
