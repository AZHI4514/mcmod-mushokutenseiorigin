package com.example.examplemod.magic.mechanic.projectile;
import com.example.examplemod.magic.domain.model.*;
public final class BlockBurstRules {
	public static boolean expired(int age,int limit,double motionSquared) { return age>=limit || motionSquared<1E-8; }
	public static boolean directContact(double contact,double wall,double fluid,boolean alreadyHit) { return !alreadyHit && contact<Math.min(wall,fluid); }
	public static Box3d bounds(Vec3d center) { return new Box3d(center.x-1.5,center.y-1.5,center.z-1.5,center.x+1.5,center.y+1.5,center.z+1.5); }
	private BlockBurstRules() { }
}
