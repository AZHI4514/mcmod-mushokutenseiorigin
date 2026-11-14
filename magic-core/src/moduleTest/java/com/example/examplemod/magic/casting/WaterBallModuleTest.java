package com.example.examplemod.magic.casting;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.catalog.SpellFactory;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.application.casting.CastSpellCommand;
import com.example.examplemod.magic.mechanic.projectile.*;

class WaterBallModuleTest {
	private static final ProjectileSpec SPEC = com.example.examplemod.magic.spell.water.water_ball.WaterBallSpec.PROJECTILE;
	private static final class MemoryPlayer implements PlayerStore {
		PlayerMagicState state = PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learn("water_ball");
		public PlayerMagicState read() { return state; }
		public void write(PlayerMagicState next) { state = next; }
	}
	private CastSpellCommand command(MemoryPlayer player, ProjectileSpawner spawner, List<VfxObserver> observers) {
		SpellFactory factory = new ProjectileSpellFactory();
		return new CastSpellCommand(SPEC, factory.create(SPEC), player, spawner, observers, error -> {});
	}
	@Test void completesExactly100TicksThenCommitsOnlyOnce() {
		var session = new CastingSession(100);
		var player = new MemoryPlayer();
		var events = new ArrayList<String>();
		var command = command(player, plan -> {
			assertEquals(99,player.state.mana());
			assertEquals(1.5F,plan.spec().speed());
			assertEquals(1F,plan.spec().inaccuracy());
			assertEquals(20F,plan.yaw());
			events.add("spawn"); return true;
		},List.of(event -> events.add("vfx")));
		for(int tick=1;tick<=100;tick++) {
			if(tick%2==0) session.heartbeat();
			var step=session.tick(true);
			assertEquals(100-tick,step.remaining());
			assertEquals(tick==100 ? CastingSession.Signal.READY : CastingSession.Signal.NONE,step.signal());
			assertEquals(100,player.state.mana());
		}
		assertEquals(CastSpellCommand.Result.SUCCESS,command.execute(new Vec3d(0,1.5,0),20,10));
		assertEquals(List.of("spawn","vfx"),events);
		assertEquals(CastSpellCommand.Result.ALREADY_EXECUTED,command.execute(Vec3d.ZERO,0,0));
		assertEquals(99,player.state.mana());
		assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
	}
	@Test void interruptedAndMissingHeartbeatNeverRelease() {
		var session=new CastingSession(100);
		for(int i=0;i<10;i++) assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		assertEquals(CastingSession.Signal.CANCELLED,session.tick(true).signal());
		assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		session=new CastingSession(100);
		session.heartbeat(); session.tick(true);
		assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());
		assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		assertEquals(CastingSession.Signal.CANCELLED,new CastingSession(100).tick(false).signal());
	}
	@Test void insufficientManaAndInvalidAimHaveNoSideEffects() {
		var player=new MemoryPlayer();player.state=player.state.spendMana(100);
		var command=command(player,plan->{fail("Must not spawn");return false;},List.of());
		assertEquals(CastSpellCommand.Result.REJECTED,command.execute(Vec3d.ZERO,0,0));
		assertEquals(0,player.state.mana());
		player=new MemoryPlayer();
		command=command(player,plan->{fail("Must not spawn");return false;},List.of());
		assertEquals(CastSpellCommand.Result.REJECTED,command.execute(Vec3d.ZERO,Float.NaN,0));
		assertEquals(100,player.state.mana());
	}
	@Test void failedSpawnRestoresStateAndPublishesNothing() {
		var player=new MemoryPlayer();var before=player.state;
		var command=command(player,plan->false,List.of(event->fail("No success notification")));
		assertEquals(CastSpellCommand.Result.SPAWN_FAILED,command.execute(Vec3d.ZERO,0,0));
		assertEquals(before,player.state);
		var throwing=command(player,plan->{throw new IllegalStateException("spawn failed");},List.of());
		assertThrows(IllegalStateException.class,()->throwing.execute(Vec3d.ZERO,0,0));
		assertEquals(before,player.state);
	}
	@Test void observerFailureCannotRefundAlreadySpawnedProjectile() {
		var player=new MemoryPlayer();var events=new ArrayList<String>();
		var command=command(player,plan->true,List.of(event->{throw new IllegalStateException();},event->events.add("later observer")));
		assertEquals(CastSpellCommand.Result.SUCCESS,command.execute(Vec3d.ZERO,0,0));
		assertEquals(99,player.state.mana());
		assertEquals(List.of("later observer"),events);
	}
}
