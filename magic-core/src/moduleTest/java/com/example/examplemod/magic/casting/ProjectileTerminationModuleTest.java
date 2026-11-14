package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ProjectileTermination;
class ProjectileTerminationModuleTest {
	@Test void fluidWinsWallTieAndIncludesStepEnd(){assertTrue(ProjectileTermination.fluid(3,3,3));assertFalse(ProjectileTermination.fluid(Math.nextUp(3.0),3,4));assertFalse(ProjectileTermination.fluid(3,4,Math.nextDown(3.0)));}
	@Test void wallRequiresActualBlockAndIncludesStepEnd(){assertTrue(ProjectileTermination.wall(3,3,true));assertFalse(ProjectileTermination.wall(3,3,false));assertFalse(ProjectileTermination.wall(Math.nextUp(3.0),3,true));}
	@Test void frozenComparisonsPreserveExceptionalAndRandomInputs(){double[] edge={0,-1,Double.NaN,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY};for(double a:edge)for(double b:edge)for(double c:edge)assertEquals(a<=b&&a<=c,ProjectileTermination.fluid(a,b,c));var r=new java.util.Random(1113);for(int i=0;i<10000;i++){double a=r.nextDouble()*8,b=r.nextDouble()*8,c=r.nextDouble()*8;assertEquals(a<=b&&a<=c,ProjectileTermination.fluid(a,b,c));assertEquals(a<=b,ProjectileTermination.wall(a,b,true));}}
	static class World implements ProjectileTermination.World {
		final java.util.List<String> events=new java.util.ArrayList<>();
		public void position(double d){events.add("position:"+d);}
		public void wallEffect(){events.add("wallEffect");}
		public void particles(){events.add("particles");}
		public void discard(){events.add("discard");}
	}
	@Test void fluidTieSkipsWallEffectAndTerminatesInOrder(){var w=new World();assertTrue(ProjectileTermination.apply(2,2,3,true,w));assertEquals(java.util.List.of("position:2.0","particles","discard"),w.events);}
	@Test void earlierWallRunsEffectBeforeParticlesAndDiscard(){var w=new World();assertTrue(ProjectileTermination.apply(3,2,3,true,w));assertEquals(java.util.List.of("position:2.0","wallEffect","particles","discard"),w.events);}
	@Test void noReachedContactOrMissingWallLeavesMovementToCaller(){var w=new World();assertFalse(ProjectileTermination.apply(4,4,3,true,w));assertFalse(ProjectileTermination.apply(4,2,3,false,w));assertTrue(w.events.isEmpty());}
	@Test void wallEffectExceptionKeepsPositionAndDoesNotInventDiscard(){var w=new World(){public void wallEffect(){super.wallEffect();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ProjectileTermination.apply(4,2,3,true,w));assertEquals(java.util.List.of("position:2.0","wallEffect"),w.events);}
	@Test void particleExceptionDoesNotDiscardOrFallThroughToWall(){var w=new World(){public void particles(){super.particles();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ProjectileTermination.apply(2,2,3,true,w));assertEquals(java.util.List.of("position:2.0","particles"),w.events);}
}
