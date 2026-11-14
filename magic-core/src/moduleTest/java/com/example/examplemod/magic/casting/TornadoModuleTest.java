package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.*;
import com.example.examplemod.magic.spell.wind.tornado.TornadoPolicy;
class TornadoModuleTest {
	private static class Target implements WindFieldPulse.Target {
		List<String> calls=new ArrayList<>();String reject="";boolean pass(String key){calls.add(key);return !reject.equals(key);}
		public boolean claim(){return pass("claim");}public boolean hurt(){return pass("hurt");}public void fireEffects(){calls.add("fire");}public void afterHit(){calls.add("hook");}public void slow(){calls.add("slow");}
	}
	@Test void claimPrecedesDamageAndNormalWindHasNoFireEffects(){var t=new Target();WindFieldPulse.apply(false,false,t);assertEquals(List.of("claim","hurt","hook"),t.calls);}
	@Test void cadenceRefusalSkipsDamage(){var t=new Target();t.reject="claim";WindFieldPulse.apply(false,false,t);assertEquals(List.of("claim"),t.calls);}
	@Test void damageRefusalDoesNotNotifyHook(){var t=new Target();t.reject="hurt";WindFieldPulse.apply(false,false,t);assertEquals(List.of("claim","hurt"),t.calls);}
	@Test void tornadoGeometryMovementAndPlayerCapAreIndependentPolicy(){var p=new TornadoPolicy();assertEquals(5,p.radius());assertEquals(20,p.height());assertTrue(p.moving());assertFalse(p.excludesOwner());assertFalse(p.disperses());assertTrue(p.movesTarget(true));assertFalse(p.movesTarget(false));var v=p.impulse(1,0,0,.34,0,true);assertEquals(-.12,v.x());assertEquals(.35,v.y());var mob=p.impulse(1,0,0,.34,0,false);assertEquals(.42,mob.y(),1E-15);}
	@Test void pullPointsTowardAxisAtEveryAngle(){var p=new TornadoPolicy();for(int i=0;i<360;i++){double x=Math.cos(Math.toRadians(i)),z=Math.sin(Math.toRadians(i));var v=p.impulse(x,z,0,0,0,false);assertTrue(v.x()*x+v.z()*z<0);assertEquals(.08,v.y());}}
	@Test void postHitExceptionStopsLaterEffects(){var t=new Target(){public void afterHit(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WindFieldPulse.apply(false,false,t));assertTrue(t.calls.contains("hurt"));assertFalse(t.calls.contains("slow"));}
}
