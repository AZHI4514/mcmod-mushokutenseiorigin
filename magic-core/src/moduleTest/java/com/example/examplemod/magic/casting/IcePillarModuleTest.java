package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastTargetedAreaCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import com.example.examplemod.magic.spell.water.ice_pillar.*;
class IcePillarModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState value=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements(IcePillarSpec.ID);
		public PlayerMagicState read() { return value; }public void write(PlayerMagicState s) { value=s; }
	}
	private CastTargetedAreaCommand command(Player p,TargetedArea area,List<Runnable> observers) {
		return new CastTargetedAreaCommand(IcePillarSpec.ID,IcePillarSpec.MANA,IcePillarSpec.RECOVERY,p,area,observers,e->{});
	}
	@Test void preparationThenValidatedBaseThenPaymentThenColumnAndObservers() {
		var p=new Player();var session=new CastingSession(IcePillarSpec.PREPARATION_TICKS);var events=new ArrayList<String>();
		for(int i=1;i<=160;i++) { session.heartbeat();assertEquals(i==160?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,p.value.mana()); }
		var area=new TargetedArea() {
			public Optional<Vec3d> locate(float y,float pitch) { events.add("preflight");assertEquals(100,p.value.mana());return Optional.of(new Vec3d(-2,3,4)); }
			public void apply(Vec3d base,float y,float pitch) { events.add("column");assertEquals(92,p.value.mana());assertEquals(10,p.value.cooldown());var box=IcePillarRules.bounds(base);assertEquals(-2,box.minX);assertEquals(6,box.maxY); }
		};
		var cmd=command(p,area,List.of(()->events.add("notify")));assertEquals(CastTargetedAreaCommand.Result.SUCCESS,cmd.execute(30,-20));
		assertEquals(CastTargetedAreaCommand.Result.ALREADY_EXECUTED,cmd.execute(30,-20));assertEquals(List.of("preflight","column","notify"),events);
	}
	@Test void missingTargetOrFailedPlacementPreflightDoesNotPayOrNotify() {
		var p=new Player();var before=p.value;
		var area=new TargetedArea() { public Optional<Vec3d> locate(float y,float pitch) { return Optional.empty(); }public void apply(Vec3d c,float y,float pitch) { fail(); } };
		assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(p,area,List.of(()->fail())).execute(0,0));assertEquals(before,p.value);
		p.value=p.value.spendMana(93);
		var forbidden=new TargetedArea() { public Optional<Vec3d> locate(float y,float pitch) { fail();return Optional.empty(); }public void apply(Vec3d c,float y,float pitch) { fail(); } };
		assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(p,forbidden,List.of()).execute(0,0));assertEquals(7,p.value.mana());
	}
	@Test void cancelledOrExpiredPreparationNeverSignalsReady() {
		var p=new Player();var session=new CastingSession(160);session.heartbeat();session.tick(true);assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());
		for(int i=0;i<200;i++)assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,p.value.mana());
		var timeout=new CastingSession(160);for(int i=0;i<10;i++)assertEquals(CastingSession.Signal.NONE,timeout.tick(true).signal());assertEquals(CastingSession.Signal.CANCELLED,timeout.tick(true).signal());
	}
	@Test void irreversibleColumnFailureKeepsPaymentAndObserverFailureDoesNotRepeatColumn() {
		var p=new Player();int[] applications={0},notices={0};
		var area=new TargetedArea() { public Optional<Vec3d> locate(float y,float pitch) { return Optional.of(Vec3d.ZERO); }public void apply(Vec3d c,float y,float pitch) { applications[0]++; } };
		var cmd=command(p,area,List.of(()->{throw new IllegalStateException();},()->notices[0]++));
		assertEquals(CastTargetedAreaCommand.Result.SUCCESS,cmd.execute(0,0));assertEquals(1,applications[0]);assertEquals(1,notices[0]);assertEquals(92,p.value.mana());
		var q=new Player();var broken=new TargetedArea() { public Optional<Vec3d> locate(float y,float pitch) { return Optional.of(Vec3d.ZERO); }public void apply(Vec3d c,float y,float pitch) { throw new IllegalStateException("partial column"); } };
		var failed=command(q,broken,List.of(()->fail()));assertThrows(IllegalStateException.class,()->failed.execute(0,0));assertEquals(92,q.value.mana());assertEquals(CastTargetedAreaCommand.Result.ALREADY_EXECUTED,failed.execute(0,0));
	}
}
