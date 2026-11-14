package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.healing.healing.*;
import com.example.examplemod.magic.spell.detox.antidote_ii.AntidoteIIEffect;
class HealingDetoxModuleTest {
	@Test void healingRejectsInvalidAndDeadPatients(){assertEquals(0,HealingRules.amount(Float.NaN,20,4));assertEquals(0,HealingRules.amount(10,Float.POSITIVE_INFINITY,4));assertEquals(0,HealingRules.amount(0,20,4));assertEquals(1,HealingRules.amount(19,20,4));}
	static class Patient implements HealingEffect.Patient {float health=10;StringBuilder log=new StringBuilder();boolean reject;public float health(){return health;}public float maximum(){return 20;}public void heal(float amount){log.append('h');if(!reject)health+=amount;}public void visual(){log.append('v');}}
	@Test void vanillaRefusalDoesNotShowSuccess(){var p=new Patient();p.reject=true;assertFalse(HealingEffect.apply(p,4));assertEquals("h",p.log.toString());}
	@Test void fullHealthDoesNotCallHeal(){var p=new Patient();p.health=20;assertFalse(HealingEffect.apply(p,4));assertEquals("",p.log.toString());}
	@Test void changedHealthPrecedesParticles(){var p=new Patient();assertTrue(HealingEffect.apply(p,4));assertEquals(14,p.health);assertEquals("hv",p.log.toString());}
	@Test void antidoteRemovesAllEffectsEvenAfterFirstSuccess(){var log=new StringBuilder();assertTrue(AntidoteIIEffect.apply(new AntidoteIIEffect.Patient(){public boolean removePoison(){log.append('p');return true;}public boolean removeNausea(){log.append('n');return false;}public boolean removeHunger(){log.append('h');return true;}}));assertEquals("pnh",log.toString());}
}
