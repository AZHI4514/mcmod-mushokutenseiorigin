package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.BurstFieldSchedule;
import com.example.examplemod.magic.spell.fire.inferno.InfernoRules;
class InfernoModuleTest {
	@Test void firstFrameIsFlashOnlyAndTwentyResidualPulsesFollow(){var s=new BurstFieldSchedule(100,501,120,false);var f=s.begin(100);assertTrue(f.flash());assertFalse(f.pulse());int flashes=1,pulses=0;for(int t=101;t<501;t++){var frame=s.begin(t);if(frame.flash())flashes++;if(frame.pulse())pulses++;}assertEquals(1,flashes);assertEquals(20,pulses);assertEquals(520,s.next());}
	@Test void residualIncludesFourHundredButNotZero(){assertEquals(0,InfernoRules.residual(0));assertEquals(5,InfernoRules.residual(20));assertEquals(5,InfernoRules.residual(400));assertEquals(0,InfernoRules.residual(401));}
	@Test void twentyByEightCylinderIsInclusive(){assertTrue(InfernoRules.contains(20,8,0));assertFalse(InfernoRules.contains(20,8,.01));assertFalse(InfernoRules.contains(0,8.001,0));assertFalse(InfernoRules.contains(0,-.001,0));}
	@Test void loadedDefaultNeverReplaysInitialFlash(){var s=new BurstFieldSchedule(0,0,Long.MAX_VALUE,true);assertFalse(s.begin(0).active());}
	@Test void skippedTimeConsumesSinglePulseWithoutCatchup(){var s=new BurstFieldSchedule(0,401,20,true);assertTrue(s.begin(100).pulse());assertEquals(120,s.next());assertFalse(s.begin(100).active());}
}
