package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.special.teleportation_magic.*;
class TeleportModuleTest {
	@Test void invitationIsServerOwned(){var a=new TeleportInvitations();var b=new TeleportInvitations();var id=java.util.UUID.randomUUID();a.put(id,new TeleportInvitations.Invitation(id,1200,false));assertNull(b.get(id));assertFalse(a.get(id).accepted());a.remove(id);assertNull(a.get(id));}
	@Test void nameAndDistanceFeesRetainLimits(){assertTrue(TeleportRules.validName("home_1-A"));assertFalse(TeleportRules.validName("a b"));assertFalse(TeleportRules.validName("a".repeat(33)));assertEquals(0,TeleportRules.extra(true,0));assertEquals(2,TeleportRules.extra(true,16));assertEquals(4,TeleportRules.extra(true,16.01));assertEquals(400,TeleportRules.extra(false,0));}
	static class World implements TeleportRelease.World {StringBuilder log=new StringBuilder();int reject=-1;public int travelerCount(){return 3;}public boolean move(int i){log.append('m').append(i);return i!=reject;}public void rollback(int i){log.append('r').append(i);}public void payExtra(){log.append('p');}public void consumeInvitations(){log.append('c');}public void visual(){log.append('v');}}
	@Test void allTravelersMoveBeforeExtraPayment(){var w=new World();assertTrue(TeleportRelease.cast(w));assertEquals("m0m1m2pcv",w.log.toString());}
	@Test void failedThirdMoveRollsBackInReverseBeforePayment(){var w=new World();w.reject=2;assertFalse(TeleportRelease.cast(w));assertEquals("m0m1m2r1r0",w.log.toString());}
	@Test void failedFirstMoveDoesNotRollbackOrPay(){var w=new World();w.reject=0;assertFalse(TeleportRelease.cast(w));assertEquals("m0",w.log.toString());}
	static class Spot implements TeleportSafety.Spot {StringBuilder log=new StringBuilder();boolean liquid;public boolean loaded(){log.append('l');return true;}public boolean border(){log.append('b');return true;}public boolean collisionFree(){log.append('c');return true;}public boolean liquid(){log.append('w');return liquid;}public boolean reserved(){log.append('r');return false;}public boolean hazard(){log.append('h');return false;}public boolean safeFloor(){log.append('f');return true;}}
	@Test void safetyChecksAllBeforeMovement(){var s=new Spot();assertTrue(TeleportSafety.safe(s));assertEquals("lbcwrhf",s.log.toString());}
	@Test void liquidPreventsReservationHazardAndFloorReads(){var s=new Spot();s.liquid=true;assertFalse(TeleportSafety.safe(s));assertEquals("lbcw",s.log.toString());}
}
