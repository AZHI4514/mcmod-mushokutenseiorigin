package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.FluidContactScan;
class FluidContactScanModuleTest {
 static class World implements FluidContactScan.World<Integer,Integer> {
  final List<String> events=new ArrayList<>();boolean penetrating;
  public boolean loaded(Integer p){events.add("loaded"+p);return p!=0;}
  public Integer fluid(Integer p){events.add("fluid"+p);return p;}
  public boolean empty(Integer f){events.add("empty"+f);return f==1;}
  public boolean ignoresWater(){events.add("ignores");return penetrating;}
  public boolean water(Integer f){events.add("water"+f);return f==2;}
  public double contact(Integer p,Integer f){assertSame(p,f);events.add("contact"+p);return p;}
 }
 @Test void unloadedAndEmptyShortCircuitBeforeWaterChecks(){var w=new World();assertEquals(Double.POSITIVE_INFINITY,FluidContactScan.first(List.of(0,1),w));assertEquals(List.of("loaded0","loaded1","fluid1","empty1"),w.events);}
 @Test void penetrationSkipsOnlyWaterAndStillHitsOtherFluids(){var w=new World();w.penetrating=true;assertEquals(3,FluidContactScan.first(List.of(2,3),w));assertFalse(w.events.contains("contact2"));assertTrue(w.events.contains("contact3"));}
 @Test void nonpenetratingDoesNotQueryWaterTag(){var w=new World();assertEquals(2,FluidContactScan.first(List.of(2),w));assertFalse(w.events.contains("water2"));}
 @Test void zeroContactDoesNotEndScanAndNaNPropagates(){var w=new World(){public double contact(Integer p,Integer f){super.contact(p,f);return p==2?0:Double.NaN;}};assertTrue(Double.isNaN(FluidContactScan.first(List.of(2,3),w)));assertTrue(w.events.contains("contact3"));}
 @Test void worldExceptionStopsRemainingReads(){var w=new World(){public Integer fluid(Integer p){super.fluid(p);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->FluidContactScan.first(List.of(2,3),w));assertEquals(List.of("loaded2","fluid2"),w.events);}
}
