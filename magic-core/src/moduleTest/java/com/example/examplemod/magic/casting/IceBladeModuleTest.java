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
import com.example.examplemod.magic.spell.water.ice_blade.IceBladeSpec;
class IceBladeModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState state = PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learnWithoutRequirements("ice_blade");
		public PlayerMagicState read() { return state; }
		public void write(PlayerMagicState value) { state=value; }
	}
	private CastSpellCommand command(Player player, ProjectileSpawner spawner, List<VfxObserver> observers) {
		var spec=IceBladeSpec.PROJECTILE;
		return new CastSpellCommand(spec,new ProjectileSpellFactory().create(spec),player,spawner,observers,error->fail(error));
	}
	@Test void preparesAndReleasesExactlyOnceWithCorrectIdentityAndCost() {
		var player=new Player();var session=new CastingSession(100);var calls=new int[2];
		for(int tick=1;tick<=100;tick++) { session.heartbeat();assertEquals(tick==100?CastingSession.Signal.READY:CastingSession.Signal.NONE,session.tick(true).signal());assertEquals(100,player.state.mana()); }
		var command=command(player,plan->{ assertEquals("ice_blade",plan.spec().id().value());assertEquals(2.5F,plan.spec().speed());assertEquals(0F,plan.spec().inaccuracy());assertEquals(95,player.state.mana());calls[0]++;return true; },List.of(plan->calls[1]++));
		assertEquals(CastSpellCommand.Result.SUCCESS,command.execute(Vec3d.ZERO,25,-15));
		assertEquals(CastSpellCommand.Result.ALREADY_EXECUTED,command.execute(Vec3d.ZERO,25,-15));
		assertArrayEquals(new int[]{1,1},calls);assertEquals(0,player.state.cooldown());
	}
	@Test void failedSpawnAndExceptionRestoreExactSnapshotWithoutNotification() {
		var player=new Player();var before=player.state;
		assertEquals(CastSpellCommand.Result.SPAWN_FAILED,command(player,plan->false,List.of(plan->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.state);
		assertThrows(IllegalStateException.class,()->command(player,plan->{throw new IllegalStateException("spawn");},List.of(plan->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.state);
	}
	@Test void rejectsInsufficientManaUnknownKnowledgeAndInvalidAimBeforeSpawn() {
		var player=new Player();player.state=player.state.spendMana(96);var before=player.state;
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,0));assertEquals(before,player.state);
		player.state=PlayerMagicState.initial(100,FrozenLearningCatalog.create());
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,0));
		player.state=player.state.learnWithoutRequirements("ice_blade");before=player.state;
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,Float.NaN,0));assertEquals(before,player.state);
	}
	@Test void cancellationAndHeartbeatExpiryNeverRelease() {
		var session=new CastingSession(100);assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		var timeout=new CastingSession(100);for(int i=0;i<10;i++)timeout.tick(true);assertEquals(CastingSession.Signal.CANCELLED,timeout.tick(true).signal());
		assertEquals(5F,IceBladeSpec.DAMAGE);assertEquals(.02,IceBladeSpec.GRAVITY);
	}

	@Test void preservesEffectThresholdAndVanillaEligibilityInputs() {
		assertEquals(100,com.example.examplemod.magic.spell.water.ice_blade.IceBladeImpactPolicy.statusTicks(Math.nextDown(.8F),true,false));
		assertEquals(0,com.example.examplemod.magic.spell.water.ice_blade.IceBladeImpactPolicy.statusTicks(.8F,true,false));
		assertEquals(0,com.example.examplemod.magic.spell.water.ice_blade.IceBladeImpactPolicy.statusTicks(Float.NaN,true,false));
		assertEquals(0,com.example.examplemod.magic.spell.water.ice_blade.IceBladeImpactPolicy.statusTicks(0,false,false));
		assertEquals(0,com.example.examplemod.magic.spell.water.ice_blade.IceBladeImpactPolicy.statusTicks(0,true,true));
	}
}
