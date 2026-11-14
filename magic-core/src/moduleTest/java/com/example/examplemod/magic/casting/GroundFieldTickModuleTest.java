package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.GroundFieldTick;
class GroundFieldTickModuleTest {
	static class World implements GroundFieldTick.World {
		StringBuilder log=new StringBuilder();int pulse=0,last=-1;boolean present=true,continuous,throwTerrain;
		public boolean statePresent(){return present;}public boolean expired(){return false;}public boolean ownerPresent(){return true;}public boolean alive(){return true;}public boolean sameLevel(){return true;}public boolean adopt(){return true;}
		public void removeState(){log.append('r');}public void discard(){log.append('d');}public void move(){log.append('m');}public int pulse(){return pulse;}public int lastPulse(){return last;}
		public void commit(int p){log.append('c');last=p;}public void terrain(int p){log.append('t');assertEquals(p,last);if(throwTerrain)throw new IllegalStateException();}
		public boolean continuousContacts(){return continuous;}public void contacts(int p,boolean attempt){log.append(attempt?'h':'a');}public boolean visualDue(){return true;}public void visual(){log.append('v');}
	}
	@Test void pulseIsCommittedBeforeTerrainAndContacts(){var w=new World();GroundFieldTick.tick(w);assertEquals("mcthv",w.log.toString());}
	@Test void repeatedPulseStillAllowsContinuousContact(){var w=new World();w.last=0;w.continuous=true;GroundFieldTick.tick(w);assertEquals("mav",w.log.toString());}
	@Test void repeatedNoncontinuousPulseOnlyMovesAndDraws(){var w=new World();w.last=0;GroundFieldTick.tick(w);assertEquals("mv",w.log.toString());}
	@Test void terrainExceptionDoesNotRollBackClaim(){var w=new World();w.throwTerrain=true;assertThrows(IllegalStateException.class,()->GroundFieldTick.tick(w));assertEquals(0,w.last);assertEquals("mct",w.log.toString());}
	@Test void absentStateSkipsExpiryAndMovement(){var w=new World(){public boolean expired(){fail();return false;}};w.present=false;GroundFieldTick.tick(w);assertEquals("rd",w.log.toString());}
}
