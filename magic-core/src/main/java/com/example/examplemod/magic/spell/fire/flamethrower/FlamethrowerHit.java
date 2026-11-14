package com.example.examplemod.magic.spell.fire.flamethrower;
import com.example.examplemod.magic.domain.model.Vec3d;
/** Cone, visibility and contact claim remain in their original short-circuit order. */
public final class FlamethrowerHit {
	public interface Targets<T> {
		Iterable<T> candidates();
		Vec3d delta(T target);
		boolean visible(T target);
		boolean claim(T target);
		void damage(T target);
	}
	public static boolean inCone(Vec3d delta,Vec3d direction,double range) {
		double length=delta.length();
		return length<=range&&(length<1.0E-6||delta.dot(direction)/length>=Math.cos(Math.toRadians(FlamethrowerSpec.HALF_ANGLE_DEGREES)));
	}
	public static <T> void apply(Vec3d direction,Targets<T> targets) {
		for(var target:targets.candidates())
			if(inCone(targets.delta(target),direction,FlamethrowerSpec.RANGE)&&targets.visible(target)&&targets.claim(target))targets.damage(target);
	}
	private FlamethrowerHit() { }
}
