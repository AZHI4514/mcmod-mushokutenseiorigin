package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.*;
class SmokeRuntimeModuleTest {
	static class World implements SmokeFieldTick.World {
		StringBuilder log=new StringBuilder();boolean flight,due=true,present=true;public boolean statePresent(){return present;}public boolean expired(){return false;}public boolean ownerPresent(){return true;}public boolean alive(){return true;}public boolean sameLevel(){return true;}public boolean adopt(){return true;}public void removeState(){log.append('r');}public void discard(){log.append('d');}public boolean flying(){return flight;}public void projectile(){log.append('p');flight=false;}public boolean pulseDue(){return due;}public void schedule(){log.append('s');}public void contacts(boolean pulse){log.append(pulse?'c':'n');}public void visual(){log.append('v');}
	}
	@Test void impactFrameDoesNotAlsoApplyFog(){var w=new World();w.flight=true;SmokeFieldTick.tick(w);assertEquals("p",w.log.toString());SmokeFieldTick.tick(w);assertEquals("pscv",w.log.toString());}
	@Test void refusedContactStillAdvancesPulseSchedule(){var w=new World(){public void contacts(boolean p){log.append('c');throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->SmokeFieldTick.tick(w));assertEquals("sc",w.log.toString());}
	@Test void offCadenceStillAppliesSight(){var w=new World();w.due=false;SmokeFieldTick.tick(w);assertEquals("nv",w.log.toString());}
	@Test void missingStateDiscardsBeforeExpiry(){var w=new World(){public boolean expired(){fail();return true;}};w.present=false;SmokeFieldTick.tick(w);assertEquals("rd",w.log.toString());}
	@Test void smokeContactPreservesSightSlowDamageOrder(){var log=new StringBuilder();SmokeContact.apply(new SmokeContact.Target(){public boolean inside(){log.append('i');return true;}public boolean exposed(){log.append('e');return true;}public boolean visible(){log.append('v');return true;}public void sight(){log.append('s');}public void slowing(){log.append('l');}public void damage(){log.append('d');}});assertEquals("ievsld",log.toString());}
	@Test void roofBlocksAllMudEffects(){SmokeContact.apply(new SmokeContact.Target(){public boolean inside(){return true;}public boolean exposed(){return false;}public boolean visible(){fail();return true;}public void sight(){fail();}public void slowing(){fail();}public void damage(){fail();}});}
}
