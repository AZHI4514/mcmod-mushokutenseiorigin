package com.example.examplemod.magic.casting;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSpellCommand;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.domain.casting.CastingSession;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.mechanic.projectile.*;
import com.example.examplemod.magic.spell.fire.fireball.*;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;

class FireballModuleTest {
	private static final class Player implements PlayerStore {
		PlayerMagicState value=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learn("fireball");
		public PlayerMagicState read() { return value; }
		public void write(PlayerMagicState state) { value=state; }
	}
	private CastSpellCommand command(Player player,ProjectileSpawner spawner,List<VfxObserver> observers) {
		var spec=FireballSpec.PROJECTILE;
		return new CastSpellCommand(spec,new ProjectileSpellFactory().create(spec),player,spawner,observers,error->{});
	}
	@Test void releasesAfter100TicksWithFireballIdentityAndExactCost() {
		var player=new Player(); var session=new CastingSession(100);var events=new ArrayList<String>();
		var command=command(player,plan->{assertEquals("fireball",plan.spec().id().value());assertEquals(1.5F,plan.spec().speed());assertEquals(97,player.value.mana());events.add("spawn");return true;},List.of(plan->events.add("released")));
		for(int t=1;t<=100;t++) { if(t%2==0)session.heartbeat();var step=session.tick(true);assertEquals(t==100?CastingSession.Signal.READY:CastingSession.Signal.NONE,step.signal());assertEquals(100,player.value.mana()); }
		assertEquals(CastSpellCommand.Result.SUCCESS,command.execute(Vec3d.ZERO,30,-20));
		assertEquals(0,player.value.cooldown());assertEquals(List.of("spawn","released"),events);
		assertEquals(CastSpellCommand.Result.ALREADY_EXECUTED,command.execute(Vec3d.ZERO,30,-20));assertEquals(97,player.value.mana());
	}
	@Test void insufficientManaAndFailedSpawnNeverBecomeFreeProjectiles() {
		var player=new Player(); player.value=player.value.spendMana(98);
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,0));
		assertEquals(2,player.value.mana());
		player=new Player();var before=player.value;
		assertEquals(CastSpellCommand.Result.SPAWN_FAILED,command(player,plan->false,List.of(plan->fail())).execute(Vec3d.ZERO,0,0));assertEquals(before,player.value);
	}
	@Test void cancellationInvalidAnglesAndMissingHeartbeatDoNotCharge() {
		var player=new Player();var session=new CastingSession(100);session.tick(true);
		assertEquals(CastingSession.Signal.CANCELLED,session.cancel().signal());assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
		var timeout=new CastingSession(100);for(int i=0;i<10;i++)timeout.tick(true);assertEquals(CastingSession.Signal.CANCELLED,timeout.tick(true).signal());
		assertEquals(CastSpellCommand.Result.REJECTED,command(player,plan->{fail();return false;},List.of()).execute(Vec3d.ZERO,0,91));assertEquals(100,player.value.mana());
	}
	@Test void ignitionKeepsExactFloatThresholdAndImmunityRules() {
		assertEquals(100,FireballImpactPolicy.ignitionTicks(Math.nextDown(.8F),false,false));
		assertEquals(0,FireballImpactPolicy.ignitionTicks(.8F,false,false));
		assertEquals(0,FireballImpactPolicy.ignitionTicks(0,true,false));
		assertEquals(0,FireballImpactPolicy.ignitionTicks(0,false,true));
		assertEquals(0,FireballImpactPolicy.ignitionTicks(Float.NaN,false,false));
		assertEquals(3F,FireballSpec.DAMAGE);assertEquals(.01,FireballSpec.GRAVITY);
	}
}
