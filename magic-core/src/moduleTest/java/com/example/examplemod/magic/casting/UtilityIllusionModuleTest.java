package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.special.divination_magic.DivinationRules;
import com.example.examplemod.magic.spell.special.disturb_magic.DisturbEffect;
import com.example.examplemod.magic.spell.barrier.illusion_barrier.IllusionPulse;
class UtilityIllusionModuleTest {
	@Test void forecastChecksOneThroughTwoHundredThenStops(){var ticks=new java.util.ArrayList<Integer>();assertEquals(3,DivinationRules.firstIntersection(i->{ticks.add(i);return i==3;}));assertEquals(java.util.List.of(1,2,3),ticks);assertEquals(200,DivinationRules.firstIntersection(i->i==200));assertEquals(0,DivinationRules.firstIntersection(i->false));}
	@Test void forecastUncertaintyClamps(){assertEquals(0,DivinationRules.reportedTime(.1,0));assertEquals(12,DivinationRules.reportedTime(11,1));assertEquals(5,DivinationRules.reportedTime(5,.5));}
	@Test void missedCounterStillReportsReleaseSuccess(){assertTrue(DisturbEffect.apply(null));}
	@Test void counterCancelsBeforePreventingAndDrawing(){var log=new StringBuilder();assertTrue(DisturbEffect.apply(new DisturbEffect.Target(){public void cancel(){log.append('c');}public void preventCasting(){log.append('p');}public void visual(){log.append('v');}}));assertEquals("cpv",log.toString());}
	static class World implements IllusionPulse.World {StringBuilder log=new StringBuilder();boolean paid=true,due=true;public boolean due(){return due;}public boolean pay(){log.append('p');return paid;}public void discard(){log.append('d');}public void schedule(){log.append('s');}public void visual(){log.append('v');}}
	@Test void illusionPaysBeforeSchedulingUnlikeModeFields(){var w=new World();IllusionPulse.tick(w);assertEquals("psv",w.log.toString());}
	@Test void failedIllusionPaymentDoesNotAdvanceNext(){var w=new World();w.paid=false;IllusionPulse.tick(w);assertEquals("pd",w.log.toString());}
}
