package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ImpactHistory;
import com.example.examplemod.magic.spell.earth.earth_pillar.*;
class EarthPillarHistoryModuleTest {
	@Test void restoredHistorySuppressesRepeatButAllowsNextTargetAtStopPlane(){var first=UUID.randomUUID();var next=UUID.randomUUID();var h=new ImpactHistory();assertTrue(h.claim(first));var restored=new ImpactHistory(h.snapshot());assertFalse(EarthPillarFall.contactDue(.25,.25,restored.contains(first)));assertTrue(EarthPillarFall.contactDue(.25,.25,restored.contains(next)));assertTrue(restored.claim(next));assertEquals(Set.of(first,next),restored.snapshot());assertEquals(Set.of(first),h.snapshot());}
	@Test void multipleTargetsClaimBeforeDamageThenStopEvenWhenStoneCannotFit(){var events=new ArrayList<String>();var h=new ImpactHistory();var a=UUID.randomUUID();var b=UUID.randomUUID();for(var id:List.of(a,a,b)){if(EarthPillarFall.contactDue(.25,.25,h.contains(id))&&h.claim(id)){assertTrue(h.contains(id));events.add("damage");}}EarthPillarStop.apply(.25,new EarthPillarStop.World(){public void move(double d){events.add("move");}public void show(){events.add("show");}public boolean hasOwner(){return true;}public void stone(){assertFalse(EarthPillarStone.place(List.of(0),new EarthPillarStone.Placement<Integer>(){public boolean available(Integer c){return false;}public void place(Integer c){fail();}}));events.add("no-stone");}public void discard(){events.add("discard");}});assertEquals(List.of("damage","damage","move","show","no-stone","discard"),events);assertEquals(2,h.snapshot().size());}
}
