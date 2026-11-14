package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.targeting.HolyOpeningHit;
import com.example.examplemod.magic.mechanic.field.NaturalSpawnSuppression;
import com.example.examplemod.magic.spell.holy.sacred_nova.SacredNovaRules;
import com.example.examplemod.magic.spell.holy.god_purge.GodPurgeRules;
class HolySanctuaryModuleTest {
	@Test void radiiRemainDistinctAndClosed(){assertTrue(SacredNovaRules.inside(256));assertFalse(SacredNovaRules.inside(Math.nextUp(256d)));assertTrue(GodPurgeRules.inside(2304));assertFalse(GodPurgeRules.inside(Double.NaN));}
	@Test void ringRetainsCadenceAndGrowth(){assertTrue(SacredNovaRules.ringDue(100));assertFalse(SacredNovaRules.ringDue(99));assertEquals(2.4,SacredNovaRules.ringRadius(100,100));assertEquals(16,SacredNovaRules.ringRadius(100,1));assertEquals(48,GodPurgeRules.ringRadius(100,1));}
	@Test void refusedDamageDoesNotKnockBack(){var log=new StringBuilder();HolyOpeningHit.apply(new HolyOpeningHit.Target(){public boolean inside(){log.append('i');return true;}public boolean visible(){log.append('v');return true;}public boolean hurt(){log.append('h');return false;}public void afterDamage(){fail();}});assertEquals("ivh",log.toString());}
	@Test void openingStopsBeforeVisibilityOutsideSphere(){HolyOpeningHit.apply(new HolyOpeningHit.Target(){public boolean inside(){return false;}public boolean visible(){fail();return true;}public boolean hurt(){fail();return true;}public void afterDamage(){fail();}});}
	@Test void removedFieldNeverQueriesWorldOrExpiry(){assertFalse(NaturalSpawnSuppression.suppresses(new NaturalSpawnSuppression.Field(){public boolean removed(){return true;}public boolean sameLevel(){fail();return true;}public boolean unexpired(){fail();return true;}public boolean inside(){fail();return true;}}));}
	@Test void expiryStopsBeforeDistance(){assertFalse(NaturalSpawnSuppression.suppresses(new NaturalSpawnSuppression.Field(){public boolean removed(){return false;}public boolean sameLevel(){return true;}public boolean unexpired(){return false;}public boolean inside(){fail();return true;}}));}
}
