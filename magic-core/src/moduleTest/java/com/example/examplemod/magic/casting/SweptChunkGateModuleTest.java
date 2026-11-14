package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.SweptChunkGate;
class SweptChunkGateModuleTest {
 @Test void negativeCoordinatesAndBoundariesKeepXMajorOrder(){var calls=new ArrayList<List<Integer>>();assertTrue(SweptChunkGate.available(-.01,16,-16,0,(x,z)->{calls.add(List.of(x,z));return true;}));assertEquals(List.of(List.of(-16,-16),List.of(-16,0),List.of(0,-16),List.of(0,0),List.of(16,-16),List.of(16,0)),calls);}
 @Test void firstMissingChunkStopsWithoutVisitingRemainder(){var calls=new ArrayList<List<Integer>>();assertFalse(SweptChunkGate.available(0,32,0,32,(x,z)->{calls.add(List.of(x,z));return z==0;}));assertEquals(List.of(List.of(0,0),List.of(0,16)),calls);}
 @Test void queryExceptionPropagatesAndDoesNotContinue(){var calls=new ArrayList<Integer>();assertThrows(IllegalStateException.class,()->SweptChunkGate.available(0,32,0,32,(x,z)->{calls.add(x);throw new IllegalStateException();}));assertEquals(List.of(0),calls);}
 @Test void frozenIterationMatchesRandomBounds(){var r=new Random(1113);for(int i=0;i<1000;i++){double x=r.nextDouble()*100-50,z=r.nextDouble()*100-50,w=r.nextDouble()*40,d=r.nextDouble()*40;var expected=new ArrayList<List<Integer>>();for(int a=(int)Math.floor(x)>>4;a<=((int)Math.floor(x+w)>>4);a++)for(int b=(int)Math.floor(z)>>4;b<=((int)Math.floor(z+d)>>4);b++)expected.add(List.of(a<<4,b<<4));var actual=new ArrayList<List<Integer>>();assertTrue(SweptChunkGate.available(x,x+w,z,z+d,(a,b)->{actual.add(List.of(a,b));return true;}));assertEquals(expected,actual);}}
}
