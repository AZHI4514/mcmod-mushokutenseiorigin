package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_wall.EarthWallLayout;
class EarthWallModuleTest {
	@Test void hasExactlyFifteenUniqueCellsInLayerOrder(){var cells=EarthWallLayout.offsets(0,1);assertEquals(15,cells.size());assertEquals(15,new HashSet<>(cells).size());for(int i=0;i<15;i++){assertEquals(i/5,cells.get(i).y());assertEquals(i%5-2,cells.get(i).x());assertEquals(0,cells.get(i).z());}}
	@Test void tieChoosesXAxisAndPerpendicularWallUsesZAxis(){assertEquals(new EarthWallLayout.Cell(-2,0,0),EarthWallLayout.offsets(1,1).getFirst());assertEquals(new EarthWallLayout.Cell(0,0,-2),EarthWallLayout.offsets(1,0).getFirst());}
	@Test void oldOrderedLayoutMatchesRandomDirections(){var random=new Random(250);for(int sample=0;sample<10000;sample++){double dx=random.nextDouble()*2-1,dz=random.nextDouble()*2-1;var expected=new ArrayList<EarthWallLayout.Cell>();boolean alongX=Math.abs(dz)>=Math.abs(dx);for(int y=0;y<3;y++)for(int x=-2;x<=2;x++)expected.add(new EarthWallLayout.Cell(alongX?x:0,y,alongX?0:x));assertEquals(expected,EarthWallLayout.offsets(dx,dz));}}
	@Test void returnedPlanCannotBeMutated(){assertThrows(UnsupportedOperationException.class,()->EarthWallLayout.offsets(0,1).clear());}
}
