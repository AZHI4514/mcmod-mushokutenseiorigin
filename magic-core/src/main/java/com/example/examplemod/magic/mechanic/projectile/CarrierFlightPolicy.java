package com.example.examplemod.magic.mechanic.projectile;
import java.util.Map;
import com.example.examplemod.magic.SpellDefinition;
/** Contact-edge and presentation capabilities of the directional carrier. */
public record CarrierFlightPolicy(boolean strictPiercing,boolean bladeSection,boolean explosion,boolean windSlice) {
	private static final CarrierFlightPolicy DEFAULT=new CarrierFlightPolicy(false,false,false,false);
	private static final Map<SpellDefinition,CarrierFlightPolicy> POLICIES=Map.of(
		SpellDefinition.ICICLE_BREAK,new CarrierFlightPolicy(true,false,false,false),SpellDefinition.ICICLE_EDGE,new CarrierFlightPolicy(true,true,false,false),
		SpellDefinition.FLOOD_FLUSH,new CarrierFlightPolicy(true,false,false,false),SpellDefinition.EXPLOSION,new CarrierFlightPolicy(true,false,true,false),SpellDefinition.WIND_SLICE,new CarrierFlightPolicy(true,false,false,true));
	public static CarrierFlightPolicy of(SpellDefinition spell){return POLICIES.getOrDefault(spell,DEFAULT);}
	public boolean candidate(double contact,double wall,double fluid,boolean hit){return strictPiercing?PiercingFlightRules.candidate(contact,wall,fluid,hit):!hit&&contact<Math.min(wall,fluid);}
	public boolean skip(double contact,double distance,boolean hit){return strictPiercing?!PiercingFlightRules.contactDue(contact,distance,hit):contact>distance||hit;}
}
