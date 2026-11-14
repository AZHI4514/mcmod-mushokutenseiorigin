package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ContactCandidates;
class ContactCandidatesModuleTest {
 @Test void filteringFinishesBeforeStableSorting(){var events=new ArrayList<String>();var result=ContactCandidates.collect(List.of(3,1,2,4),x->{events.add("filter"+x);return x!=4;},x->{events.add("contact"+x);return x==3?1:x;});assertEquals(List.of(3,1,2),result);assertEquals(List.of("filter3","filter1","filter2","filter4"),events.subList(0,4));}
 @Test void rejectedCandidatesNeverReachComparator(){assertEquals(List.of(2),ContactCandidates.collect(List.of(1,2),x->x==2,x->{fail();return 0;}));}
 @Test void filterFailurePreventsSorting(){var events=new ArrayList<Integer>();assertThrows(IllegalStateException.class,()->ContactCandidates.collect(List.of(1,2),x->{if(x==2)throw new IllegalStateException();return true;},x->{events.add(x);return x;}));assertTrue(events.isEmpty());}
 @Test void matchesFrozenStableSortIncludingNaN(){var random=new Random(1113);for(int n=0;n<1000;n++){var input=new ArrayList<Integer>();for(int i=0;i<20;i++)input.add(random.nextInt(10));var expected=new ArrayList<Integer>();for(var x:input)if(x%2==0)expected.add(x);expected.sort(Comparator.comparingDouble(x->x==0?Double.NaN:x));assertEquals(expected,ContactCandidates.collect(input,x->x%2==0,x->x==0?Double.NaN:x));}}
 @Test void comparatorFailureLeavesSourceUnmodified(){var input=List.of(2,1);assertThrows(IllegalStateException.class,()->ContactCandidates.collect(input,x->true,x->{throw new IllegalStateException();}));assertEquals(List.of(2,1),input);}
}
