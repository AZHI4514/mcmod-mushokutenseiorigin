package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarSpawn;
class EarthPillarSpawnModuleTest {
 @Test void frozenSpawnGeometryMatchesTenThousandPoints(){var r=new Random(3600);for(int i=0;i<10000;i++){double x=r.nextDouble()*200-100,y=r.nextDouble()*400-100,z=r.nextDouble()*200-100;var t=EarthPillarSpawn.top(new Vec3d(x,y,z));var b=EarthPillarSpawn.bounds(t);assertEquals(x,t.x);assertEquals(y+6,t.y);assertEquals(z,t.z);assertEquals(x-1.5,b.minX);assertEquals(x+1.5,b.maxX);assertEquals(y+6,b.minY);assertEquals(y+9,b.maxY);assertEquals(z-1.5,b.minZ);assertEquals(z+1.5,b.maxZ);}}
}
