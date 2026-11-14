package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.water.water_shield.*;
class WaterShieldModuleTest {
	static class Target implements WaterShieldContact.Target {
		final List<String> events=new ArrayList<>();boolean claimed;
		public void drag(){events.add("drag");}
		public void markMotion(){events.add("mark");}
		public boolean alreadyContacted(){events.add("check");return claimed;}
		public void claim(){events.add("claim");claimed=true;}
		public void wet(){events.add("wet");}
	}
	static class Shield implements WaterShieldAbsorb.Shield {
		float value=8;final List<String> events=new ArrayList<>();
		public float capacity(){return value;}
		public void capacity(float v){events.add("capacity");value=v;}
		public void show(){events.add("show");}
		public void discard(){events.add("discard");}
	}
	@Test void firstContactDragsThenClaimsBeforeWet(){var t=new Target();WaterShieldContact.apply(t);assertEquals(List.of("drag","mark","check","claim","wet"),t.events);}
	@Test void repeatedContactStillDragsButDoesNotWet(){var t=new Target();t.claimed=true;WaterShieldContact.apply(t);assertEquals(List.of("drag","mark","check"),t.events);}
	@Test void wetFailureKeepsClaim(){var t=new Target(){public void wet(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WaterShieldContact.apply(t));assertTrue(t.claimed);}
	@Test void absorbShowsRemainingCapacity(){var s=new Shield();WaterShieldAbsorb.apply(3,s);assertEquals(5,s.value);assertEquals(List.of("capacity","show"),s.events);}
	@Test void depletedShieldShowsThenDiscards(){var s=new Shield();WaterShieldAbsorb.apply(20,s);assertEquals(0,s.value);assertEquals(List.of("capacity","show","discard"),s.events);}
	@Test void presentationFailureDoesNotUndoCapacityOrDiscard(){var s=new Shield(){public void show(){super.show();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->WaterShieldAbsorb.apply(10,s));assertEquals(0,s.value);assertEquals(List.of("capacity","show"),s.events);}
	@Test void renewBoundaryAndSaturation(){assertTrue(WaterShieldRules.canRenew(true,Math.nextDown(1E-6)));assertFalse(WaterShieldRules.canRenew(true,1E-6));assertFalse(WaterShieldRules.canRenew(false,0));assertEquals(160,WaterShieldRules.extend(80));assertEquals(Integer.MAX_VALUE,WaterShieldRules.extend(Integer.MAX_VALUE));}
	@Test void ieeeDamageIsNotSilentlyFiltered(){var s=new Shield();WaterShieldAbsorb.apply(Float.NaN,s);assertTrue(Float.isNaN(s.value));assertEquals(List.of("capacity","show"),s.events);}
}
