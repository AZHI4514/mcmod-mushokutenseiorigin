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
class FlamePillarModuleTest {
	private static final class Fixture implements PlayerStore,LocalEffect {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("flame_pillar");
		boolean target=true,placed=true,partial;int queries,placements;final List<String> events=new ArrayList<>();
		public PlayerMagicState read(){return state;}public void write(PlayerMagicState value){state=value;}
		public Optional<Vec3d> locate(float yaw,float pitch){queries++;assertEquals(100,state.mana());return target?Optional.of(new Vec3d(.5,2,.5)):Optional.empty();}
		public boolean place(Vec3d base,float yaw,float pitch){placements++;assertEquals(88,state.mana());assertEquals(10,state.cooldown());assertEquals(2,base.y);if(partial)throw new IllegalStateException();return placed;}
		CastLocalEffectCommand command(List<Runnable> observers){return new CastLocalEffectCommand("flame_pillar",12,10,this,this,observers,error->events.add("error"));}
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
		var f=new Fixture();f.partial=true;var cmd=f.command(List.of(()->fail()));assertThrows(IllegalStateException.class,()->cmd.execute(0,0));assertEquals(88,f.state.mana());assertEquals(CastLocalEffectCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));
	}
	@Test void observerFailureCannotRefundOrSuppressLaterObservers(){
		var f=new Fixture();var cmd=f.command(List.of(()->{throw new IllegalStateException();},()->f.events.add("sequence")));
		assertEquals(CastLocalEffectCommand.Result.SUCCESS,cmd.execute(0,0));assertEquals(88,f.state.mana());assertEquals(List.of("error","sequence"),f.events);
	}
	@Test void cancellationAndHeartbeatExpiryNeverReachReadyOrSpend(){
		var f=new Fixture();var session=new CastingSession(160);session.tick(true);session.cancel();
		for(int i=0;i<200;i++)assertNotEquals(CastingSession.Signal.READY,session.tick(true).signal());
		var expired=new CastingSession(160);for(int i=0;i<11;i++)expired.tick(true);
		assertNotEquals(CastingSession.Signal.READY,expired.tick(true).signal());assertEquals(100,f.state.mana());assertEquals(0,f.placements);
	}
	@Test void releasePaysOnceForIgnitionAndTwoLaterPulsesWithoutRepeatingFire(){
		var f=new Fixture();var tasks=new ArrayList<com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn>();
		var effects=new ArrayList<String>();
		var effect=new LocalEffect(){
			public Optional<Vec3d> locate(float yaw,float pitch){return f.locate(yaw,pitch);}
			public boolean place(Vec3d base,float yaw,float pitch){
				assertTrue(f.place(base,yaw,pitch));
				com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarIgnition.apply(new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarIgnition.World(){
					public void ignite(int ticks){effects.add("ignite:"+ticks);}
					public void damage(){effects.add("damage");}
					public void placeFire(){effects.add("fire");}
					public void burst(){effects.add("burst");}
					public void schedule(){tasks.add(new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn(100));}
				});return true;
			}
		};
		var cmd=new CastLocalEffectCommand("flame_pillar",12,10,f,effect,List.of(()->effects.add("released")),error->fail(error));
		assertEquals(CastLocalEffectCommand.Result.SUCCESS,cmd.execute(0,0));assertEquals(1,tasks.size());
		var target=new com.example.examplemod.magic.spell.fire.flame_pillar.FlamePillarBurn.Target(){
			public boolean alive(){return true;}
			public boolean sameLevel(){return true;}
			public void damage(){effects.add("damage");}
		};
		for(long now=100;now<=160;now++)assertEquals(now==160,tasks.getFirst().tick(now,target));
		assertEquals(List.of("ignite:60","damage","fire","burst","released","damage","damage"),effects);
		assertEquals(88,f.state.mana());assertEquals(10,f.state.cooldown());assertEquals(1,f.placements);
		assertEquals(CastLocalEffectCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));
	}

}
