package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.water_shield.WaterShieldSelection;
class WaterShieldSelectionModuleTest {
	@Test void nearestWithStableTie(){var c=WaterShieldSelection.nearest(List.of("a","b","c"),s->true,s->s.equals("a")?2:1);assertEquals("b",c.shield());assertEquals(1,c.distance());}
	@Test void rejectedShieldNeverReadsContact(){var events=new ArrayList<String>();var c=WaterShieldSelection.nearest(List.of("a","b"),s->{events.add("eligible"+s);return s.equals("b");},s->{events.add("contact"+s);return 1;});assertEquals("b",c.shield());assertEquals(List.of("eligiblea","eligibleb","contactb"),events);}
	@Test void nonFiniteMatchesStrictLegacyComparison(){assertNull(WaterShieldSelection.nearest(List.of(1,2),s->true,s->s==1?Double.NaN:Double.POSITIVE_INFINITY));assertEquals(2,WaterShieldSelection.nearest(List.of(1,2),s->true,s->s==1?0:Double.NEGATIVE_INFINITY).shield());}
	@Test void emptyHasNoContact(){assertNull(WaterShieldSelection.nearest(List.of(),s->{fail();return true;},s->{fail();return 0;}));}
}
