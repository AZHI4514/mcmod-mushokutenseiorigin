package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.feature.input.WheelGeometry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class WheelGeometryModuleTest {
	@Test void sectorsMatchDisplayedLabelsForAllLoadoutSizes() {
		for(int count=1;count<=10;count++)for(int i=0;i<count;i++){
			double a=-Math.PI/2+i*Math.PI*2/count;int r=WheelGeometry.radius(1920,1080);
			assertEquals(i,WheelGeometry.hitSlot(1920,1080,count,960+Math.cos(a)*r*.62,540+Math.sin(a)*r*.62));
		}
	}
	@Test void centerOutsideEmptyAndTinyViewportHaveStableBoundaries() {
		assertEquals(-1,WheelGeometry.hitSlot(1920,1080,10,960,540));
		assertEquals(-1,WheelGeometry.hitSlot(1920,1080,10,0,0));
		assertEquals(-1,WheelGeometry.hitSlot(1920,1080,0,960,440));assertEquals(1,WheelGeometry.radius(1,1));
		assertEquals(0,WheelGeometry.hitSlot(1920,1080,10,960,530));
	}
}
