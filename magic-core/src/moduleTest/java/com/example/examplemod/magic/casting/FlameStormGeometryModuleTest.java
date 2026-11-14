package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.flame_storm.FlameStormGeometry;
class FlameStormGeometryModuleTest {
	@Test void wallMatchesOriginalDotOrderAcrossDirections(){var random=new java.util.Random(7);for(int i=0;i<10000;i++){double a=random.nextDouble()*Math.PI*2,fx=Math.cos(a),fz=Math.sin(a),x=random.nextDouble()*24-12,y=random.nextDouble()*12-2,z=random.nextDouble()*24-12;boolean old=y>=0&&y<=8&&Math.abs(x*(-fz)+y*0+z*fx)<=8&&Math.abs(x*fx+y*0+z*fz)<=2;assertEquals(old,FlameStormGeometry.wall(x,y,z,fx,0,fz));}}
	@Test void travelCapsWithoutChangingNegativeAge(){assertEquals(-.1,FlameStormGeometry.travel(-1));assertEquals(12,FlameStormGeometry.travel(120));assertEquals(24,FlameStormGeometry.travel(240));assertEquals(24,FlameStormGeometry.travel(1000));}
}
