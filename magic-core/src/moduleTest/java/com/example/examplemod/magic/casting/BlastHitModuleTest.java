package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.mixed.blast.BlastHit;
class BlastHitModuleTest {
	static class Target implements BlastHit.Target {
		final List<String> events=new ArrayList<>();boolean accepted=true,immune,useAim;double strength;
		public boolean hurt(){events.add("hurt");return accepted;}
		public boolean impulseImmune(){events.add("immune");return immune;}
		public void impulse(double s,boolean a){events.add("impulse");strength=s;useAim=a;}
		public void afterHit(){events.add("after");}
	}
	@Test void acceptedHitAppliesImpulseBeforeHook(){var t=new Target();BlastHit.apply(t,1);assertEquals(List.of("hurt","immune","impulse","after"),t.events);assertEquals(.6,t.strength);assertFalse(t.useAim);}
	@Test void damageRejectionSkipsImmunityImpulseAndHook(){var t=new Target();t.accepted=false;BlastHit.apply(t,0);assertEquals(List.of("hurt"),t.events);}
	@Test void immunitySuppressesOnlyImpulse(){var t=new Target();t.immune=true;BlastHit.apply(t,0);assertEquals(List.of("hurt","immune","after"),t.events);}
	@Test void impulseNearOriginUsesStrictSquaredThreshold(){for(double sq:new double[]{0,Math.nextDown(1E-6),1E-6,Double.NaN}){var t=new Target();BlastHit.apply(t,sq);assertEquals(sq<1E-6,t.useAim);}}
	@Test void partialImpulseExceptionStopsBeforeHook(){var t=new Target(){public void impulse(double s,boolean a){super.impulse(s,a);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->BlastHit.apply(t,0));assertEquals(List.of("hurt","immune","impulse"),t.events);assertEquals(.6,t.strength);}
	@Test void damageAndImmunityExceptionsDoNotInventLaterEffects(){var a=new Target(){public boolean hurt(){super.hurt();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->BlastHit.apply(a,1));assertEquals(List.of("hurt"),a.events);var b=new Target(){public boolean impulseImmune(){super.impulseImmune();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->BlastHit.apply(b,1));assertEquals(List.of("hurt","immune"),b.events);}
}
