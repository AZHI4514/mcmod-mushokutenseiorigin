package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastLocalEffectCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class WindBindModuleTest {
	private static final class Fixture implements PlayerStore,LocalEffect {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("wind_bind");
		boolean target=true,placed=true,partial;int queries,placements;final List<String> events=new ArrayList<>();
		public PlayerMagicState read(){return state;}public void write(PlayerMagicState value){state=value;}
		public Optional<Vec3d> locate(float yaw,float pitch){queries++;assertEquals(100,state.mana());return target?Optional.of(new Vec3d(.5,2,.5)):Optional.empty();}
		public boolean place(Vec3d base,float yaw,float pitch){placements++;assertEquals(90,state.mana());assertEquals(10,state.cooldown());assertEquals(2,base.y);if(partial)throw new IllegalStateException();return placed;}
		CastLocalEffectCommand command(List<Runnable> observers){return new CastLocalEffectCommand("wind_bind",10,10,this,this,observers,error->events.add("error"));}
	}
	@Test void preparationAndSuccessCommitOnceThenNotifyInOrder(){
		var f=new Fixture();var session=new CastingSession(160);
		for(int tick=1;tick<=160;tick++){session.heartbeat();assertEquals(tick==160?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,f.state.mana());}
		var cmd=f.command(List.of(()->f.events.add("recovery"),()->f.events.add("sequence"),()->f.events.add("glow"),()->f.events.add("sound")));
		assertEquals(CastLocalEffectCommand.Result.SUCCESS,cmd.execute(45,-30));assertEquals(CastLocalEffectCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));
		assertEquals(List.of("recovery","sequence","glow","sound"),f.events);assertEquals(1,f.placements);
	}
	@Test void missingTargetDoesNotPayAndFalsePlacementRestoresExactState(){
		var f=new Fixture();f.target=false;var before=f.state;
		assertEquals(CastLocalEffectCommand.Result.REJECTED,f.command(List.of(()->fail())).execute(0,0));assertEquals(before,f.state);assertEquals(0,f.placements);
		f.target=true;f.placed=false;var cmd=f.command(List.of(()->fail()));assertEquals(CastLocalEffectCommand.Result.REJECTED,cmd.execute(0,0));assertEquals(before,f.state);assertEquals(CastLocalEffectCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));
	}
	@Test void invalidAimInsufficientManaAndUnknownSpellNeverQueryWorld(){
		var f=new Fixture();assertEquals(CastLocalEffectCommand.Result.REJECTED,f.command(List.of()).execute(Float.NaN,0));assertEquals(CastLocalEffectCommand.Result.REJECTED,f.command(List.of()).execute(0,91));
		f.state=f.state.spendMana(91);assertEquals(CastLocalEffectCommand.Result.REJECTED,f.command(List.of()).execute(0,0));
		f.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());assertEquals(CastLocalEffectCommand.Result.REJECTED,f.command(List.of()).execute(0,0));assertEquals(0,f.queries);
	}
	@Test void unknownPartialEffectKeepsPaymentAndDoesNotNotifyOrRetry(){
		var f=new Fixture();f.partial=true;var cmd=f.command(List.of(()->fail()));assertThrows(IllegalStateException.class,()->cmd.execute(0,0));assertEquals(90,f.state.mana());assertEquals(CastLocalEffectCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));
	}
	@Test void observerFailureCannotRefundOrSuppressLaterObservers(){
		var f=new Fixture();var cmd=f.command(List.of(()->{throw new IllegalStateException();},()->f.events.add("sequence")));
		assertEquals(CastLocalEffectCommand.Result.SUCCESS,cmd.execute(0,0));assertEquals(90,f.state.mana());assertEquals(List.of("error","sequence"),f.events);
	}
	@Test void cancellationAndHeartbeatExpiryNeverReachReadyOrSpend(){
		var f=new Fixture();var session=new CastingSession(160);session.tick(true);session.cancel();
		for(int i=0;i<200;i++)assertNotEquals(CastingSession.Signal.READY,session.tick(true).signal());
		var expired=new CastingSession(160);for(int i=0;i<11;i++)expired.tick(true);
		assertNotEquals(CastingSession.Signal.READY,expired.tick(true).signal());assertEquals(100,f.state.mana());assertEquals(0,f.placements);
	}
	@Test void paidReleaseAppliesEffectThenVisualAndFiveDeferredPulses(){
		var f=new Fixture();var events=new ArrayList<String>();
		var effect=new LocalEffect(){
			public Optional<Vec3d> locate(float y,float p){return f.locate(y,p);}
			public boolean place(Vec3d base,float y,float p){assertTrue(f.place(base,y,p));return com.example.examplemod.magic.spell.wind.wind_bind.WindBindEffect.apply(new com.example.examplemod.magic.spell.wind.wind_bind.WindBindEffect.Target(){
				public boolean apply(int ticks){assertEquals(100,ticks);events.add("effect");return true;}
				public void showBound(){events.add("visual");}
			});}
		};
		var cmd=new CastLocalEffectCommand("wind_bind",10,10,f,effect,List.of(()->events.add("released")),error->fail(error));
		assertEquals(CastLocalEffectCommand.Result.SUCCESS,cmd.execute(0,0));assertEquals(List.of("effect","visual","released"),events);
		for(int ticks=100;ticks>=1;ticks--)if(com.example.examplemod.magic.spell.wind.wind_bind.WindBindSpec.pulse(ticks))events.add("damage");
		assertEquals(5,Collections.frequency(events,"damage"));assertEquals(90,f.state.mana());assertEquals(10,f.state.cooldown());
		assertEquals(CastLocalEffectCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));assertEquals(1,f.placements);
	}

}
