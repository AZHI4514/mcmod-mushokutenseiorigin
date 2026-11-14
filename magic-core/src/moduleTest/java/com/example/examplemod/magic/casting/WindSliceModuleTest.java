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
import com.example.examplemod.magic.spell.wind.wind_slice.WindSliceSpec;
class WindSliceModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState state = PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("wind_slice");
		public PlayerMagicState read() { return state; }
		public void write(PlayerMagicState value) { state=value; }
	}
	private CastSpellCommand command(Player player, ProjectileSpawner spawner, List<VfxObserver> observers) {
		var spec=WindSliceSpec.PROJECTILE;
		return new CastSpellCommand(spec,new ProjectileSpellFactory().create(spec),player,spawner,observers,error->fail(error));
	}
	@Test void preparesAndReleasesExactlyOnceWithCorrectIdentityAndCost() {
		var player=new Player();var session=new CastingSession(WindSliceSpec.PREPARATION_TICKS);var calls=new int[2];
		for(int tick=1;tick<=160;tick++) { session.heartbeat();assertEquals(tick==160?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,player.state.mana()); }
		var command=command(player,plan->{ assertEquals("wind_slice",plan.spec().id().value());assertEquals(2F,plan.spec().speed());assertEquals(0F,plan.spec().inaccuracy());assertEquals(92,player.state.mana());calls[0]++;return true; },List.of(plan->calls[1]++));
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
		player.state=player.state.learnWithoutRequirements("wind_slice");before=player.state;
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,Float.NaN,0));assertEquals(before,player.state);
	}
	@Test void cancellationAndExpiryDoNotCommit() {
		var session=new CastingSession(160);assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		var timeout=new CastingSession(160);for(int i=0;i<10;i++)timeout.tick(true);assertEquals(CastingSession.Signal.CANCELLED,timeout.tick(true).signal());
	}
	@Test void paidReleaseSharesAreaHistoryAcrossRestorationWithoutPayingAgain(){
		var player=new Player();var plans=new java.util.ArrayList<com.example.examplemod.magic.mechanic.projectile.ProjectileSpawnPlan>();
		var cmd=command(player,plan->{plans.add(plan);return true;},List.of());
		assertEquals(CastSpellCommand.Result.SUCCESS,cmd.execute(Vec3d.ZERO,0,0));
		var a=new java.util.UUID(0,1);var b=new java.util.UUID(0,2);var c=new java.util.UUID(0,3);
		var hits=new java.util.ArrayList<java.util.UUID>();var history=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory();
		assertTrue(history.claim(a));
		com.example.examplemod.magic.spell.wind.wind_slice.WindSliceHit.apply(history,new com.example.examplemod.magic.spell.wind.wind_slice.WindSliceHit.Targets<java.util.UUID>(){
			public Iterable<java.util.UUID> candidates(){return List.of(a,b);}
			public java.util.UUID identity(java.util.UUID id){return id;}
			public boolean direct(java.util.UUID id){return id.equals(a);}
			public void damage(java.util.UUID id){hits.add(id);}
		});
		var restored=new com.example.examplemod.magic.mechanic.projectile.ImpactHistory(history.snapshot());
		assertFalse(com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.contactDue(1,2,restored.contains(b)));
		assertTrue(restored.claim(c));assertEquals(List.of(a,b),hits);assertEquals(92,player.state.mana());
		assertEquals(CastSpellCommand.Result.ALREADY_EXECUTED,cmd.execute(Vec3d.ZERO,0,0));assertEquals(1,plans.size());
		assertTrue(com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules.expired(120,120));
	}

}
