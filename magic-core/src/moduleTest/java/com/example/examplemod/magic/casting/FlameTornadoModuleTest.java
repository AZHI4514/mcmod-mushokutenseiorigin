package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.WindFieldPulse;
import com.example.examplemod.magic.spell.mixed.flame_tornado.FlameTornadoPolicy;
class FlameTornadoModuleTest {
	private static class Target implements WindFieldPulse.Target {
		List<String> calls=new ArrayList<>();boolean hurt=true;
		public boolean claim(){calls.add("claim");return true;}public boolean hurt(){calls.add("hurt");return hurt;}public void fireEffects(){calls.add("fire");}public void afterHit(){calls.add("hook");}public void slow(){fail();}
	}
	@Test void successfulDamageAppliesFlameBeforeHook(){var t=new Target();WindFieldPulse.apply(true,false,t);assertEquals(List.of("claim","hurt","fire","hook"),t.calls);}
	@Test void refusedDamageDoesNotIgnite(){var t=new Target();t.hurt=false;WindFieldPulse.apply(true,false,t);assertEquals(List.of("claim","hurt"),t.calls);}
	@Test void flameFailureDoesNotNotifyHook(){var t=new Target(){public void fireEffects(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WindFieldPulse.apply(true,false,t));assertFalse(t.calls.contains("hook"));}
	@Test void geometryAndPulseMovementDifferFromOrdinaryTornado(){var p=new FlameTornadoPolicy();assertEquals(5,p.radius());assertEquals(18,p.height());assertTrue(p.flame());assertTrue(p.moving());assertFalse(p.excludesOwner());assertTrue(p.movesTarget(true));assertFalse(p.movesTarget(false));assertFalse(p.disperses());}
	@Test void verticalUsesOriginalVelocityPlusPointZeroSix(){assertEquals(.26,FlameTornadoPolicy.vertical(.2,true));assertEquals(.35,FlameTornadoPolicy.vertical(.34,true));assertEquals(.4,FlameTornadoPolicy.vertical(.34,false));assertTrue(Double.isNaN(FlameTornadoPolicy.vertical(Double.NaN,true)));}
}
