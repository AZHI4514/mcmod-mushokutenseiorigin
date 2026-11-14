package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaHit;
class FrostNovaHitModuleTest {
	static class Target implements FrostNovaHit.Target {
		final List<String> events=new ArrayList<>();boolean wet,warm,damage=true,freezable=true,player;
		public boolean wet(){events.add("wet");return wet;}
		public boolean warm(){events.add("warm");return warm;}
		public boolean hurt(float d){events.add("hurt:"+d);return damage;}
		public void removeWet(){events.add("removeWet");wet=false;}
		public void removeWarm(){events.add("removeWarm");warm=false;}
		public boolean canFreeze(){events.add("canFreeze");return freezable;}
		public void freeze(int ticks){events.add("freeze:"+ticks);}
		public boolean player(){events.add("player");return player;}
		public void control(int ticks){events.add("control:"+ticks);}
		public void afterHit(){events.add("after");}
	}
	@Test void wetMobConsumesWetBeforeFreezeAndControl(){var t=new Target();t.wet=true;FrostNovaHit.apply(t);assertEquals(List.of("wet","warm","hurt:14.0","removeWet","canFreeze","freeze:300","player","control:40","after"),t.events);}
	@Test void warmthBlocksControlButNotDamageOrStatusConsumption(){var t=new Target();t.wet=t.warm=true;FrostNovaHit.apply(t);assertEquals(List.of("wet","warm","hurt:14.0","removeWet","removeWarm","after"),t.events);}
	@Test void rejectedDamageConsumesNothing(){var t=new Target();t.wet=t.warm=true;t.damage=false;FrostNovaHit.apply(t);assertTrue(t.wet&&t.warm);assertEquals(List.of("wet","warm","hurt:14.0"),t.events);}
	@Test void freezeImmunityDoesNotSkipPlayerControl(){var t=new Target();t.freezable=false;t.player=true;FrostNovaHit.apply(t);assertEquals(List.of("wet","warm","hurt:10.0","canFreeze","player","control:20","after"),t.events);}
	@Test void damageMutationDoesNotReplaceWarmSnapshot(){var t=new Target(){public boolean hurt(float d){warm=true;return super.hurt(d);}};FrostNovaHit.apply(t);assertTrue(t.events.contains("freeze:300"));assertFalse(t.events.contains("removeWarm"));}
	@Test void statusRemovalFailureStopsLaterControlAndHook(){var t=new Target(){public void removeWet(){super.removeWet();throw new IllegalStateException();}};t.wet=true;assertThrows(IllegalStateException.class,()->FrostNovaHit.apply(t));assertFalse(t.wet);assertEquals("removeWet",t.events.getLast());}
}
