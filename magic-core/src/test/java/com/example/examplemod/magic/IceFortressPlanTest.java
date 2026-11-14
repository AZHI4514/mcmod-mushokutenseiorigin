package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.ice_fortress.IceFortressSpec;
class IceFortressPlanTest {
	@Test void wallKeepsFifteenCellsBottomRowFirstAndAxisTieRule() {
		var cells=IceFortressSpec.plan(-3,64,7,1,1);assertEquals(15,cells.size());assertEquals(new IceFortressSpec.Cell(-5,64,7),cells.getFirst());assertEquals(new IceFortressSpec.Cell(-1,66,7),cells.getLast());
		assertEquals(15,cells.stream().distinct().count());assertThrows(UnsupportedOperationException.class,()->cells.clear());
	}
	@Test void xDominantAimMakesZSpanningWallIncludingNegativeCoordinates() {
		var cells=IceFortressSpec.plan(-3,64,-7,-1,.1);assertEquals(new IceFortressSpec.Cell(-3,64,-9),cells.getFirst());assertEquals(new IceFortressSpec.Cell(-3,66,-5),cells.getLast());
	}
}
