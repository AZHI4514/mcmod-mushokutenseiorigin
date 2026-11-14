package com.example.examplemod.magic.casting;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastTargetedAreaCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import com.example.examplemod.magic.spell.water.ice_fortress.IceFortressSpec;
class IceFortressModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState value=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("ice_fortress");
		public PlayerMagicState read() { return value; }public void write(PlayerMagicState state) { value=state; }
	}
	private CastTargetedAreaCommand command(Player player,TargetedArea area,List<Runnable> observers) {
		return new CastTargetedAreaCommand("ice_fortress",14,10,player,area,observers,error->{});
	}
	@Test void completesPreparationAndCommitsExactlyOnceInOrder() {
		var player=new Player();var events=new ArrayList<String>();var session=new CastingSession(160);
		for(int t=1;t<=160;t++) { session.heartbeat();assertEquals(t==160?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,player.value.mana()); }
		var area=new TargetedArea() {
			public Optional<Vec3d> locate(float yaw,float pitch) { assertEquals(100,player.value.mana());assertEquals(45,yaw);events.add("locate");return Optional.of(new Vec3d(1,2,3)); }
			public void apply(Vec3d center,float yaw,float pitch) { assertEquals(86,player.value.mana());assertEquals(10,player.value.cooldown());assertEquals(2,center.y);events.add("apply"); }
		};
		var cmd=command(player,area,List.of(()->events.add("notify")));assertEquals(CastTargetedAreaCommand.Result.SUCCESS,cmd.execute(45,-30));assertEquals(CastTargetedAreaCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));assertEquals(List.of("locate","apply","notify"),events);
	}
	@Test void unloadedPointAndReadOnlyLookupExceptionDoNotCharge() {
		var player=new Player();var before=player.value;
		var area=new TargetedArea() { public Optional<Vec3d> locate(float y,float p) { return Optional.empty(); }public void apply(Vec3d c,float y,float p) { fail(); } };
		assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(player,area,List.of(()->fail())).execute(0,0));assertEquals(before,player.value);
		var broken=new TargetedArea() { public Optional<Vec3d> locate(float y,float p) { throw new IllegalStateException(); }public void apply(Vec3d c,float y,float p) { fail(); } };
		assertThrows(IllegalStateException.class,()->command(player,broken,List.of()).execute(0,0));assertEquals(before,player.value);
	}
	@Test void rejectedAimManaAndKnowledgeNeverQueryWorld() {
		var player=new Player();var area=new TargetedArea() { public Optional<Vec3d> locate(float y,float p) { fail();return Optional.empty(); }public void apply(Vec3d c,float y,float p) { fail(); } };
		assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(player,area,List.of()).execute(Float.NaN,0));assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(player,area,List.of()).execute(0,91));
		player.value=player.value.spendMana(93);assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(player,area,List.of()).execute(0,0));
		player.value=PlayerMagicState.initial(100,FrozenLearningCatalog.create());assertEquals(CastTargetedAreaCommand.Result.REJECTED,command(player,area,List.of()).execute(0,0));
	}
	@Test void failureAfterWorldApplicationBeginsCannotRefundPartialDamage() {
		var player=new Player();var area=new TargetedArea() { public Optional<Vec3d> locate(float y,float p) { return Optional.of(Vec3d.ZERO); }public void apply(Vec3d c,float y,float p) { throw new IllegalStateException("partial effect"); } };
		var cmd=command(player,area,List.of(()->fail()));assertThrows(IllegalStateException.class,()->cmd.execute(0,0));assertEquals(86,player.value.mana());assertEquals(10,player.value.cooldown());assertEquals(CastTargetedAreaCommand.Result.ALREADY_EXECUTED,cmd.execute(0,0));
	}
	@Test void observerFailureDoesNotRefundAndRemainingObserverStillRuns() {
		var player=new Player();var count=new int[1];var area=new TargetedArea() { public Optional<Vec3d> locate(float y,float p) { return Optional.of(Vec3d.ZERO); }public void apply(Vec3d c,float y,float p) { } };
		assertEquals(CastTargetedAreaCommand.Result.SUCCESS,command(player,area,List.of(()->{throw new IllegalStateException();},()->count[0]++)).execute(0,0));assertEquals(1,count[0]);assertEquals(86,player.value.mana());
	}
	@Test void cancellationAndHeartbeatExpiryCannotReachReady() {
		var session=new CastingSession(160);assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());var timeout=new CastingSession(160);for(int i=0;i<10;i++)timeout.tick(true);assertEquals(CastingSession.Signal.CANCELLED,timeout.tick(true).signal());
	}
}
