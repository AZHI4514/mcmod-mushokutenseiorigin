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
class IceLanceModuleTest {
	private static final class Fixture implements PlayerStore,LocalEffect {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("ice_lance");
		boolean target=true,placed=true,partial;int queries,placements;final List<String> events=new ArrayList<>();
		public PlayerMagicState read(){return state;}public void write(PlayerMagicState value){state=value;}
		public Optional<Vec3d> locate(float yaw,float pitch){queries++;assertEquals(100,state.mana());return target?Optional.of(new Vec3d(.5,2,.5)):Optional.empty();}
		public boolean place(Vec3d base,float yaw,float pitch){placements++;assertEquals(90,state.mana());assertEquals(10,state.cooldown());assertEquals(2,base.y);if(partial)throw new IllegalStateException();return placed;}
		CastLocalEffectCommand command(List<Runnable> observers){return new CastLocalEffectCommand("ice_lance",10,10,this,this,observers,error->events.add("error"));}
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
	@Test void preparationReleaseAndSavedAgeProduceOnePaidHitWithoutEarlyDamage(){
		var f=new Fixture();var hits=new ArrayList<Integer>();var ages=new ArrayList<Integer>();
		var targets=new LocalHitTargets<Integer>() {
			public Iterable<Integer> candidates(){return List.of(7);}
			public com.example.examplemod.magic.domain.model.Box3d bounds(Integer id){return new com.example.examplemod.magic.domain.model.Box3d(.4,2,.4,.6,3,.6);}
			public boolean visible(Integer id){return true;}
			public void damage(Integer id){hits.add(id);}
		};
		var effect=new LocalEffect() {
			public Optional<Vec3d> locate(float y,float p){return f.locate(y,p);}
			public boolean place(Vec3d base,float y,float p){assertTrue(f.place(base,y,p));ages.add(0);return true;}
		};
		var cmd=new CastLocalEffectCommand("ice_lance",10,10,f,effect,List.of(()->f.events.add("sequence")),error->fail());
		var session=new CastingSession(160);
		for(int t=0;t<160;t++){
			session.heartbeat();var step=session.tick(true);
			if(step.signal()==CastingSession.Signal.READY)assertEquals(CastLocalEffectCommand.Result.SUCCESS,cmd.execute(0,0));
			else {assertTrue(ages.isEmpty());assertEquals(100,f.state.mana());}
			assertTrue(hits.isEmpty());
		}
		assertEquals(List.of(0),ages);assertEquals(List.of("sequence"),f.events);
		for(int restoredAge:new int[]{0,1,5,11})com.example.examplemod.magic.spell.water.ice_lance.IceLanceHit.apply(restoredAge+1,new Vec3d(.5,3.5,.5),new Vec3d(0,0,1),targets);
		assertEquals(List.of(7),hits);assertEquals(90,f.state.mana());assertEquals(1,f.placements);
	}
}
