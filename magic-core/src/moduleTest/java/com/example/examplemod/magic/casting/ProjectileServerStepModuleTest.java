package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ProjectileServerStep;
class ProjectileServerStepModuleTest {
 static class World implements ProjectileServerStep.World {
  final List<Integer> events=new ArrayList<>();boolean loaded=true,targetStop,terminalStop;int age;
  public boolean loaded(){events.add(1);return loaded;}
  public void unloaded(){events.add(0);}
  public void advanceAge(){events.add(2);age++;}
  public void solidsAndTerrain(){events.add(3);}
  public void fluids(){events.add(4);}
  public boolean targets(){events.add(5);return targetStop;}
  public boolean terminate(){events.add(6);return terminalStop;}
  public void move(){events.add(7);}
 }
 @Test void completeSweepOrder(){var w=new World();ProjectileServerStep.apply(w);assertEquals(List.of(1,2,3,4,5,6,7),w.events);assertEquals(1,w.age);}
 @Test void missingChunkDoesNotAdvanceAge(){var w=new World();w.loaded=false;ProjectileServerStep.apply(w);assertEquals(List.of(1,0),w.events);assertEquals(0,w.age);}
 @Test void targetTerminationSkipsBoundaryAndMovement(){var w=new World();w.targetStop=true;ProjectileServerStep.apply(w);assertEquals(List.of(1,2,3,4,5),w.events);}
 @Test void boundaryTerminationSkipsMovement(){var w=new World();w.terminalStop=true;ProjectileServerStep.apply(w);assertEquals(List.of(1,2,3,4,5,6),w.events);}
 @Test void terrainExceptionKeepsAgeAndPreventsFluidRead(){var w=new World(){public void solidsAndTerrain(){super.solidsAndTerrain();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ProjectileServerStep.apply(w));assertEquals(1,w.age);assertEquals(List.of(1,2,3),w.events);}
 @Test void fluidExceptionStopsBeforeTargets(){var w=new World(){public void fluids(){super.fluids();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ProjectileServerStep.apply(w));assertEquals(List.of(1,2,3,4),w.events);}
}
