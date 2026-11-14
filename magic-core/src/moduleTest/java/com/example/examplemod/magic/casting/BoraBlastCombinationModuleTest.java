package com.example.examplemod.magic.casting;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.magic.spell.mixed.bora_blast.BoraBlastHit;
class BoraBlastCombinationModuleTest {
	@Test void damageRejectionStillCommitsPaidWave(){var s=new BoraBlastCommandModuleTest.Store();var t=new BoraBlastHitModuleTest.Target(){public boolean hurt(){assertEquals(62,s.state.mana());return super.hurt();}};t.accepted=false;var c=BoraBlastCommandModuleTest.command(s,(y,p)->{BoraBlastHit.apply(t,0);return true;},List.of());assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(0,0));assertEquals(List.of("hurt"),t.events);assertEquals(62,s.state.mana());}
	@Test void partialWarmthConsumptionKeepsPaymentAndCannotReplay(){var s=new BoraBlastCommandModuleTest.Store();var t=new BoraBlastHitModuleTest.Target(){public void removeWarm(){super.removeWarm();throw new IllegalStateException();}};t.warm=true;var c=BoraBlastCommandModuleTest.command(s,(y,p)->{BoraBlastHit.apply(t,0);return true;},List.of(()->fail()));assertThrows(IllegalStateException.class,()->c.execute(0,0));assertFalse(t.warm);assertEquals(62,s.state.mana());assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
}
