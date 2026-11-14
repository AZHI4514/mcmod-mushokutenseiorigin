package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import com.example.examplemod.magic.spell.wind.sonic_blast.*;
class SonicBlastCommandModuleTest {
	static final class Store implements PlayerStore {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("sonic_blast");
		public PlayerMagicState read(){return state;}public void write(PlayerMagicState value){state=value;}
	}
	static CastSelfEffectCommand command(Store s,SelfEffect e,List<Runnable> o){return new CastSelfEffectCommand("sonic_blast",40,20,s,e,o,error->{});}
	@Test void prepares240TicksThenPaysOnceBeforeHitTerrainAndObservers(){
		var s=new Store();var session=new CastingSession(240);
		for(int i=1;i<=240;i++){session.heartbeat();assertEquals(i==240?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,s.state.mana());}
		var waveEvents=new ArrayList<String>();
		var target=new SonicBlastHitModuleTest.Target(){public boolean hurt(){assertEquals(60,s.state.mana());assertEquals(20,s.state.cooldown());waveEvents.add("hit");return super.hurt();}};
		var c=command(s,(yaw,pitch)->{assertEquals(45,yaw);assertEquals(-30,pitch);SonicBlastHit.apply(target,1);SonicBlastTerrain.apply(List.of(1),new SonicBlastTerrain.World<Integer>(){public boolean visible(Integer b){waveEvents.add("visible");return true;}public boolean breakBlock(Integer b){waveEvents.add("break");return true;}});waveEvents.add("burst");return true;},List.of(()->waveEvents.add("recovery"),()->waveEvents.add("sequence"),()->waveEvents.add("glow"),()->waveEvents.add("sound")));
		assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(45,-30));assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));assertEquals(List.of("hit","visible","break","burst","recovery","sequence","glow","sound"),waveEvents);
	}
	@Test void rejectedDamageIsStillACommittedWave(){var s=new Store();var t=new SonicBlastHitModuleTest.Target();t.accepted=false;assertEquals(CastSelfEffectCommand.Result.SUCCESS,command(s,(y,p)->{SonicBlastHit.apply(t,1);return true;},List.of()).execute(0,0));assertEquals(60,s.state.mana());assertEquals(List.of("hurt"),t.events);}
	@Test void partialTerrainFailureKeepsPaymentAndCannotReplay(){var s=new Store();var w=new SonicBlastTerrainModuleTest.World(){public boolean breakBlock(Integer b){if(b==2)throw new IllegalStateException();return super.breakBlock(b);}};var c=command(s,(y,p)->{SonicBlastTerrain.apply(List.of(1,2,3),w);return true;},List.of(()->fail()));assertThrows(IllegalStateException.class,()->c.execute(0,0));assertEquals(List.of(1),w.broken);assertEquals(60,s.state.mana());assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
	@Test void invalidAimKnowledgeOrManaNeverRunsEffect(){var s=new Store();SelfEffect never=(y,p)->{fail();return true;};assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(Float.NaN,0));assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,91));s.state=s.state.spendMana(61);assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,0));s.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,0));}
	@Test void explicitFalseRestoresStateAndObserverFailureDoesNotRefundSuccess(){var s=new Store();var before=s.state;assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,(y,p)->false,List.of(()->fail())).execute(0,0));assertSame(before,s.state);var events=new ArrayList<String>();assertEquals(CastSelfEffectCommand.Result.SUCCESS,command(s,(y,p)->true,List.of(()->{throw new IllegalStateException();},()->events.add("sound"))).execute(0,0));assertEquals(60,s.state.mana());assertEquals(List.of("sound"),events);}
	@Test void cancellationNeverBecomesReady(){var session=new CastingSession(240);session.cancel();for(int i=0;i<250;i++)assertNotEquals(CastingSession.Signal.READY,session.tick(true).signal());}
}
