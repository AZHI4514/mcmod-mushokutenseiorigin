package com.example.examplemod.magic.spell.fire.ex_flame;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.mechanic.projectile.BlockBurstRules;
public final class ExFlameRules {
	public static int ignitionTicks(float roll,boolean immune,boolean resistance) { return roll<.8F && !immune && !resistance ? ExFlameSpec.IGNITION_TICKS : 0; }
	public static boolean expired(int age,double motionSquared) { return BlockBurstRules.expired(age,ExFlameSpec.FLIGHT_TICKS,motionSquared); }
	public static boolean directContact(double contact,double wall,double fluid,boolean alreadyHit) { return BlockBurstRules.directContact(contact,wall,fluid,alreadyHit); }
	public static Box3d blockImpactBounds(Vec3d center) { return BlockBurstRules.bounds(center); }
	private ExFlameRules() { }
}
