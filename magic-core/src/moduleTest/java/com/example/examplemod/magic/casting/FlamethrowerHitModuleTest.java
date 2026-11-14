package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit;
class FlamethrowerHitModuleTest {
	@Test void conePreservesInclusiveRangeAngleAndNearOriginRules(){
		var dir=new Vec3d(0,0,1);
		assertTrue(FlamethrowerHit.inCone(new Vec3d(0,0,16),dir,16));assertFalse(FlamethrowerHit.inCone(new Vec3d(0,0,16.00001),dir,16));
		assertTrue(FlamethrowerHit.inCone(new Vec3d(0,0,-.0000001),dir,16));assertFalse(FlamethrowerHit.inCone(new Vec3d(0,0,-1),dir,16));
		assertTrue(FlamethrowerHit.inCone(new Vec3d(Math.sin(Math.toRadians(24.99)),0,Math.cos(Math.toRadians(24.99))),dir,16));
		assertFalse(FlamethrowerHit.inCone(new Vec3d(Math.sin(Math.toRadians(25.01)),0,Math.cos(Math.toRadians(25.01))),dir,16));
	}
	@Test void visibilityPrecedesClaimAndClaimPrecedesDamageWithoutReorderingCandidates(){
		var events=new ArrayList<String>();
		FlamethrowerHit.apply(new Vec3d(0,0,1),new FlamethrowerHit.Targets<Integer>(){
			public Iterable<Integer> candidates(){return List.of(1,2,3,4);}
			public Vec3d delta(Integer t){return t==1?new Vec3d(10,0,1):new Vec3d(0,0,2);}
			public boolean visible(Integer t){events.add("visible"+t);return t!=2;}
			public boolean claim(Integer t){events.add("claim"+t);return t!=3;}
			public void damage(Integer t){events.add("damage"+t);}
		});assertEquals(List.of("visible2","visible3","claim3","visible4","claim4","damage4"),events);
	}
	@Test void frozenLegacyConeExpressionMatchesTwoThousandFiniteSamples(){
		var random=new Random(1111);
		for(int i=0;i<2000;i++){
			var d=new Vec3d(random.nextDouble()*40-20,random.nextDouble()*40-20,random.nextDouble()*40-20);
			var forward=new Vec3d(random.nextDouble()-.5,random.nextDouble()-.5,random.nextDouble()-.5).normalize();
			double length=Math.sqrt(d.x*d.x+d.y*d.y+d.z*d.z);
			boolean expected=length<=16&&(length<1.0E-6||(d.x*forward.x+d.y*forward.y+d.z*forward.z)/length>=Math.cos(Math.toRadians(25)));
			assertEquals(expected,FlamethrowerHit.inCone(d,forward,16));
		}
	}
}
