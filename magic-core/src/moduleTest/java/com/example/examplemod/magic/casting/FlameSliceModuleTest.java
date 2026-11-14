package com.example.examplemod.magic.casting;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSpellCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.mechanic.projectile.*;
import com.example.examplemod.magic.spell.fire.flame_slice.*;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
class FlameSliceModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState value=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("flame_slice");
		public PlayerMagicState read() { return value; }
		public void write(PlayerMagicState state) { value=state; }
	}
	private CastSpellCommand command(Player player,ProjectileSpawner spawn,List<VfxObserver> observers) {
		var spec=FlameSliceSpec.PROJECTILE;return new CastSpellCommand(spec,new ProjectileSpellFactory().create(spec),player,spawn,observers,error->fail(error));
	}
	@Test void preparesThenReleasesRequestedAnglesWithoutScatterAndCommitsOnce() {
		var player=new Player();var session=new CastingSession(100);var count=new int[2];
		for(int t=1;t<=100;t++) { session.heartbeat();assertEquals(t==100?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,player.value.mana()); }
		var cmd=command(player,plan->{assertEquals("flame_slice",plan.spec().id().value());assertEquals(1085,plan.yaw());assertEquals(-90,plan.pitch());assertEquals(2F,plan.spec().speed());assertEquals(0F,plan.spec().inaccuracy());assertEquals(64,plan.origin().y);assertEquals(96,player.value.mana());count[0]++;return true;},List.of(plan->count[1]++));
		assertEquals(CastSpellCommand.Result.SUCCESS,cmd.execute(new Vec3d(1,64,2),1085,-90));assertEquals(CastSpellCommand.Result.ALREADY_EXECUTED,cmd.execute(Vec3d.ZERO,0,0));assertArrayEquals(new int[]{1,1},count);assertEquals(0,player.value.cooldown());
	}
	@Test void spawnFailureAndExceptionRollbackBeforeAnyNotification() {
		var player=new Player();var before=player.value;
		assertEquals(CastSpellCommand.Result.SPAWN_FAILED,command(player,p->false,List.of(p->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.value);
		assertThrows(IllegalStateException.class,()->command(player,p->{throw new IllegalStateException();},List.of(p->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.value);
	}
	@Test void invalidAnglesInsufficientManaAndCancellationNeverSpawn() {
		var player=new Player();assertEquals(CastSpellCommand.Result.REJECTED,command(player,p->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,91));assertEquals(100,player.value.mana());
		player.value=player.value.spendMana(97);assertEquals(CastSpellCommand.Result.REJECTED,command(player,p->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,0));assertEquals(3,player.value.mana());
		var session=new CastingSession(100);assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
	}
}
