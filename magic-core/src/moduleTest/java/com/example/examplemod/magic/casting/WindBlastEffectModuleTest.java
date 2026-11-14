package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.wind.wind_blast.WindBlastEffect;
class WindBlastEffectModuleTest {
	@Test void rejectedRefreshStillSucceedsAfterOneReverseImpulse(){var events=new ArrayList<String>();assertTrue(WindBlastEffect.apply(new Vec3d(1,0,0),new WindBlastEffect.Caster(){public void launch(Vec3d v){assertEquals(-.9,v.x,1E-15);assertEquals(.42,v.y);events.add("launch");}public boolean slowFall(int ticks){assertEquals(100,ticks);events.add("slowfall");return false;}}));assertEquals(List.of("launch","slowfall"),events);}
	@Test void launchFailureNeverAttemptsEffect(){assertThrows(IllegalStateException.class,()->WindBlastEffect.apply(new Vec3d(0,1,0),new WindBlastEffect.Caster(){public void launch(Vec3d v){throw new IllegalStateException();}public boolean slowFall(int ticks){fail();return true;}}));}
	@Test void effectFailureDoesNotUndoImpulse(){var events=new ArrayList<String>();assertThrows(IllegalStateException.class,()->WindBlastEffect.apply(new Vec3d(0,0,1),new WindBlastEffect.Caster(){public void launch(Vec3d v){events.add("launched");}public boolean slowFall(int ticks){throw new IllegalStateException();}}));assertEquals(List.of("launched"),events);}
	@Test void repeatedCastReplacesVelocityAndRequestsFixedDuration(){var velocities=new ArrayList<Vec3d>();var durations=new ArrayList<Integer>();var caster=new WindBlastEffect.Caster(){public void launch(Vec3d v){velocities.add(v);}public boolean slowFall(int ticks){durations.add(ticks);return true;}};WindBlastEffect.apply(new Vec3d(1,0,0),caster);WindBlastEffect.apply(new Vec3d(-1,0,0),caster);assertEquals(2,velocities.size());assertEquals(-velocities.get(0).x,velocities.get(1).x);assertEquals(List.of(100,100),durations);}
}
