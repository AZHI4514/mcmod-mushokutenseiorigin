package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactHit;
class TornadoImpactHitModuleTest {
	static class Target implements TornadoImpactHit.Target {
		final List<String> events=new ArrayList<>();boolean damage=true,immune;
		public boolean hurt(){events.add("hurt");return damage;}
		public boolean impulseImmune(){events.add("immune");return immune;}
		public void impulse(){events.add("impulse");}
		public void knockback(){events.add("knockback");}
		public void effect(){events.add("effect");}
		public void afterHit(){events.add("after");}
	}
	@Test void successfulDamagePrecedesImpulseEffectsAndHook(){var t=new Target();TornadoImpactHit.apply(t);assertEquals(List.of("hurt","immune","impulse","knockback","effect","after"),t.events);}
	@Test void rejectedDamageDoesNotReadTagOrApplyEffects(){var t=new Target();t.damage=false;TornadoImpactHit.apply(t);assertEquals(List.of("hurt"),t.events);}
	@Test void immuneTagOnlySuppressesDirectionalImpulse(){var t=new Target();t.immune=true;TornadoImpactHit.apply(t);assertEquals(List.of("hurt","immune","knockback","effect","after"),t.events);}
	@Test void impulseFailureStopsWithoutInventingLaterEffects(){var t=new Target(){public void impulse(){super.impulse();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->TornadoImpactHit.apply(t));assertEquals(List.of("hurt","immune","impulse"),t.events);}
	@Test void effectFailureDoesNotRunHook(){var t=new Target(){public void effect(){super.effect();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->TornadoImpactHit.apply(t));assertEquals("effect",t.events.getLast());}
}
