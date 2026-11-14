package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.targeting.SupportConsent;
import com.example.examplemod.magic.spell.detox.antidote_iii.AntidoteIIIEffect;
class SupportModuleTest {
	static class Recipient implements SupportConsent.Recipient {boolean self,player,trusted,pet,owned,allied;public boolean self(){return self;}public boolean player(){return player;}public boolean trusted(){return trusted;}public boolean pet(){return pet;}public boolean owned(){return owned;}public boolean allied(){return allied;}}
	@Test void playerAllianceDoesNotBypassExplicitConsent(){var r=new Recipient();r.player=true;r.allied=true;assertFalse(SupportConsent.allows(r));r.trusted=true;assertTrue(SupportConsent.allows(r));}
	@Test void selfNeverReadsAttachments(){var r=new Recipient(){public boolean player(){fail();return false;}};r.self=true;assertTrue(SupportConsent.allows(r));}
	@Test void ownedPetAndAlliedNonplayerAreAccepted(){var r=new Recipient();r.pet=true;r.owned=true;assertTrue(SupportConsent.allows(r));r.owned=false;assertFalse(SupportConsent.allows(r));r.allied=true;assertTrue(SupportConsent.allows(r));}
	static class Patient implements AntidoteIIIEffect.Patient {StringBuilder log=new StringBuilder();boolean changed;public boolean removePoison(){log.append('p');return changed;}public boolean removeNausea(){log.append('n');return false;}public boolean removeHunger(){log.append('h');return false;}public boolean removeWeakness(){log.append('w');return false;}public void visual(){log.append('v');}}
	@Test void noCureProducesNoVisual(){var p=new Patient();assertFalse(AntidoteIIIEffect.apply(p));assertEquals("pnhw",p.log.toString());}
	@Test void firstSuccessStillRemovesAllBeforeVisual(){var p=new Patient();p.changed=true;assertTrue(AntidoteIIIEffect.apply(p));assertEquals("pnhwv",p.log.toString());}
}
