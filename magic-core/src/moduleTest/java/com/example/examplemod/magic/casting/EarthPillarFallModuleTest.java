package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarFall;
class EarthPillarFallModuleTest {
 @Test void frozenContactAndStopRulesMatchTenThousandInputs(){var r=new Random(3601);for(int i=0;i<10000;i++){double contact=r.nextDouble()*4-1,stop=r.nextDouble()*4-1;boolean claimed=r.nextBoolean();assertEquals(contact<=Math.min(.5,stop)&&!claimed,EarthPillarFall.contactDue(contact,stop,claimed));assertEquals(stop<=.5,EarthPillarFall.stopped(stop));assertEquals(stop<=.5?Math.max(0,stop):.5,EarthPillarFall.movement(stop));}}
 @Test void sharedStopPlaneIsInclusiveButHistorySuppressesRepeat(){assertTrue(EarthPillarFall.contactDue(.5,.5,false));assertFalse(EarthPillarFall.contactDue(.5,.5,true));assertFalse(EarthPillarFall.contactDue(.50001,Double.POSITIVE_INFINITY,false));assertEquals(.5,EarthPillarFall.movement(Double.POSITIVE_INFINITY));assertEquals(0,EarthPillarFall.movement(-1));}
}
