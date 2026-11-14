package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.visual.ProjectileMeshSpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ProjectileMeshSpecModuleTest {
	@Test void elongatedVisualsKeepDimensionsAndDefaultOffsets(){
		assertEquals(new ProjectileMeshSpec(.14F,.65F,.95F,0),ProjectileMeshSpec.basic("ice_blade"));assertEquals(new ProjectileMeshSpec(.16F,.16F,.65F,0),ProjectileMeshSpec.basic("water_arrow"));
		assertEquals(new ProjectileMeshSpec(.25F,.25F,.25F,.125F),ProjectileMeshSpec.basic("unknown"));assertEquals(new ProjectileMeshSpec(.45F,.45F,1.4F,0),ProjectileMeshSpec.swept("unknown"));
		assertEquals(new ProjectileMeshSpec(3,1,.25F,0),ProjectileMeshSpec.swept("sonic_boom"));assertEquals(new ProjectileMeshSpec(.38F,.38F,.55F,0),ProjectileMeshSpec.swept("stone_cannon"));
	}
	@Test void onlyStationaryNonShieldAndFallingRockSuppressPitch(){
		for(boolean stationary:new boolean[]{false,true})for(boolean shield:new boolean[]{false,true})for(boolean falling:new boolean[]{false,true}){
			assertEquals(falling||stationary&&!shield?0:(float)Math.toDegrees(Math.atan2(.5,.75)),ProjectileMeshSpec.carrierPitch(falling,stationary,shield,.5,.75));
			assertEquals(stationary&&!shield||falling?1.5F:0,ProjectileMeshSpec.carrierOffset(stationary,shield,falling,3));
		}
	}
}
