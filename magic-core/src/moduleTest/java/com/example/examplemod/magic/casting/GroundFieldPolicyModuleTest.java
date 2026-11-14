package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.flame_storm.FlameStormPolicy;
import com.example.examplemod.magic.spell.fire.apocalypse_flame.ApocalypseFlamePolicy;
import com.example.examplemod.magic.spell.earth.earthquake.EarthquakePolicy;
import com.example.examplemod.magic.spell.earth.terra_fall.TerraFallPolicy;
import com.example.examplemod.magic.spell.earth.continental_sink.ContinentalSinkPolicy;
class GroundFieldPolicyModuleTest {
	@Test void flameWallHasTwelvePulses(){var p=new FlameStormPolicy();for(long a=-100;a<500;a++)assertEquals(a>=0&&a<240?(int)(a/20):-1,p.damagePulse(a));assertEquals(9,p.radius());assertEquals(8,p.height());}
	@Test void apocalypseRetainsGapAfterOpening(){var p=new ApocalypseFlamePolicy();assertEquals(0,p.damagePulse(60));for(long a=61;a<80;a++)assertEquals(-1,p.damagePulse(a));assertEquals(1,p.damagePulse(80));assertEquals(15,p.damagePulse(360));assertEquals(-1,p.damagePulse(361));assertTrue(p.warning(59));assertFalse(p.warning(60));}
	@Test void terraFallPulsesAndWarningAreIndependent(){var p=new TerraFallPolicy();assertEquals(-1,p.damagePulse(19));assertEquals(0,p.damagePulse(20));assertEquals(0,p.damagePulse(59));assertEquals(1,p.damagePulse(60));assertEquals(2,p.damagePulse(100));assertEquals(-1,p.damagePulse(120));assertTrue(p.warning(50));assertFalse(p.warning(60));}
	@Test void groundShockOnlyDamagesAtExactZero(){var q=new EarthquakePolicy();var s=new ContinentalSinkPolicy();for(long a=-100;a<500;a++){assertEquals(a==0?0:-1,q.damagePulse(a));assertEquals(a==0?0:-1,s.damagePulse(a));}assertEquals(16,q.radius());assertEquals(48,s.radius());assertEquals(4,s.height());}
}
