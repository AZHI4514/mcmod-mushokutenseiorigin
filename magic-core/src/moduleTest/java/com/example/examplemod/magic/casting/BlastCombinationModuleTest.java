package com.example.examplemod.magic.casting;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.magic.spell.mixed.blast.BlastHit;
class BlastCombinationModuleTest {
	@Test void damageRejectionStillCommitsWavePayment(){var s=new BlastCommandModuleTest.Store();var t=new BlastHitModuleTest.Target(){public boolean hurt(){assertEquals(84,s.state.mana());return super.hurt();}};t.accepted=false;var c=BlastCommandModuleTest.command(s,(y,p)->{BlastHit.apply(t,0);return true;},List.of());assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(0,0));assertEquals(List.of("hurt"),t.events);assertEquals(84,s.state.mana());}
	@Test void partialImpulseFailureRetainsPaymentAndCannotReplay(){var s=new BlastCommandModuleTest.Store();var t=new BlastHitModuleTest.Target(){public void impulse(double v,boolean a){super.impulse(v,a);throw new IllegalStateException();}};var c=BlastCommandModuleTest.command(s,(y,p)->{BlastHit.apply(t,0);return true;},List.of(()->fail()));assertThrows(IllegalStateException.class,()->c.execute(0,0));assertEquals(84,s.state.mana());assertEquals(List.of("hurt","immune","impulse"),t.events);assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
}
