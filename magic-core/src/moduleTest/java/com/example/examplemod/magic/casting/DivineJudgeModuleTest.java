package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.holy.divine_judge.*;
class DivineJudgeModuleTest {
	static class World implements DivineJudgeTick.World {
		DivineJudgePulse pulse=new DivineJudgePulse(100,0);long time=100;boolean owner=true,throwStrike;StringBuilder log=new StringBuilder();int strikes;
		public DivineJudgePulse state(){return pulse;}public long now(){return time;}
		public boolean ownerPresent(){return owner;}public boolean alive(){return true;}public boolean sameLevel(){return true;}public boolean adopt(){return true;}
		public void removeState(){log.append('r');pulse=null;}public void discard(){log.append('d');}
		public void commit(DivineJudgePulse p){log.append('c');pulse=p;}public void strike(int index){log.append('s');strikes++;assertEquals(index+1,pulse.count());if(throwStrike)throw new IllegalStateException();}
	}
	@Test void skippedTicksDoNotReplayMissedPulses(){var w=new World();w.time=145;DivineJudgeTick.tick(w);assertEquals(3,w.pulse.count());assertEquals(1,w.strikes);DivineJudgeTick.tick(w);assertEquals(1,w.strikes);}
	@Test void commitPrecedesWorldException(){var w=new World();w.throwStrike=true;assertThrows(IllegalStateException.class,()->DivineJudgeTick.tick(w));assertEquals(1,w.pulse.count());assertEquals("cs",w.log.toString());}
	@Test void expiryDiscardsBeforeOwner(){var w=new World(){public boolean ownerPresent(){fail();return true;}};w.time=160;DivineJudgeTick.tick(w);assertNull(w.pulse);assertEquals("rd",w.log.toString());}
	@Test void missingStateDoesNotReadTime(){var w=new World(){public long now(){fail();return 0;}};w.pulse=null;DivineJudgeTick.tick(w);assertEquals("rd",w.log.toString());}
	@Test void invalidOwnerRemovesState(){var w=new World();w.owner=false;DivineJudgeTick.tick(w);assertNull(w.pulse);assertEquals(0,w.strikes);}
	@Test void pulseAndGeometryBoundaries(){assertEquals(-1,DivineJudgeRules.pulseIndex(-1));assertEquals(0,DivineJudgeRules.pulseIndex(19));assertEquals(1,DivineJudgeRules.pulseIndex(20));assertEquals(2,DivineJudgeRules.pulseIndex(59));assertEquals(-1,DivineJudgeRules.pulseIndex(60));assertTrue(DivineJudgeRules.inside(12,32,0));assertFalse(DivineJudgeRules.inside(12,32.01,0));assertFalse(DivineJudgeRules.inside(12.01,0,0));assertEquals(0,DivineJudgeRules.restoredCount(-5));assertEquals(3,DivineJudgeRules.restoredCount(10));}
}
