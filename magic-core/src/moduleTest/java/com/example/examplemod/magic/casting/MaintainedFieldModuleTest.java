package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.MaintainedFieldTick;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionTick;
class MaintainedFieldModuleTest {
	static class World implements MaintainedFieldTick.World {
		StringBuilder log=new StringBuilder();boolean present=true,due=true,paid=true;public boolean statePresent(){return present;}public boolean expired(){return false;}public boolean ownerPresent(){return true;}public boolean alive(){return true;}public boolean sameLevel(){return true;}public boolean adopt(){return true;}public void removeState(){log.append('r');}public void discard(){log.append('d');}public void follow(){log.append('f');}public boolean pulseDue(){return due;}public void schedule(){log.append('s');}public boolean payUpkeep(){log.append('p');return paid;}public void update(boolean pulse){log.append(pulse?'u':'n');}
	}
	@Test void schedulePrecedesPaymentAndUpdate(){var w=new World();MaintainedFieldTick.tick(w);assertEquals("fspu",w.log.toString());}
	@Test void insufficientManaDiscardsWithoutRemovingState(){var w=new World();w.paid=false;MaintainedFieldTick.tick(w);assertEquals("fspd",w.log.toString());}
	@Test void noPulseDoesNotReadMana(){var w=new World(){public boolean payUpkeep(){fail();return true;}};w.due=false;MaintainedFieldTick.tick(w);assertEquals("fn",w.log.toString());}
	@Test void missingStateSkipsExpiry(){var w=new World(){public boolean expired(){fail();return true;}};w.present=false;MaintainedFieldTick.tick(w);assertEquals("rd",w.log.toString());}
	static class Nuclear implements NuclearExplosionTick.World {long age=100;boolean exploded,throwTargets;StringBuilder log=new StringBuilder();public long age(){return age;}public boolean exploded(){return exploded;}public void commitExplosion(){exploded=true;log.append('c');}public void targets(){assertTrue(exploded);log.append('h');if(throwTargets)throw new IllegalStateException();}public void terrain(){log.append('t');}public boolean visualDue(){return true;}public void visual(){log.append('v');}}
	@Test void nuclearCommitsOnceBeforeTargetsAndTerrain(){var w=new Nuclear();NuclearExplosionTick.tick(w);NuclearExplosionTick.tick(w);assertEquals("chtvv",w.log.toString());}
	@Test void nuclearExceptionCannotReplayExplosion(){var w=new Nuclear();w.throwTargets=true;assertThrows(IllegalStateException.class,()->NuclearExplosionTick.tick(w));assertTrue(w.exploded);w.throwTargets=false;NuclearExplosionTick.tick(w);assertEquals("chv",w.log.toString());}
	@Test void warningDoesNotHitOrExcavate(){var w=new Nuclear();w.age=99;NuclearExplosionTick.tick(w);assertEquals("v",w.log.toString());}
}
