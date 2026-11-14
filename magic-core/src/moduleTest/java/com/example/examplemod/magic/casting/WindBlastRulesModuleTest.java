package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.wind.wind_blast.WindBlastRules;
class WindBlastRulesModuleTest {
	private static double peak(double v){double h=0;for(int t=0;t<200&&v>0;t++){h+=v;v=(v-.08)*.98;}return h;}
	private static Vec3d old(Vec3d d){var n=d.normalize();double y;if(n.y>0){double lo=0,hi=Math.max(2,10*n.y);for(int i=0;i<48;i++){double m=(lo+hi)/2;if(peak(m)<10*n.y)lo=m;else hi=m;}y=(lo+hi)/2;}else y=n.y<0?-1.2*Math.abs(n.y):.42;return new Vec3d(n.x*(10*(1-.91)),y,n.z*(10*(1-.91)));}
	@Test void frozenImpulseMatchesTenThousandDirections(){var r=new Random(3410);for(int i=0;i<10000;i++){var d=new Vec3d(r.nextDouble()*2-1,r.nextDouble()*2-1,r.nextDouble()*2-1);var a=old(d);var b=WindBlastRules.velocity(d);assertEquals(a.x,b.x,0);assertEquals(a.y,b.y,0);assertEquals(a.z,b.z,0);}}
	@Test void horizontalRecoilHasLiftAndNominalTenBlockDragScale(){var v=WindBlastRules.velocity(new Vec3d(-1,0,0));assertEquals(-.9,v.x,1E-15);assertEquals(.42,v.y);assertEquals(10,Math.abs(v.x)/(1-.91),1E-12);assertEquals(100,WindBlastRules.SLOW_FALL_TICKS);assertEquals(8,WindBlastRules.MANA);}
	@Test void verticalAndTinyDirectionBoundariesRemainUnchanged(){assertEquals(10,peak(WindBlastRules.velocity(new Vec3d(0,1,0)).y),1E-12);assertEquals(-1.2,WindBlastRules.velocity(new Vec3d(0,-1,0)).y);assertEquals(.42,WindBlastRules.velocity(Vec3d.ZERO).y);assertEquals(0,WindBlastRules.velocity(new Vec3d(1E-6,0,0)).x);assertTrue(WindBlastRules.velocity(new Vec3d(1E-5,0,0)).x>0);}
}
