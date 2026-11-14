package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.magic.application.port.PlayerStore;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.spell.detox.detoxification.DetoxificationEffect;
import com.example.examplemod.magic.testing.FrozenLearningCatalog;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DetoxificationCommandModuleTest {
	static class Player implements PlayerStore {
		PlayerMagicState state=PlayerMagicState.initial(100,FrozenLearningCatalog.create()).learn("detoxification");
		boolean poisoned;int removals,observed;
		public PlayerMagicState read(){return state;}public void write(PlayerMagicState next){state=next;}
		boolean remove(){removals++;boolean before=poisoned;poisoned=false;return before;}
	}
	CastSelfEffectCommand command(Player p){var s=SpellDefinition.DETOXIFICATION;return new CastSelfEffectCommand(s.id(),s.manaCost(),s.cooldownTicks(),p,(yaw,pitch)->DetoxificationEffect.apply(p::remove),List.of(()->p.observed++),error->{throw error;});}
	@Test void noPoisonRestoresExactPaymentSnapshotAndSuppressesSuccess(){
		var p=new Player();var before=p.state;var c=command(p);assertEquals(CastSelfEffectCommand.Result.REJECTED,c.execute(0,0));assertSame(before,p.state);assertEquals(1,p.removals);assertEquals(0,p.observed);assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));assertEquals(1,p.removals);
	}
	@Test void poisonRemovalChargesOnceAndNotifiesOnlyAfterRemoval(){
		var p=new Player();p.poisoned=true;var c=command(p);assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(0,0));assertFalse(p.poisoned);assertEquals(100-SpellDefinition.DETOXIFICATION.manaCost(),p.state.mana());assertEquals(1,p.observed);assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));assertEquals(1,p.removals);
	}
	@Test void invalidAimNeverTouchesPatientOrPayment(){var p=new Player();p.poisoned=true;var before=p.state;assertEquals(CastSelfEffectCommand.Result.REJECTED,command(p).execute(Float.NaN,0));assertSame(before,p.state);assertTrue(p.poisoned);assertEquals(0,p.removals);}
}
