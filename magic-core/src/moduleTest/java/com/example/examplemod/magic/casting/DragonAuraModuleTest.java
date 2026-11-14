package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.special.saint_dragon_battle_aura.*;
class DragonAuraModuleTest {
	static class World implements DragonAuraTick.World {boolean ended,active,paid=true,due=true;StringBuilder log=new StringBuilder();public boolean ended(){return ended;}public boolean alive(){return true;}public boolean active(){return active;}public void clearActive(){active=false;log.append('c');}public void recovery(){log.append('r');}public void endNow(){log.append('e');}public void removeResistance(){log.append('x');}public void activate(){active=true;log.append('a');}public boolean paymentDue(){return due;}public void schedule(){log.append('s');}public boolean pay(){log.append('p');return paid;}public void addResistance(){log.append('k');}public void visual(){log.append('v');}}
	@Test void firstActiveTickMarksThenSchedulesAndPays(){var w=new World();DragonAuraTick.tick(w);assertEquals("aspkv",w.log.toString());}
	@Test void insufficientManaEndsWithoutClearingActiveYet(){var w=new World();w.paid=false;DragonAuraTick.tick(w);assertTrue(w.active);assertEquals("asperx",w.log.toString());}
	@Test void normalEndClearsThenSetsRecoveryAndEnd(){var w=new World();w.ended=true;w.active=true;DragonAuraTick.tick(w);assertEquals("crex",w.log.toString());}
	@Test void inactiveEndOnlyRemovesModifier(){var w=new World();w.ended=true;DragonAuraTick.tick(w);assertEquals("x",w.log.toString());}
	@Test void offCadenceDoesNotPay(){var w=new World();w.active=true;w.due=false;DragonAuraTick.tick(w);assertEquals("kv",w.log.toString());}
	@Test void readinessAndFloatReductionAreExact(){assertTrue(DragonAuraRules.canCast(100,100,100));assertFalse(DragonAuraRules.canCast(99,100,0));assertEquals(10*.65F,DragonAuraRules.reducedDamage(10));}
}
