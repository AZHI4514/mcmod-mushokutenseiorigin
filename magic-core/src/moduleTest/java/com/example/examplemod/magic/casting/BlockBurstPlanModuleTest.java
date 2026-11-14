package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.mechanic.projectile.BlockBurstPlan;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class BlockBurstPlanModuleTest {
	@Test void impactSideSelectsEightOrderedNeighborsWithoutDuplicates() {
		for(int mask=0;mask<8;mask++){
			double x=(mask&1)==0?.1:.9,y=(mask&2)==0?.1:.9,z=(mask&4)==0?.1:.9;
			var cells=BlockBurstPlan.cells(0,0,0,2,x,y,z);assertEquals(8,cells.size());assertEquals(8,cells.stream().distinct().count());
			assertEquals(new BlockBurstPlan.Cell(x<.5?-1:0,y<.5?-1:0,z<.5?-1:0),cells.getFirst());
		}
	}
	@Test void singleCellAndNonpositiveWidthsPreserveOriginalSelection() {
		assertEquals(java.util.List.of(new BlockBurstPlan.Cell(3,4,5)),BlockBurstPlan.cells(3,4,5,1,Double.NaN,Double.NaN,Double.NaN));
		assertTrue(BlockBurstPlan.cells(3,4,5,0,0,0,0).isEmpty());assertTrue(BlockBurstPlan.cells(3,4,5,-1,0,0,0).isEmpty());
	}
}
