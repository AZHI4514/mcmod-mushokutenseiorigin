package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.ocean_freeze.*;
class OceanFreezeModuleTest {
	@Test void boundedExpansionAndDamageWindow(){assertEquals(0,OceanFreezeRules.radius(-1));assertEquals(64,OceanFreezeRules.radius(160));assertEquals(64,OceanFreezeRules.radius(11999));assertTrue(OceanFreezeRules.damageActive(160));assertFalse(OceanFreezeRules.damageActive(161));assertTrue(OceanFreezeRules.expired(12000));assertTrue(OceanFreezeRules.expired(-1));}
	@Test void scanRepeatsAtExactStartAndPreservesBudget(){assertTrue(OceanFreezeRules.scanDue(0,0,0));assertFalse(OceanFreezeRules.scanDue(0,0,1));assertTrue(OceanFreezeRules.scanDue(0,1,3));assertEquals(0,OceanFreezeRules.remainingBudget(8192));}
	@Test void savedClaimsRestoreWithInvalidUuidIgnored(){var id=java.util.UUID.randomUUID();var s=new OceanFreezeState(1,-1,0);s.restoreHits("bad,"+id+",broken");assertTrue(s.hit(id));assertEquals(id.toString(),s.savedHits());s.addBlocks(12);s.scanned(5);assertEquals(12,s.blocks());assertEquals(5,s.scanned());}
	static class Target implements OceanFreezeHit.Target {StringBuilder log=new StringBuilder();boolean hit,hurt=true,freeze=true;public boolean alreadyHit(){return hit;}public boolean outside(){return false;}public boolean visible(){log.append('v');return true;}public void claim(){hit=true;log.append('c');}public boolean hurt(){assertTrue(hit);log.append('h');return hurt;}public boolean canFreeze(){log.append('f');return freeze;}public void freeze(){log.append('e');}}
	@Test void refusedDamageStillClaimsWithoutFreezeRead(){var t=new Target();t.hurt=false;OceanFreezeHit.apply(t);assertTrue(t.hit);assertEquals("vch",t.log.toString());OceanFreezeHit.apply(t);assertEquals("vch",t.log.toString());}
	@Test void successfulDamageChecksFreezeAfterClaim(){var t=new Target();OceanFreezeHit.apply(t);assertEquals("vchfe",t.log.toString());}
	@Test void restoredClampsMatchOldDefaults(){assertEquals(-1,OceanFreezeRules.restoredScanned(-10));assertEquals(64,OceanFreezeRules.restoredScanned(99));assertEquals(0,OceanFreezeRules.restoredBlocks(-3));assertEquals(8192,OceanFreezeRules.restoredBlocks(9999));}
}
