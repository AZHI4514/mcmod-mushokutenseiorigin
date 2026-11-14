package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.summon.SummonTick;
class SummonLifecycleModuleTest {
	static class World implements SummonTick.World {StringBuilder log=new StringBuilder();boolean owner=true,slot=true,due=true,paid=true;public boolean ownerPresent(){return owner;}public boolean sameLevel(){return true;}public boolean ownerAlive(){return true;}public boolean summonAlive(){return true;}public boolean expired(){return false;}public boolean slotMatches(){return slot;}public void discard(){log.append('d');}public boolean pulseDue(){return due;}public void schedule(){log.append('s');}public boolean pay(){log.append('p');return paid;}public void act(boolean pulse){log.append(pulse?'a':'n');}}
	@Test void missingOwnerDiscardsBeforeWorldRead(){var w=new World(){public boolean sameLevel(){fail();return false;}};w.owner=false;SummonTick.tick(w);assertEquals("d",w.log.toString());}
	@Test void changedSlotCannotContinueOrCharge(){var w=new World();w.slot=false;SummonTick.tick(w);assertEquals("d",w.log.toString());}
	@Test void paymentScheduledBeforeAct(){var w=new World();SummonTick.tick(w);assertEquals("spa",w.log.toString());}
	@Test void insufficientManaDoesNotRunAiOrLight(){var w=new World();w.paid=false;SummonTick.tick(w);assertEquals("spd",w.log.toString());}
	@Test void offCadenceStillRunsMovement(){var w=new World();w.due=false;SummonTick.tick(w);assertEquals("n",w.log.toString());}
}
