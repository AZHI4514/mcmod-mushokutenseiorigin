package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.sonic_blast.SonicBlastHit;
class SonicBlastHitModuleTest {
	static class Target implements SonicBlastHit.Target {
		final List<String> events=new ArrayList<>();boolean accepted=true,immune,useAim;double strength;
		public boolean hurt(){events.add("hurt");return accepted;}
		public boolean impulseImmune(){events.add("immune");return immune;}
		public void impulse(double s,boolean a){events.add("impulse");strength=s;useAim=a;}
		public void afterHit(){events.add("after");}
	}
	@Test void acceptedHitAppliesImpulseBeforeHook(){var t=new Target();SonicBlastHit.apply(t,1);assertEquals(List.of("hurt","immune","impulse","after"),t.events);assertEquals(1.2,t.strength);assertFalse(t.useAim);}
	@Test void damageRejectionSkipsImmunityImpulseAndHook(){var t=new Target();t.accepted=false;SonicBlastHit.apply(t,0);assertEquals(List.of("hurt"),t.events);}
	@Test void immunitySuppressesOnlyImpulse(){var t=new Target();t.immune=true;SonicBlastHit.apply(t,0);assertEquals(List.of("hurt","immune","after"),t.events);}
	@Test void impulseNearOriginUsesStrictSquaredThreshold(){for(double sq:new double[]{0,Math.nextDown(.001),.001,Double.NaN}){var t=new Target();SonicBlastHit.apply(t,sq);assertEquals(sq<.001,t.useAim);}}
	@Test void partialImpulseExceptionStopsBeforeHook(){var t=new Target(){public void impulse(double s,boolean a){super.impulse(s,a);throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->SonicBlastHit.apply(t,0));assertEquals(List.of("hurt","immune","impulse"),t.events);assertEquals(1.2,t.strength);}
	@Test void damageAndImmunityExceptionsDoNotInventLaterEffects(){var a=new Target(){public boolean hurt(){super.hurt();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->SonicBlastHit.apply(a,1));assertEquals(List.of("hurt"),a.events);var b=new Target(){public boolean impulseImmune(){super.impulseImmune();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->SonicBlastHit.apply(b,1));assertEquals(List.of("hurt","immune"),b.events);}
}
