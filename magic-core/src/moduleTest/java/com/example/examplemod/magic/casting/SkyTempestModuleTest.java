package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.sky_tempest.SkyTempestPolicy;
class SkyTempestModuleTest {
	@Test void centerDamageIncludesEightBlockBoundary(){assertEquals(7,SkyTempestPolicy.damage(8,0,4));assertEquals(4,SkyTempestPolicy.damage(8,.001,4));assertEquals(4,SkyTempestPolicy.damage(Double.NaN,0,4));}
	@Test void geometryAndEveryTickMovementDifferFromTornado(){var p=new SkyTempestPolicy();assertEquals(24,p.radius());assertEquals(40,p.height());assertTrue(p.movesTarget(false));assertTrue(p.movesTarget(true));assertFalse(p.moving());assertFalse(p.excludesOwner());assertTrue(p.disperses());assertFalse(p.violent());}
	@Test void swirlIsTangentialAtEveryAngle(){var p=new SkyTempestPolicy();for(int i=0;i<360;i++){double x=Math.cos(Math.toRadians(i)),z=Math.sin(Math.toRadians(i));var v=p.impulse(x,z,0,0,0,false);assertEquals(0,v.x()*x+v.z()*z,1E-15);assertEquals(.08,Math.hypot(v.x(),v.z()),1E-15);assertEquals(.04,v.y());}}
	@Test void playerCapDoesNotApplyToMobs(){var p=new SkyTempestPolicy();assertEquals(.35,p.impulse(1,0,0,.34,0,true).y());assertEquals(.38,p.impulse(1,0,0,.34,0,false).y(),1E-15);}
}
