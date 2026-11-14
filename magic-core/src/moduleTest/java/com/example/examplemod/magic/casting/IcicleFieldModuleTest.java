package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.water.icicle_field.IcicleFieldRules;
import com.example.examplemod.magic.mechanic.projectile.DeferredAreaImpact;
class IcicleFieldModuleTest {
	@Test void colliderEndpointKeepsBackoffAndTwelveBlockMiss() {
		assertEquals(12,IcicleFieldRules.center(Vec3d.ZERO,new Vec3d(1,0,0),null).x);
		assertEquals(2.98,IcicleFieldRules.center(Vec3d.ZERO,new Vec3d(1,0,0),new Vec3d(3,4,5)).x);
	}
	@Test void inclusiveSpherePreservesOriginalRejectionComparison() {
		assertTrue(IcicleFieldRules.within(Vec3d.ZERO,new Vec3d(6,0,0)));
		assertFalse(IcicleFieldRules.within(Vec3d.ZERO,new Vec3d(6.001,0,0)));
		assertTrue(IcicleFieldRules.within(Double.NaN,0,0));
		var random=new Random(142);for(int i=0;i<10000;i++) { var t=new Vec3d(random.nextDouble()*16-8,random.nextDouble()*16-8,random.nextDouble()*16-8);assertEquals(!(t.x*t.x+t.y*t.y+t.z*t.z>36),IcicleFieldRules.within(Vec3d.ZERO,t)); }
	}
	private static class World implements DeferredAreaImpact.World<Integer> {
		final List<String> events=new ArrayList<>();boolean wet=true;
		public Iterable<Integer> targets(){return List.of(1,2,3);}
		public boolean affects(Integer t){events.add("eligible"+t);return t!=3;}
		public boolean wet(Integer t){events.add("wet"+t);return wet;}
		public boolean hurt(Integer t){events.add("hurt"+t);wet=false;return true;}
		public boolean applyEffects(Integer t,boolean wasWet){events.add("effects"+t+":"+wasWet);return wasWet;}
		public void afterHit(Integer t){events.add("hook"+t);}
		public void encase(Integer t){events.add("encase"+t);}
		public void burst(){events.add("burst");}
	}
	@Test void wetSnapshotPrecedesDamageAndAllHitsPrecedeTerrain(){
		var w=new World();DeferredAreaImpact.apply(w);assertEquals(List.of("eligible1","wet1","hurt1","effects1:true","hook1","eligible2","wet2","hurt2","effects2:false","hook2","eligible3","encase1","burst"),w.events);
	}
	@Test void refusedDamageHasNoEffectsHookOrTerrain(){
		var w=new World(){public boolean hurt(Integer t){events.add("refuse"+t);return false;}};DeferredAreaImpact.apply(w);assertEquals(List.of("eligible1","wet1","refuse1","eligible2","wet2","refuse2","eligible3","burst"),w.events);
	}
	@Test void partialHitFailureDoesNotPlaceDeferredTerrainOrBurst(){
		var w=new World(){public void afterHit(Integer t){super.afterHit(t);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->DeferredAreaImpact.apply(w));assertFalse(w.events.contains("encase1"));assertFalse(w.events.contains("burst"));
	}
	@Test void terrainFailureDoesNotRunFinalBurst(){
		var w=new World(){public void encase(Integer t){super.encase(t);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->DeferredAreaImpact.apply(w));assertFalse(w.events.contains("burst"));assertTrue(w.events.contains("hook2"));
	}
}
