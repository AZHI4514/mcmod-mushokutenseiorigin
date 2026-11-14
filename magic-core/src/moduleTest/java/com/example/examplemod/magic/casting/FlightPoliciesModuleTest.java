package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.mechanic.projectile.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FlightPoliciesModuleTest {
	@Test void directionalCapabilitiesKeepContactBoundaryAndExceptionalIds(){
		for(var spell:SpellDefinition.ALL){var p=CarrierFlightPolicy.of(spell);
			assertEquals(java.util.Set.of("icicle_break","icicle_edge","flood_flush","explosion","wind_slice").contains(spell.id()),p.strictPiercing());
			assertEquals(spell==SpellDefinition.ICICLE_EDGE,p.bladeSection());assertEquals(spell==SpellDefinition.EXPLOSION,p.explosion());assertEquals(spell==SpellDefinition.WIND_SLICE,p.windSlice());
			for(double c:new double[]{Double.NaN,Double.NEGATIVE_INFINITY,-1,0,1,2,Double.POSITIVE_INFINITY})for(boolean hit:new boolean[]{false,true}){
				assertEquals(p.strictPiercing()?PiercingFlightRules.candidate(c,1,2,hit):!hit&&c<1,p.candidate(c,1,2,hit));
				assertEquals(p.strictPiercing()?!PiercingFlightRules.contactDue(c,1,hit):c>1||hit,p.skip(c,1,hit));
			}
		}
	}
	@Test void sweptPoliciesDelegateActualPerSpellLifetimeAndRetainSpecialCapabilities(){
		for(var spell:java.util.List.of(SpellDefinition.WATER_CANNON,SpellDefinition.STONE_CANNON,SpellDefinition.EX_FLAME,SpellDefinition.EXODUS_FLAME,SpellDefinition.TORNADO_IMPACT,SpellDefinition.SONIC_BOOM)){
			var p=SweptProjectilePolicy.of(spell);assertEquals(spell==SpellDefinition.EXODUS_FLAME||spell==SpellDefinition.TORNADO_IMPACT,p.discardUnloaded());assertEquals(spell==SpellDefinition.EXODUS_FLAME,p.burstContacts());assertEquals(spell==SpellDefinition.TORNADO_IMPACT,p.harvestsTerrain());
			for(int age:new int[]{-1,0,119,120,179,180,999})for(double m:new double[]{Double.NaN,0,1E-9,1E-8,1}){
				boolean expected=switch(spell.id()){
					case "water_cannon"->com.example.examplemod.magic.spell.water.water_cannon.WaterCannonRules.expired(age,m);
					case "stone_cannon"->com.example.examplemod.magic.spell.earth.stone_cannon.StoneCannonRules.expired(age,m);
					case "ex_flame"->com.example.examplemod.magic.spell.fire.ex_flame.ExFlameRules.expired(age,m);
					case "exodus_flame"->com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameFlight.expired(age,spell.flightTicks(),m);
					case "tornado_impact"->com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactFlight.expired(age,spell.flightTicks(),m);
					default->age>=spell.flightTicks()||m<1E-8;
				};assertEquals(expected,p.expiry().expired(age,spell.flightTicks(),m));
			}
		}
	}
}
