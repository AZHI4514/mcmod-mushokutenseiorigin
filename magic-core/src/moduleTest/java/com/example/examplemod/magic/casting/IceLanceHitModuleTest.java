package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.port.LocalHitTargets;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.spell.water.ice_lance.IceLanceHit;
class IceLanceHitModuleTest {
	private static final class Targets implements LocalHitTargets<Integer> {
		final List<String> events=new ArrayList<>();
		public Iterable<Integer> candidates(){events.add("query");return List.of(1,2,3,4);}
		public Box3d bounds(Integer target){events.add("bounds"+target);return target==2?new Box3d(3,0,3,4,1,4):new Box3d(-.1,0,-.1,.1,1,.1);}
		public boolean visible(Integer target){events.add("visible"+target);return target!=3;}
		public void damage(Integer target){events.add("damage"+target);}
	}
	@Test void filtersGeometryBeforeVisibilityAndPreservesCandidateDamageOrder(){
		var t=new Targets();IceLanceHit.apply(1,new Vec3d(0,1.5,0),new Vec3d(0,0,1),t);
		assertEquals(List.of("query","bounds1","visible1","damage1","bounds2","bounds3","visible3","bounds4","visible4","damage4"),t.events);
	}
	@Test void restoredPostHitAgesNeverQueryOrRepeatDamage(){
		var t=new Targets();for(int age:new int[]{0,2,11,12,200,Integer.MAX_VALUE})IceLanceHit.apply(age,Vec3d.ZERO,new Vec3d(0,0,1),t);assertTrue(t.events.isEmpty());
	}
	@Test void worldDamageExceptionKeepsOriginalStopAtFailingTarget(){
		var hit=new ArrayList<Integer>();var t=new LocalHitTargets<Integer>() {
			public Iterable<Integer> candidates(){return List.of(1,2);}
			public Box3d bounds(Integer id){return new Box3d(-.1,0,-.1,.1,1,.1);}
			public boolean visible(Integer id){return true;}
			public void damage(Integer id){hit.add(id);throw new IllegalStateException();}
		};
		assertThrows(IllegalStateException.class,()->IceLanceHit.apply(1,new Vec3d(0,1.5,0),new Vec3d(0,0,1),t));assertEquals(List.of(1),hit);
	}
	@Test void horizontalRotationRejectsBroadPhaseCornersAndKeepsHeightBoundary(){
		double q=Math.sqrt(.5);var hit=new ArrayList<Integer>();
		var boxes=List.of(new Box3d(.61,1,.61,.65,2,.65),new Box3d(.1,2.99,.1,.2,3.01,.2),new Box3d(.1,3.001,.1,.2,3.1,.2));
		var targets=new LocalHitTargets<Integer>() {
			public Iterable<Integer> candidates(){return List.of(0,1,2);}
			public Box3d bounds(Integer id){return boxes.get(id);}
			public boolean visible(Integer id){assertNotEquals(0,id);assertNotEquals(2,id);return true;}
			public void damage(Integer id){hit.add(id);}
		};
		IceLanceHit.apply(1,new Vec3d(0,1.5,0),new Vec3d(q,0,q),targets);assertEquals(List.of(1),hit);
	}
}
