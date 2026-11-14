package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.fortress_barrier.FortressUpkeep;
import com.example.examplemod.magic.spell.barrier.demon_god_sealing_barrier.DemonGodSealRules;
import com.example.examplemod.magic.mechanic.barrier.PoweredBarrierTick;
class PoweredBarrierModuleTest {
	static class Power implements FortressUpkeep.World {long dry,now=100;boolean energy;StringBuilder log=new StringBuilder();public boolean due(){return true;}public void schedule(){log.append('s');}public boolean takeEnergy(){log.append('t');return energy;}public void clearDry(){dry=0;log.append('c');}public void repair(){log.append('r');}public long drySince(){return dry;}public long now(){return now;}public void beginDry(){dry=now;log.append('d');}}
	@Test void successfulSupplyClearsDryThenRepairs(){var p=new Power();p.energy=true;p.dry=10;assertTrue(FortressUpkeep.maintain(p));assertEquals("stcr",p.log.toString());assertEquals(0,p.dry);assertEquals(600,FortressUpkeep.repair(599));}
	@Test void dryGraceEndsAtExactlyHundredTicks(){var p=new Power();assertTrue(FortressUpkeep.maintain(p));assertEquals(100,p.dry);p.now=199;assertTrue(FortressUpkeep.maintain(p));p.now=200;assertFalse(FortressUpkeep.maintain(p));}
	@Test void playerCanNeverBeSealTarget(){assertFalse(DemonGodSealRules.eligible(new DemonGodSealRules.Target(){public boolean player(){return true;}public float health(){fail();return 0;}public float maxHealth(){fail();return 0;}public boolean tagged(){fail();return true;}}));}
	@Test void twentyPercentBoundaryAndTagRemainRequired(){assertTrue(DemonGodSealRules.eligible(new DemonGodSealRules.Target(){public boolean player(){return false;}public float health(){return 20;}public float maxHealth(){return 100;}public boolean tagged(){return true;}}));}
	static class World implements PoweredBarrierTick.World {boolean present=true,cores=true,maintain=true;StringBuilder log=new StringBuilder();public boolean statePresent(){return present;}public boolean coreCountValid(){return true;}public boolean depleted(){return false;}public boolean expired(){return false;}public boolean ownerPresent(){return true;}public boolean alive(){return true;}public boolean sameLevel(){return true;}public boolean adopt(){log.append('a');return true;}public boolean coresValid(){log.append('c');return cores;}public boolean maintain(){log.append('m');return maintain;}public void release(){log.append('r');}public void visual(){log.append('v');}}
	@Test void invalidCoreReleasesBeforeMaintenance(){var w=new World();w.cores=false;PoweredBarrierTick.tick(w);assertEquals("acr",w.log.toString());}
	@Test void failedMaintenanceReleasesWithoutDrawing(){var w=new World();w.maintain=false;PoweredBarrierTick.tick(w);assertEquals("acmr",w.log.toString());}
	@Test void absentStateSkipsCoreInspection(){var w=new World(){public boolean coreCountValid(){fail();return true;}};w.present=false;PoweredBarrierTick.tick(w);assertEquals("r",w.log.toString());}
}
