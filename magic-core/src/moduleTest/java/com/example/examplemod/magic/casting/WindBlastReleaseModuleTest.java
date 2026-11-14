package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class WindBlastReleaseModuleTest {
 static final class Store implements PlayerStore {
  PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("wind_blast");
  public PlayerMagicState read(){return state;} public void write(PlayerMagicState s){state=s;}
 }
 static CastSelfEffectCommand command(Store s,SelfEffect effect,List<Runnable> observers){return new CastSelfEffectCommand("wind_blast",8,10,s,effect,observers,e->{});}
 @Test void preparationAndSingleReleaseKeepAimAndPayment(){
  var s=new Store();var session=new CastingSession(160);for(int i=1;i<=160;i++){session.heartbeat();assertEquals(i==160?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,s.state.mana());}
  var events=new ArrayList<String>();var c=command(s,(y,p)->{assertEquals(92,s.state.mana());assertEquals(10,s.state.cooldown());assertEquals(45,y);assertEquals(-30,p);events.add("launch");events.add("slowfall");return true;},List.of(()->events.add("recovery"),()->events.add("sequence"),()->events.add("glow"),()->events.add("sound")));
  assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(45,-30));assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));assertEquals(List.of("launch","slowfall","recovery","sequence","glow","sound"),events);
 }
 @Test void rejectedInputsNeverApply(){var s=new Store();SelfEffect never=(y,p)->{fail();return true;};assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(Float.NaN,0));assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,91));s.state=s.state.spendMana(93);assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,0));s.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,0));}
 @Test void explicitFalseRestoresFullState(){var s=new Store();var before=s.state;var c=command(s,(y,p)->false,List.of(()->fail()));assertEquals(CastSelfEffectCommand.Result.REJECTED,c.execute(0,0));assertSame(before,s.state);assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
 @Test void partialWorldFailureKeepsPaymentAndCannotReplay(){var s=new Store();var c=command(s,(y,p)->{throw new IllegalStateException();},List.of(()->fail()));assertThrows(IllegalStateException.class,()->c.execute(0,0));assertEquals(92,s.state.mana());assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
 @Test void observerFailureCannotUndoOrSuppressLaterNotification(){var s=new Store();var events=new ArrayList<String>();assertEquals(CastSelfEffectCommand.Result.SUCCESS,command(s,(y,p)->true,List.of(()->{throw new IllegalStateException();},()->events.add("sound"))).execute(0,0));assertEquals(92,s.state.mana());assertEquals(List.of("sound"),events);}
 @Test void cancelledPreparationNeverReleases(){var session=new CastingSession(160);session.cancel();for(int i=0;i<170;i++)assertNotEquals(CastingSession.Signal.READY,session.tick(true).signal());}
}
