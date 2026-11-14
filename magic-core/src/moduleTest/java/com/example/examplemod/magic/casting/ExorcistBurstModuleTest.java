package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.targeting.HolyAreaHit;
import com.example.examplemod.magic.spell.holy.exorcist_burst.ExorcistBurstPolicy;
class ExorcistBurstModuleTest {
	@Test void sphereIsCenteredAtEndpointWithInclusiveRadiusFour(){var p=new ExorcistBurstPolicy();assertTrue(p.endpointBurst());assertFalse(p.singleTarget());assertFalse(p.visibleBeam());assertEquals(1.5,p.burstSpread());assertTrue(p.shape(4,0,0,0,0,1,10));assertFalse(p.shape(4,0,.001,0,0,1,10));}
	@Test void shapeBeforeVisibilityBeforeDamageIsLazy(){for(int reject=0;reject<3;reject++){var calls=new ArrayList<String>();final int f=reject;HolyAreaHit.apply(new HolyAreaHit.Target(){public boolean inside(){calls.add("shape");return f!=0;}public boolean visible(){calls.add("visible");return f!=1;}public void hurt(){calls.add("hurt");}});assertEquals(List.of("shape","visible","hurt").subList(0,reject+1),calls);}}
	@Test void damageExceptionPropagatesWithoutExtraEffects(){assertThrows(IllegalStateException.class,()->HolyAreaHit.apply(new HolyAreaHit.Target(){public boolean inside(){return true;}public boolean visible(){return true;}public void hurt(){throw new IllegalStateException();}}));}
}
