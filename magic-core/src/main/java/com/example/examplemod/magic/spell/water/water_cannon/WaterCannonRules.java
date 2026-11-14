package com.example.examplemod.magic.spell.water.water_cannon;
import com.example.examplemod.magic.domain.model.Box3d;
import com.example.examplemod.magic.domain.model.Vec3d;
/** Pure water-cannon decisions. Vanilla target eligibility and terrain permissions stay in adapters. */
public final class WaterCannonRules {
	public static int wetTicks(float roll) { return roll < .6F ? WaterCannonSpec.WET_TICKS : 0; }
	public static boolean expired(int age,double motionSquared) { return com.example.examplemod.magic.mechanic.projectile.BlockBurstRules.expired(age,WaterCannonSpec.FLIGHT_TICKS,motionSquared); }
	public static boolean directContact(double contact,double wall,double fluid,boolean alreadyHit) { return com.example.examplemod.magic.mechanic.projectile.BlockBurstRules.directContact(contact,wall,fluid,alreadyHit); }
	public static Box3d blockImpactBounds(Vec3d center) { return com.example.examplemod.magic.mechanic.projectile.BlockBurstRules.bounds(center); }
	private WaterCannonRules() { }
}
