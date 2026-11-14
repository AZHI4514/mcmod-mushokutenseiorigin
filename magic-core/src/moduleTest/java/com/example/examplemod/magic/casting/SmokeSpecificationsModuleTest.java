package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.spell.earth.sand_cloud.SandCloudPolicy;
import com.example.examplemod.magic.spell.earth.dust_storm.DustStormPolicy;
import com.example.examplemod.magic.spell.earth.sandstorm.SandstormPolicy;
import com.example.examplemod.magic.spell.mixed.deep_mist.DeepMistPolicy;
import com.example.examplemod.magic.spell.mixed.mud_rain.MudRainPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SmokeSpecificationsModuleTest {
	@Test void mudUsesExposureAndHoldWithoutFogOrDamage() {
		var mud=new MudRainPolicy();assertTrue(mud.requiresSky());assertNull(mud.sight());assertTrue(mud.slowing().held());assertFalse(mud.damaging());
	}
	@Test void launchedSandAndDeepMistShareRangeButNotLingerOrColor() {
		var sand=new SandCloudPolicy();var mist=new DeepMistPolicy();assertTrue(sand.launched());assertFalse(mist.launched());
		assertEquals(sand.sight().range(),mist.sight().range());assertNotEquals(sand.sight().linger(),mist.sight().linger());assertNotEquals(sand.sight().color(),mist.sight().color());
		assertNull(sand.slowing());assertNull(mist.slowing());
	}
	@Test void dustyVariantsHaveDistinctSeverityAndDamage() {
		var dust=new DustStormPolicy();var storm=new SandstormPolicy();assertFalse(dust.damaging());assertTrue(storm.damaging());
		assertTrue(dust.sight().range()>storm.sight().range());assertTrue(dust.slowing().amplifier()<storm.slowing().amplifier());assertTrue(dust.particleCount()<storm.particleCount());
	}
}
