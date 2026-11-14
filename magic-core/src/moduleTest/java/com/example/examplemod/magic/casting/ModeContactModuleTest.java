package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.atmosphere_dominion.AtmosphereContact;
import com.example.examplemod.magic.spell.special.gravity_magic.GravityContact;
class ModeContactModuleTest {
	static class Wind implements AtmosphereContact.Target {StringBuilder log=new StringBuilder();boolean owner,claim=true,immune;public boolean owner(){return owner;}public double height(){return 1;}public double horizontalSquared(){return 1;}public boolean allowed(){log.append('a');return true;}public boolean visible(){log.append('v');return true;}public boolean claim(){log.append('c');return claim;}public void hurt(){log.append('h');}public boolean immune(){log.append('i');return immune;}public void push(){log.append('p');}}
	@Test void windOwnerSkipsEveryOtherRead(){var t=new Wind(){public double height(){fail();return 0;}};t.owner=true;AtmosphereContact.apply(t,2,true);assertEquals("",t.log.toString());}
	@Test void offensiveModeUsesWindCadenceOnlyOnPulse(){var t=new Wind();AtmosphereContact.apply(t,2,true);assertEquals("avch",t.log.toString());t.log.setLength(0);AtmosphereContact.apply(t,2,false);assertEquals("av",t.log.toString());}
	@Test void pushModeChecksImmunityWithoutCadence(){var t=new Wind();AtmosphereContact.apply(t,1,true);assertEquals("avip",t.log.toString());}
	static class Gravity implements GravityContact.Target {StringBuilder log=new StringBuilder();boolean immune;public double height(){return 1;}public double horizontalSquared(){return 1;}public boolean allowed(){return true;}public boolean visible(){return true;}public void heavyEffects(){log.append('e');}public void hurt(){log.append('h');}public void slowFalling(){log.append('f');}public boolean immune(){log.append('i');return immune;}public void lift(){log.append('l');}}
	@Test void heavyModeDoesNotSkipEffectsForImmuneTarget(){var t=new Gravity();t.immune=true;GravityContact.apply(t,0,true);assertEquals("eh",t.log.toString());}
	@Test void lowGravityAlwaysGrantsSlowFallingBeforeImmunity(){var t=new Gravity();t.immune=true;GravityContact.apply(t,1,true);assertEquals("fi",t.log.toString());}
	@Test void heavyOffCadenceStillAppliesDebuffs(){var t=new Gravity();GravityContact.apply(t,0,false);assertEquals("e",t.log.toString());}
}
