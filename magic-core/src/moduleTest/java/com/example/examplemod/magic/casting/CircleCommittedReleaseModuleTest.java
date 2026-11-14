package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
class CircleCommittedReleaseModuleTest {
	@Test void circleConsumedAfterEffectBeforeObservers(){var s=new BlastCommandModuleTest.Store();var log=new StringBuilder();var c=BlastCommandModuleTest.command(s,(y,p)->{log.append('e');return true;},List.of(()->log.append('o')));assertEquals(CastSelfEffectCommand.Result.SUCCESS,c.execute(0,0,List.of(()->log.append('c'))));assertEquals("eco",log.toString());}
	@Test void failedEffectRollsBackWithoutConsumingCircle(){var s=new BlastCommandModuleTest.Store();var before=s.state;var c=BlastCommandModuleTest.command(s,(y,p)->false,List.of(()->fail()));assertEquals(CastSelfEffectCommand.Result.REJECTED,c.execute(0,0,List.of(()->fail())));assertSame(before,s.state);}
	@Test void circleExceptionKeepsPaymentAndStopsObserversAsBefore(){var s=new BlastCommandModuleTest.Store();var c=BlastCommandModuleTest.command(s,(y,p)->true,List.of(()->fail()));assertThrows(IllegalStateException.class,()->c.execute(0,0,List.of(()->{throw new IllegalStateException();})));assertEquals(84,s.state.mana());assertEquals(CastSelfEffectCommand.Result.ALREADY_EXECUTED,c.execute(0,0));}
}
