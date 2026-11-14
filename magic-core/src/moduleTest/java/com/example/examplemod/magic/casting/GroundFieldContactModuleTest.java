package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.GroundFieldContact;
class GroundFieldContactModuleTest {
	static class Target implements GroundFieldContact.Target {
		StringBuilder log=new StringBuilder();boolean due=true,claim=true,hurt=true;
		public boolean eligible(){log.append('e');return true;}public boolean inside(){log.append('i');return true;}public boolean surface(){log.append('s');return true;}public boolean visible(){log.append('v');return true;}
		public void continuous(){log.append('c');}public boolean attackDue(){log.append('a');return due;}public boolean claim(){log.append('q');return claim;}public boolean hurt(){log.append('h');return hurt;}public void afterDamage(){log.append('d');}public void hook(){log.append('k');}
	}
	@Test void successOrderIncludesClaimBeforeDamage(){var t=new Target();GroundFieldContact.apply(t);assertEquals("eisvcaq hdk".replace(" ",""),t.log.toString());}
	@Test void continuousDebuffAppliesBetweenPulses(){var t=new Target();t.due=false;GroundFieldContact.apply(t);assertEquals("eisvca",t.log.toString());}
	@Test void refusedDamageStillClaimsButDoesNotIgnite(){var t=new Target();t.hurt=false;GroundFieldContact.apply(t);assertEquals("eisvcaqh",t.log.toString());}
	@Test void refusedCadenceStopsBeforeDamage(){var t=new Target();t.claim=false;GroundFieldContact.apply(t);assertEquals("eisvcaq",t.log.toString());}
	@Test void obstructedTargetDoesNotGetContinuousDebuff(){var t=new Target(){public boolean visible(){log.append('v');return false;}};GroundFieldContact.apply(t);assertEquals("eisv",t.log.toString());}
	@Test void effectExceptionStopsHook(){var t=new Target(){public void afterDamage(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->GroundFieldContact.apply(t));assertEquals("eisvcaqh",t.log.toString());}
}
