package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.WaterfallChannel;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class WaterfallChannelModuleTest {
	private static final class Fixture implements PlayerStore,WaterSource {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("waterfall");
		int debt,begins,generations,renews,glows,queries;boolean first=true,target=true,generation=true;
		final List<String> events=new ArrayList<>();
		final WaterfallChannel channel=new WaterfallChannel(this,new ChannelDebt() { public int read() { return debt; }public void write(int value) { debt=value; } },this,()->{glows++;events.add("glow");});
		public PlayerMagicState read() { return state; }public void write(PlayerMagicState value) { state=value;events.add("state"); }
		public boolean begin(float y,float p) { begins++;return first; }
		public boolean targetAvailable(float y,float p) { queries++;events.add("target");return target; }
		public boolean generate(float y,float p) { generations++;events.add("generate");return generation; }
		public void renew() { renews++;events.add("renew"); }
	}
	@Test void initialFailureRestoresPaymentAndRepeatedBeginDoesNotRegenerate() {
		var f=new Fixture();f.debt=-3;f.first=false;var before=f.state;
		assertFalse(f.channel.begin(0,0));assertEquals(before,f.state);assertEquals(-3,f.debt);assertFalse(f.channel.stop());
		f.first=true;assertTrue(f.channel.begin(0,0));assertTrue(f.channel.begin(1,2));assertEquals(2,f.begins);assertEquals(99,f.state.mana());assertEquals(-19,f.debt);
	}
	@Test void twentiethTickRenewsBeforeGenerationAndChargesOnlyTheTickRate() {
		var f=new Fixture();assertTrue(f.channel.begin(0,0));
		for(int i=0;i<19;i++) { f.channel.heartbeat();assertFalse(f.channel.tick(true,0,0)); }
		assertEquals(0,f.generations);f.events.clear();f.channel.heartbeat();assertFalse(f.channel.tick(true,0,0));
		assertEquals(List.of("target","state","glow","renew","generate"),f.events);assertEquals(1,f.generations);assertEquals(20,f.renews);assertEquals(95,f.state.mana());assertEquals(-16,f.debt);
	}
	@Test void lostTargetAndIneligiblePlayerStopBeforePaymentAndNeverRenewAgain() {
		for(boolean loseTarget:new boolean[]{true,false}) {
			var f=new Fixture();assertTrue(f.channel.begin(0,0));f.target=!loseTarget;int mana=f.state.mana(),debt=f.debt;
			assertTrue(f.channel.tick(loseTarget,0,0));assertEquals(mana,f.state.mana());assertEquals(debt,f.debt);assertEquals(0,f.renews);assertEquals(10,f.state.cooldown());
			assertFalse(f.channel.tick(true,0,0));assertFalse(f.channel.stop());assertEquals(0,f.generations);
		}
	}
	@Test void seventhMissingHeartbeatStopsAndExplicitStopPreservesLargerCooldownAndCredit() {
		var f=new Fixture();assertTrue(f.channel.begin(0,0));for(int i=0;i<6;i++)assertFalse(f.channel.tick(true,0,0));
		int mana=f.state.mana(),debt=f.debt;assertTrue(f.channel.tick(true,0,0));assertEquals(mana,f.state.mana());assertEquals(debt,f.debt);assertEquals(6,f.renews);
		var other=new Fixture();assertTrue(other.channel.begin(0,0));other.state=other.state.withCooldown(30);int paidCredit=other.debt;assertTrue(other.channel.stop());assertEquals(30,other.state.cooldown());assertEquals(paidCredit,other.debt);assertEquals(99,other.state.mana());
	}
	@Test void finalPrepaidCreditRunsOutThenStopsWithoutCreatingMoreWater() {
		var f=new Fixture();f.state=f.state.spendMana(99);assertTrue(f.channel.begin(0,0));assertEquals(0,f.state.mana());
		for(int i=0;i<4;i++) { f.channel.heartbeat();assertFalse(f.channel.tick(true,0,0)); }
		assertEquals(0,f.debt);f.channel.heartbeat();assertTrue(f.channel.tick(true,0,0));assertEquals(4,f.renews);assertEquals(0,f.generations);assertEquals(10,f.state.cooldown());
	}
	@Test void invalidAimAndInsufficientEntryManaNeverCallWorldAndPeriodicFailureKeepsChannel() {
		var f=new Fixture();assertFalse(f.channel.begin(Float.NaN,0));assertFalse(f.channel.begin(0,91));assertEquals(0,f.begins);
		f.state=f.state.spendMana(100);f.debt=-16;assertFalse(f.channel.begin(0,0));assertEquals(0,f.begins);
		var other=new Fixture();assertTrue(other.channel.begin(0,0));other.generation=false;
		for(int i=0;i<21;i++) { other.channel.heartbeat();assertFalse(other.channel.tick(true,0,0)); }
		assertEquals(1,other.generations);assertEquals(21,other.renews);assertEquals(0,other.state.cooldown());assertTrue(other.channel.stop());
	}
	@Test void independentChannelsKeepHeartbeatPaymentAndSourcesIsolated() {
		var a=new Fixture();var b=new Fixture();assertTrue(a.channel.begin(0,0));assertTrue(b.channel.begin(0,0));
		for(int i=0;i<7;i++) { a.channel.heartbeat();assertFalse(a.channel.tick(true,0,0));assertEquals(i==6,b.channel.tick(true,0,0)); }
		assertEquals(7,a.renews);assertEquals(6,b.renews);assertEquals(0,a.state.cooldown());assertEquals(10,b.state.cooldown());
		int bMana=b.state.mana(),bDebt=b.debt;assertTrue(a.channel.stop());
		assertEquals(bMana,b.state.mana());assertEquals(bDebt,b.debt);assertFalse(b.channel.tick(true,0,0));
	}
	@Test void stoppedUseCaseCanBeRecreatedWithoutResettingPersistentCredit() {
		var f=new Fixture();assertTrue(f.channel.begin(0,0));int mana=f.state.mana(),credit=f.debt;
		assertTrue(f.channel.stop());f.channel.heartbeat();assertFalse(f.channel.tick(true,0,0));assertEquals(0,f.renews);
		f.state=f.state.withCooldown(0);
		var next=new WaterfallChannel(f,new ChannelDebt() { public int read(){return f.debt;}public void write(int value){f.debt=value;} },f,()->f.glows++);
		assertTrue(next.begin(0,0));assertEquals(mana,f.state.mana());assertEquals(credit+4,f.debt);
		assertFalse(f.channel.stop());next.heartbeat();assertFalse(next.tick(true,0,0));assertEquals(1,f.renews);
	}
}
