package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.mechanic.projectile.LaunchBallistics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LaunchBallisticsModuleTest {
	@Test void solvedImpulseReachesRequestedHeightUnderVanillaDrag() {
		for(int i=1;i<=160;i++){double height=i*.1;assertEquals(height,LaunchBallistics.launchPeak(LaunchBallistics.verticalLaunch(height)),1E-7);}
	}
	@Test void descendingAndStationaryImpulsesCannotGainHeight() {
		assertEquals(0,LaunchBallistics.launchPeak(0));assertEquals(0,LaunchBallistics.launchPeak(-1));
	}
}
