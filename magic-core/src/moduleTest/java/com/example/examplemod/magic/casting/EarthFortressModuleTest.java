package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_fortress.EarthFortressLayout;
class EarthFortressModuleTest {
	private List<EarthFortressLayout.Cell> old(double dx,double dz){var result=new ArrayList<EarthFortressLayout.Cell>();boolean alongZ=Math.abs(dz)>=Math.abs(dx);int sign=(alongZ?dz:dx)>=0?1:-1;for(int y=0;y<=4;y++)for(int x=-4;x<=4;x++)for(int z=-4;z<=4;z++){int radius=x*x+y*y+z*z;if(radius>16||radius<9)continue;boolean door=y<2&&(alongZ?x==0&&z*sign>=3:z==0&&x*sign>=3);if(!door)result.add(new EarthFortressLayout.Cell(x,y,z));}result.sort(Comparator.comparingInt(EarthFortressLayout.Cell::y).thenComparingDouble(EarthFortressLayout.Cell::squaredDistance));return List.copyOf(result);}
	@Test void frozenOriginalOrderMatchesAllQuadrantsAndRandomDirections(){var random=new Random(628);for(int i=0;i<1000;i++){double dx=random.nextDouble()*2-1,dz=random.nextDouble()*2-1;assertEquals(old(dx,dz),EarthFortressLayout.shell(dx,dz));}for(double dx:new double[]{-1,0,1})for(double dz:new double[]{-1,0,1})assertEquals(old(dx,dz),EarthFortressLayout.shell(dx,dz));}
	@Test void shellIsHollowUpperHemisphereAndNoDuplicateCells(){var cells=EarthFortressLayout.shell(0,1);assertEquals(cells.size(),new HashSet<>(cells).size());for(var cell:cells){assertTrue(cell.y()>=0&&cell.y()<=4);assertTrue(cell.squaredDistance()>=9&&cell.squaredDistance()<=16);}assertFalse(cells.contains(new EarthFortressLayout.Cell(0,0,0)));}
	@Test void doorwayIsTwoHighOnChosenReleaseSide(){for(int sign:new int[]{-1,1}){var cells=EarthFortressLayout.shell(0,sign);assertFalse(cells.contains(new EarthFortressLayout.Cell(0,0,3*sign)));assertFalse(cells.contains(new EarthFortressLayout.Cell(0,1,3*sign)));assertTrue(cells.contains(new EarthFortressLayout.Cell(0,2,3*sign)));assertTrue(cells.contains(new EarthFortressLayout.Cell(0,0,-3*sign)));}}
	@Test void stableLayerAndDistanceOrderCannotBeMutated(){var cells=EarthFortressLayout.shell(1,0);for(int i=1;i<cells.size();i++){var a=cells.get(i-1);var b=cells.get(i);assertTrue(a.y()<b.y()||a.y()==b.y()&&a.squaredDistance()<=b.squaredDistance());}assertThrows(UnsupportedOperationException.class,cells::clear);}
}
