package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldAbsorb;
class PhysicalShieldAbsorbModuleTest {
	static class Shield implements PhysicalShieldAbsorb.Shield {float capacity=16;final List<String> events=new ArrayList<>();public float capacity(){return capacity;}public void capacity(float v){capacity=v;events.add("capacity");}public void show(){events.add("show");}public void discard(){events.add("discard");}}
	@Test void cumulativePhysicalDamageBreaksAfterVisual(){var s=new Shield();PhysicalShieldAbsorb.apply(6,s);assertEquals(10,s.capacity);PhysicalShieldAbsorb.apply(10,s);assertEquals(0,s.capacity);assertEquals(List.of("capacity","show","capacity","show","discard"),s.events);}
	@Test void frozenCapacityMatchesTenThousandFiniteInputs(){var r=new Random(3900);for(int i=0;i<10000;i++){var s=new Shield();s.capacity=r.nextFloat()*32;float incoming=r.nextFloat()*40-4;float expected=Math.max(0,s.capacity-Math.max(0,incoming));PhysicalShieldAbsorb.apply(incoming,s);assertEquals(expected,s.capacity);assertEquals(expected<=0,s.events.contains("discard"));}}
	@Test void visualExceptionKeepsCapacityWriteAndOriginalExceptionOrder(){var s=new Shield(){public void show(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->PhysicalShieldAbsorb.apply(16,s));assertEquals(0,s.capacity);assertEquals(List.of("capacity"),s.events);}
}
