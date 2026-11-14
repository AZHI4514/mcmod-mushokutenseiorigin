package com.example.examplemod.magic.casting;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSpellCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpellFactory;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import com.example.examplemod.magic.spell.water.icicle_edge.IcicleEdgeSpec;
class IcicleEdgeModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState state = PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("icicle_edge");
		public PlayerMagicState read() { return state; }
		public void write(PlayerMagicState value) { state=value; }
	}
	private CastSpellCommand command(Player player, ProjectileSpawner spawner, List<VfxObserver> observers) {
		var spec=IcicleEdgeSpec.PROJECTILE;
		return new CastSpellCommand(spec,new ProjectileSpellFactory().create(spec),player,spawner,observers,error->fail(error));
	}
	@Test void preparesAndReleasesExactlyOnceWithCorrectIdentityAndCost() {
		var player=new Player();var session=new CastingSession(IcicleEdgeSpec.PREPARATION_TICKS);var calls=new int[2];
		for(int tick=1;tick<=160;tick++) { session.heartbeat();assertEquals(tick==160?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,player.state.mana()); }
		var command=command(player,plan->{ assertEquals("icicle_edge",plan.spec().id().value());assertEquals(3F,plan.spec().speed());assertEquals(0F,plan.spec().inaccuracy());assertEquals(90,player.state.mana());calls[0]++;return true; },List.of(plan->calls[1]++));
		assertEquals(CastSpellCommand.Result.SUCCESS,command.execute(Vec3d.ZERO,25,-15));
		assertEquals(CastSpellCommand.Result.ALREADY_EXECUTED,command.execute(Vec3d.ZERO,25,-15));
		assertArrayEquals(new int[]{1,1},calls);assertEquals(10,player.state.cooldown());
	}
	@Test void failedSpawnAndExceptionRestoreExactSnapshotWithoutNotification() {
		var player=new Player();var before=player.state;
		assertEquals(CastSpellCommand.Result.SPAWN_FAILED,command(player,plan->false,List.of(plan->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.state);
		assertThrows(IllegalStateException.class,()->command(player,plan->{throw new IllegalStateException("spawn");},List.of(plan->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.state);
	}
	@Test void rejectsInsufficientManaUnknownKnowledgeAndInvalidAimBeforeSpawn() {
		var player=new Player();player.state=player.state.spendMana(93);var before=player.state;
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,0));assertEquals(before,player.state);
		player.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,0));
		player.state=player.state.learnWithoutRequirements("icicle_edge");before=player.state;
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,Float.NaN,0));assertEquals(before,player.state);
	}
	@Test void cancellationAndExpiryDoNotCommit() {
		var session=new CastingSession(160);assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		var timeout=new CastingSession(160);for(int i=0;i<10;i++)timeout.tick(true);assertEquals(CastingSession.Signal.CANCELLED,timeout.tick(true).signal());
	}
	@Test void preparedReleaseKeepsSavedBudgetAndVictimsAcrossCoreReconstruction() {
		var player=new Player();var session=new CastingSession(160);var spawned=new java.util.ArrayList<com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan>();
		var cmd=command(player,plan->{spawned.add(plan);return true;},List.of());
		for(int t=0;t<160;t++){session.heartbeat();var step=session.tick(true);if(step.signal()==CastingSession.Signal.READY)assertEquals(CastSpellCommand.Result.SUCCESS,cmd.execute(new Vec3d(1,65,2),47,-23));else assertTrue(spawned.isEmpty());}
		assertEquals(1,spawned.size());var plan=spawned.getFirst();assertEquals(65,plan.origin().y);assertEquals(47,plan.yaw());assertEquals(-23,plan.pitch());
		var victim=java.util.UUID.randomUUID();var history=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory();int attempts=0,age=0;double travelled=0;
		while(!com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.expired(age,120)) {
			++age;
			if(com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.candidate(1,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,history.contains(victim))&&com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.contactDue(1,plan.spec().speed(),history.contains(victim))){assertTrue(history.claim(victim));attempts++;}
			travelled+=plan.spec().speed();
			if(age==37)history=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory(history.snapshot());
		}
		assertEquals(120,age);assertEquals(360,travelled);assertEquals(1,attempts);assertEquals(90,player.state.mana());
		assertFalse(com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.expired(36,37));assertTrue(com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.expired(37,37));
	}
}
