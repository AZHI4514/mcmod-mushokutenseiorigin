package com.example.examplemod.magic.mechanic.projectile;
import java.util.Map;
import com.example.examplemod.magic.SpellDefinition;
/** Explicit policies for the common swept carrier; custom rules retain their own implementations. */
public record SweptProjectilePolicy(Expiry expiry,Candidate candidate,double width,double height,double depth,boolean discardUnloaded,boolean burstContacts,boolean harvestsTerrain,int blockBurstWidth,boolean blockBurstFire) {
	@FunctionalInterface public interface Expiry {boolean expired(int age,int lifetime,double motionSquared);}
	@FunctionalInterface public interface Candidate {boolean accepts(double contact,double wall,double fluid,boolean hit);}
	private static final Expiry STANDARD_EXPIRY=(age,lifetime,motion)->age>=lifetime||motion<1E-8;
	private static final Candidate STANDARD_CONTACT=(contact,wall,fluid,hit)->!hit&&contact<Math.min(wall,fluid);
	private static final SweptProjectilePolicy DEFAULT=new SweptProjectilePolicy(STANDARD_EXPIRY,STANDARD_CONTACT,.25,.25,.25,false,false,false,0,false);
	private static final Map<SpellDefinition,SweptProjectilePolicy> POLICIES=Map.of(
		SpellDefinition.STONE_CANNON,new SweptProjectilePolicy((a,l,m)->com.example.examplemod.magic.spell.earth.stone_cannon.StoneCannonRules.expired(a,m),com.example.examplemod.magic.spell.earth.stone_cannon.StoneCannonRules::directContact,.25,.25,.25,false,false,false,0,false),
		SpellDefinition.WATER_CANNON,new SweptProjectilePolicy((a,l,m)->com.example.examplemod.magic.spell.water.water_cannon.WaterCannonRules.expired(a,m),com.example.examplemod.magic.spell.water.water_cannon.WaterCannonRules::directContact,.25,.25,.25,false,false,false,1,false),
		SpellDefinition.EX_FLAME,new SweptProjectilePolicy((a,l,m)->com.example.examplemod.magic.spell.fire.ex_flame.ExFlameRules.expired(a,m),com.example.examplemod.magic.spell.fire.ex_flame.ExFlameRules::directContact,2,2,2,false,false,false,2,true),
		SpellDefinition.SONIC_BOOM,new SweptProjectilePolicy(STANDARD_EXPIRY,STANDARD_CONTACT,3,1,.25,false,false,false,0,false),
		SpellDefinition.EXODUS_FLAME,new SweptProjectilePolicy(com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameFlight::expired,(c,w,f,h)->!h&&com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameFlight.candidate(c,w,f),3,3,3,true,true,false,0,false),
		SpellDefinition.TORNADO_IMPACT,new SweptProjectilePolicy(com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactFlight::expired,(c,w,f,h)->!h&&com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactFlight.candidate(c,w,f),1.5,1.5,3,true,false,true,0,false));
	public static SweptProjectilePolicy of(SpellDefinition spell){return POLICIES.getOrDefault(spell,DEFAULT);}
	public boolean skip(double contact,double distance,boolean hit){return burstContacts?com.example.examplemod.magic.spell.fire.exodus_flame.ExodusFlameFlight.skipContact(contact,distance,contact>distance?false:hit):contact>distance;}
}
