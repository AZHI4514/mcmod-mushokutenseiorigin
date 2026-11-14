package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldSelection;
class PhysicalShieldSelectionModuleTest {
	@Test void meleeStopsAtFirstFiniteRatherThanNearest(){var seen=new ArrayList<Integer>();assertEquals(8,PhysicalShieldSelection.firstIntersecting(List.of(8,2),x->true,x->{seen.add(x);return x;}));assertEquals(List.of(8),seen);}
	@Test void projectileSelectsNearestAndRetainsFirstTie(){var a=new String("a");var b=new String("b");var c=PhysicalShieldSelection.nearest(List.of(a,b),x->true,x->2);assertSame(a,c.shield());assertEquals(2,c.distance());}
	@Test void rejectedCandidatesNeverQueryGeometry(){assertNull(PhysicalShieldSelection.nearest(List.of(1),x->false,x->{fail();return 0;}));assertNull(PhysicalShieldSelection.firstIntersecting(List.of(1),x->false,x->{fail();return 0;}));}
	@Test void meleeRejectsAllNonfiniteContacts(){assertNull(PhysicalShieldSelection.firstIntersecting(List.of(Double.NaN,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY),x->true,x->x));}
	@Test void projectilePreservesStrictComparisonIncludingNegativeInfinity(){var result=PhysicalShieldSelection.nearest(List.of(Double.NaN,Double.POSITIVE_INFINITY,5.0,Double.NEGATIVE_INFINITY),x->true,x->x);assertEquals(Double.NEGATIVE_INFINITY,result.distance());assertNull(PhysicalShieldSelection.nearest(List.of(Double.NaN,Double.POSITIVE_INFINITY),x->true,x->x));}
	@Test void emptyCandidatesReturnNoSelection(){assertNull(PhysicalShieldSelection.nearest(List.<Integer>of(),x->true,x->x));assertNull(PhysicalShieldSelection.firstIntersecting(List.<Integer>of(),x->true,x->x));}
	@Test void frozenSelectionMatchesTenThousandSequences(){var r=new Random(3941);for(int i=0;i<10000;i++){var xs=new ArrayList<Double>();for(int j=0;j<12;j++)xs.add(r.nextInt(10)==0?Double.NaN:r.nextDouble()*30-2);Double old=null;double distance=Double.POSITIVE_INFINITY;for(var x:xs)if(x<distance){old=x;distance=x;}var actual=PhysicalShieldSelection.nearest(xs,x->true,x->x);if(old==null)assertNull(actual);else{assertSame(old,actual.shield());assertEquals(distance,actual.distance());}}}
}
