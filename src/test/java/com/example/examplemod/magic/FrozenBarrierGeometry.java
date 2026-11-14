package com.example.examplemod.magic;

import net.minecraft.world.phys.Vec3;

/** First boundary hit of a swept segment, including an outside-to-outside crossing. */
public final class FrozenBarrierGeometry {
	public static double sphere(Vec3 center,double radius,boolean hemisphere,Vec3 from,Vec3 movement){
		var offset=from.subtract(center);double a=movement.lengthSqr();if(a<1E-16||radius<=0)return Double.POSITIVE_INFINITY;
		double b=2*offset.dot(movement),c=offset.lengthSqr()-radius*radius,discriminant=b*b-4*a*c;
		double first=Double.POSITIVE_INFINITY;
		if(discriminant>1E-10){double root=Math.sqrt(discriminant);for(double t:new double[]{(-b-root)/(2*a),(-b+root)/(2*a)})
			if(t>=0&&t<=1&&(!hemisphere||offset.y+movement.y*t>=0))first=Math.min(first,t);}
		if(hemisphere&&Math.abs(movement.y)>1E-12){double t=-offset.y/movement.y;if(t>=0&&t<=1){var p=offset.add(movement.scale(t));if(p.horizontalDistanceSqr()<radius*radius)first=Math.min(first,t);}}
		return first;
	}
	private FrozenBarrierGeometry(){}
}
