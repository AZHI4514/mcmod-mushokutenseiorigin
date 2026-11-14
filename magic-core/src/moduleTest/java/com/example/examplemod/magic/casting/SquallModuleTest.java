package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.squall.*;
import com.example.examplemod.magic.mechanic.field.RainExposure;
class SquallModuleTest {
	private static class World implements SquallTick.World {
		List<String> calls=new ArrayList<>();String reject="";int age;
		boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean ownerPresent(){return pass("present");}public boolean alive(){return pass("alive");}public boolean removed(){return !pass("removed");}public boolean sameLevel(){return pass("level");}public boolean expired(){return !pass("expiry");}public boolean adopt(){return pass("adopt");}
		public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public int age(){calls.add("age");return age;}public void apply(){calls.add("apply");}
	}
	@Test void invalidationPreservesShortCircuitAndRemoveBeforeDiscard(){var order=List.of("present","alive","removed","level","expiry","adopt");for(int i=0;i<order.size();i++){var w=new World();w.reject=order.get(i);SquallTick.tick(w);var expected=new ArrayList<>(order.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}}
	@Test void tenTickCadenceAlsoIncludesZero(){for(int age=0;age<100;age++){var w=new World();w.age=age;SquallTick.tick(w);assertEquals(age%10==0,w.calls.contains("apply"));assertFalse(w.calls.contains("discard"));}}
	@Test void durationAndShorteningDoNotCreateGlobalWeather(){assertEquals(900,SquallRules.duration(false));assertEquals(450,SquallRules.duration(true));assertEquals(100,SquallRules.shorten(100,120,50));assertEquals(110,SquallRules.shorten(100,120,10));assertEquals(130,SquallRules.shorten(100,120,-10));}
	@Test void horizontalRadiusIsInclusive(){assertTrue(SquallRules.inside(16,0));assertFalse(SquallRules.inside(16,.01));assertTrue(SquallRules.inside(Double.NaN,0));}
	@Test void exposureGatesAreLazyAndClearFirePrecedesWet(){for(int fail=0;fail<4;fail++){var calls=new ArrayList<String>();final int f=fail;var target=new RainExposure.Target(){public boolean inside(){calls.add("inside");return f!=0;}public boolean loaded(){calls.add("loaded");return f!=1;}public boolean skyVisible(){calls.add("sky");return f!=2;}public void extinguish(){calls.add("clear");}public void wet(){calls.add("wet");}};assertEquals(fail==3,RainExposure.apply(target));assertEquals(List.of("inside","loaded","sky","clear","wet").subList(0,fail==3?5:fail+1),calls);}}
	@Test void failedWetDoesNotRestoreFire(){var calls=new ArrayList<String>();var t=new RainExposure.Target(){public boolean inside(){return true;}public boolean loaded(){return true;}public boolean skyVisible(){return true;}public void extinguish(){calls.add("clear");}public void wet(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->RainExposure.apply(t));assertEquals(List.of("clear"),calls);}
	@Test void adoptExceptionDoesNotRemoveExistingState(){var w=new World(){public boolean adopt(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->SquallTick.tick(w));assertFalse(w.calls.contains("remove"));}
}
