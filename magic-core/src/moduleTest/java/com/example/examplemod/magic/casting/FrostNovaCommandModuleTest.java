package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class FrostNovaCommandModuleTest {
 static final class Store implements PlayerStore {
  PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("frost_nova");
  public PlayerMagicState read(){return state;} public void write(PlayerMagicState s){state=s;}
 }
 static CastSelfEffectCommand command(Store s,SelfEffect effect,List<Runnable> observers){return new CastSelfEffectCommand("frost_nova",40,20,s,effect,observers,e->{});}
 @Test void preparationAndSingleReleaseKeepAimAndPayment(){
  var s=new Store();var session=new CastingSession(240);for(int i=1;i<=240;i++){session.heartbeat();assertEquals(i==240?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,s.state.mana());}
  var events=new ArrayList<String>();var c=command(s,(y,p)->{assertEquals(60,s.state.mana());assertEquals(20,s.state.cooldown());assertEquals(45,y);assertEquals(-30,p);events.add("nova");return true;},List.of(()->events.add("recovery"),()->events.add("sequence"),()->events.add("glow"),()->events.add("sound")));
  assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(45,-30));assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));assertEquals(List.of("nova","recovery","sequence","glow","sound"),events);
 }
 @Test void rejectedInputsNeverApply(){var s=new Store();SelfEffect never=(y,p)->{fail();return true;};assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(Float.NaN,0));assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,91));s.state=s.state.spendMana(93);assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,0));s.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());assertEquals(CastSelfEffectCommand.Result.REJECTED,command(s,never,List.of()).execute(0,0));}
 @Test void explicitFalseRestoresFullState(){var s=new Store();var before=s.state;var c=command(s,(y,p)->false,List.of(()->fail()));assertEquals(CastSelfEffectCommand.Result.REJECTED,c.execute(0,0));assertSame(before,s.state);assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
 @Test void partialWorldFailureKeepsPaymentAndCannotReplay(){var s=new Store();var c=command(s,(y,p)->{throw new IllegalStateException();},List.of(()->fail()));assertThrows(IllegalStateException.class,()->c.execute(0,0));assertEquals(60,s.state.mana());assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
 @Test void observerFailureCannotUndoOrSuppressLaterNotification(){var s=new Store();var events=new ArrayList<String>();assertEquals(CastSelfEffectCommand.Result.SUCCESS,command(s,(y,p)->true,List.of(()->{throw new IllegalStateException();},()->events.add("sound"))).execute(0,0));assertEquals(60,s.state.mana());assertEquals(List.of("sound"),events);}
 @Test void cancelledPreparationNeverReleases(){var session=new CastingSession(240);session.cancel();for(int i=0;i<250;i++)assertNotEquals(CastingSession.Signal.READY,session.tick(true).signal());}

 @Test void paidReleaseConsumesWetOnlyAfterSuccessfulDamage(){
  for(boolean accepted:List.of(true,false)){
   var store=new Store();var target=new FrostNovaHitModuleTest.Target(){public boolean hurt(float d){assertEquals(60,store.state.mana());return super.hurt(d);}};target.wet=true;target.damage=accepted;
   var c=command(store,(y,p)->{com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaHit.apply(target);return true;},List.of());
   assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(0,0));assertEquals(!accepted,target.wet);assertEquals(60,store.state.mana());assertEquals(accepted,target.events.contains("after"));
  }
 }
 @Test void partialStatusConsumptionFailureKeepsPaymentAndPreventsReplay(){
  var store=new Store();var target=new FrostNovaHitModuleTest.Target(){public void removeWet(){super.removeWet();throw new IllegalStateException();}};target.wet=true;
  var c=command(store,(y,p)->{com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaHit.apply(target);return true;},List.of(()->fail()));
  assertThrows(IllegalStateException.class,()->c.execute(0,0));assertFalse(target.wet);assertEquals(60,store.state.mana());assertEquals("removeWet",target.events.getLast());assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));
 }

}
