package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.FlamethrowerChannel;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class FlamethrowerChannelModuleTest {
	private static final class Fixture implements PlayerStore {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("flamethrower");
		int debt,emissions;boolean fail;final List<String> events=new ArrayList<>();
		final ChannelDebt credit=new ChannelDebt(){public int read(){return debt;}public void write(int value){debt=value;}};
		FlamethrowerChannel channel=create();
		public PlayerMagicState read(){return state;}public void write(PlayerMagicState value){state=value;}
		FlamethrowerChannel create(){return new FlamethrowerChannel(this,credit,(yaw,pitch)->{events.add("emit");emissions++;if(fail)throw new IllegalStateException("world");},()->events.add("glow"));}
		void begin(){assertTrue(channel.begin(0,0));events.add("registered");channel.emit(0,0);events.add("sound");}
	}
	@Test void entryPaymentPrecedesRegistrationGlowEmissionAndSoundAndTwentyEmissionsCostSixMana(){
		var f=new Fixture();assertThrows(IllegalStateException.class,()->f.channel.emit(0,0));f.begin();
		assertEquals(List.of("registered","glow","emit","sound"),f.events);assertEquals(99,f.state.mana());assertEquals(-14,f.debt);
		for(int i=1;i<20;i++){f.channel.heartbeat();assertFalse(f.channel.tick(true,20,-10));}
		assertEquals(20,f.emissions);assertEquals(94,f.state.mana());assertEquals(0,f.debt);assertEquals(0,f.state.cooldown());
	}
	@Test void repeatedBeginOnlyRefreshesHeartbeatAndDoesNotRepayOrEmit(){
		var f=new Fixture();f.begin();int mana=f.state.mana(),debt=f.debt;
		assertTrue(f.channel.begin(30,20));assertEquals(mana,f.state.mana());assertEquals(debt,f.debt);assertEquals(1,f.emissions);
	}
	@Test void seventhMissingHeartbeatStopsBeforePaymentAndEmission(){
		var f=new Fixture();f.begin();for(int i=0;i<6;i++)assertFalse(f.channel.tick(true,0,0));
		int mana=f.state.mana(),debt=f.debt;assertTrue(f.channel.tick(true,0,0));assertEquals(7,f.emissions);
		assertEquals(mana,f.state.mana());assertEquals(debt,f.debt);assertEquals(10,f.state.cooldown());assertFalse(f.channel.tick(true,0,0));assertFalse(f.channel.stop());
	}
	@Test void eligibilityLossAndBadAimStopWithoutAdditionalEffects(){
		for(boolean eligible:new boolean[]{false,true}){
			var f=new Fixture();f.begin();int mana=f.state.mana(),debt=f.debt;
			assertTrue(f.channel.tick(eligible,eligible?Float.NaN:0,0));assertEquals(1,f.emissions);assertEquals(mana,f.state.mana());assertEquals(debt,f.debt);assertEquals(10,f.state.cooldown());
		}
	}
	@Test void finalCreditCanBeSpentAtZeroManaButNewEntryStillRequiresOneMana(){
		var f=new Fixture();f.state=f.state.spendMana(99);f.begin();assertEquals(0,f.state.mana());
		for(int i=0;i<2;i++){f.channel.heartbeat();assertFalse(f.channel.tick(true,0,0));}
		assertEquals(-2,f.debt);f.channel.heartbeat();assertTrue(f.channel.tick(true,0,0));assertEquals(-2,f.debt);assertEquals(3,f.emissions);
		f.state=f.state.withCooldown(0);var next=f.create();assertFalse(next.begin(0,0));assertEquals(3,f.emissions);
	}
	@Test void stopPreservesLargerCooldownAndPersistentCreditAcrossRecreation(){
		var f=new Fixture();f.begin();f.state=f.state.withCooldown(30);int mana=f.state.mana(),credit=f.debt;
		assertTrue(f.channel.stop());assertEquals(30,f.state.cooldown());assertEquals(credit,f.debt);assertFalse(f.channel.stop());
		f.state=f.state.withCooldown(0);var next=f.create();assertTrue(next.begin(0,0));assertEquals(mana,f.state.mana());assertEquals(credit+6,f.debt);
	}
	@Test void invalidOrUnlearnedEntryNeverPaysAndWorldExceptionKeepsCommittedPayment(){
		var f=new Fixture();assertFalse(f.channel.begin(Float.NaN,0));assertFalse(f.channel.begin(0,91));assertEquals(100,f.state.mana());assertEquals(0,f.debt);
		f.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());assertFalse(f.channel.begin(0,0));assertTrue(f.events.isEmpty());
		var other=new Fixture();other.fail=true;assertTrue(other.channel.begin(0,0));
		assertThrows(IllegalStateException.class,()->other.channel.emit(0,0));assertEquals(99,other.state.mana());assertEquals(-14,other.debt);
		other.channel.heartbeat();assertThrows(IllegalStateException.class,()->other.channel.tick(true,0,0));assertEquals(-8,other.debt);assertTrue(other.channel.stop());
	}
	@Test void playersHaveIndependentPaymentHeartbeatAndStopState(){
		var a=new Fixture();var b=new Fixture();a.begin();b.begin();
		for(int i=0;i<7;i++){a.channel.heartbeat();assertFalse(a.channel.tick(true,0,0));assertEquals(i==6,b.channel.tick(true,0,0));}
		assertEquals(8,a.emissions);assertEquals(7,b.emissions);assertEquals(0,a.state.cooldown());assertEquals(10,b.state.cooldown());
		int mana=b.state.mana(),credit=b.debt;assertTrue(a.channel.stop());assertEquals(mana,b.state.mana());assertEquals(credit,b.debt);
	}
	@Test void stopAndRestartPreserveVictimWindowWhileNewChannelSpendsExistingCredit(){
		var f=new Fixture();var now=new long[]{100};var hits=new ArrayList<Long>();
		var contacts=new com.example.examplemod.magic.mechanic.field.ContactCooldowns(20);
		var source=UUID.randomUUID();var victim=UUID.randomUUID();
		var targets=new com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit.Targets<UUID>(){
			public Iterable<UUID> candidates(){return List.of(victim);}
			public com.example.examplemod.magic.domain.model.Vec3d delta(UUID target){return new com.example.examplemod.magic.domain.model.Vec3d(0,0,2);}
			public boolean visible(UUID target){return true;}
			public boolean claim(UUID target){return contacts.claim(source,target,now[0]);}
			public void damage(UUID target){hits.add(now[0]);}
		};
		FlameStream stream=(yaw,pitch)->com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit.apply(new com.example.examplemod.magic.domain.model.Vec3d(0,0,1),targets);
		var first=new FlamethrowerChannel(f,f.credit,stream,()->{});assertTrue(first.begin(0,0));first.emit(0,0);assertTrue(first.stop());
		f.state=f.state.withCooldown(0);now[0]=105;
		var second=new FlamethrowerChannel(f,f.credit,stream,()->{});assertTrue(second.begin(0,0));second.emit(0,0);
		now[0]=119;second.heartbeat();assertFalse(second.tick(true,0,0));now[0]=120;second.heartbeat();assertFalse(second.tick(true,0,0));
		assertEquals(List.of(100L,120L),hits);assertEquals(98,f.state.mana());assertEquals(-16,f.debt);
		assertFalse(first.tick(true,0,0));assertTrue(second.stop());assertEquals(10,f.state.cooldown());
	}

}
