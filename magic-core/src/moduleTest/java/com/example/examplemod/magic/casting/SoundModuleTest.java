package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.special.howling.*;
import com.example.examplemod.magic.spell.special.hypnosis_magic.*;
class SoundModuleTest {
	@Test void coneRetainsNearZeroAndOuterRange(){assertTrue(HowlingRules.inside(0,-1));assertFalse(HowlingRules.inside(12.01,13));assertTrue(HowlingRules.inside(12,12));assertFalse(HowlingRules.inside(1,0));assertTrue(HowlingRules.inside(Double.NaN,Double.NaN));}
	@Test void refusedHowlDamageDoesNotControl(){var log=new StringBuilder();HowlingHit.apply(new HowlingHit.Target(){public boolean inside(){return true;}public boolean visible(){log.append('v');return true;}public boolean hurt(){log.append('h');return false;}public void control(){fail();}});assertEquals("vh",log.toString());}
	@Test void hypnosisImmunityShortCircuitsPermissions(){assertFalse(HypnosisRules.eligible(new HypnosisRules.Target(){public boolean immune(){return true;}public boolean allowed(){fail();return true;}public boolean hypnotized(){fail();return false;}public boolean ready(){fail();return true;}}));}
	@Test void hypnosisDurationsRemainPlayerSpecific(){assertEquals(40,HypnosisRules.duration(true));assertEquals(160,HypnosisRules.duration(false));}
	@Test void failedSleepDoesNotSetImmunityOrActive(){assertFalse(HypnosisEffect.apply(new HypnosisEffect.Target(){public boolean addEffect(){return false;}public void setImmunity(){fail();}public void markActive(){fail();}public void visual(){fail();}}));}
	@Test void acceptedSleepCommitsBeforeVisual(){var log=new StringBuilder();assertTrue(HypnosisEffect.apply(new HypnosisEffect.Target(){public boolean addEffect(){log.append('e');return true;}public void setImmunity(){log.append('i');}public void markActive(){log.append('a');}public void visual(){log.append('v');}}));assertEquals("eiav",log.toString());}
}
