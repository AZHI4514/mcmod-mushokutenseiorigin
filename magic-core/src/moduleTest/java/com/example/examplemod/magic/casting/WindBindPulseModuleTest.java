package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.wind_bind.WindBindSpec;
class WindBindPulseModuleTest {
	@Test void fivePulsesOnlyAfterInitialTwentyTicksAndAtLastRemainingTick(){
		var pulses=new ArrayList<Integer>();
		for(int remaining=100;remaining>=0;remaining--)if(WindBindSpec.pulse(remaining))pulses.add(remaining);
		assertEquals(List.of(80,60,40,20,1),pulses);assertEquals(1F,WindBindSpec.PULSE_DAMAGE);
	}
	@Test void longOrExpiredEffectsDoNotInventAdditionalPulses(){
		for(int remaining:new int[]{Integer.MIN_VALUE,-20,-1,0,81,99,100,120,Integer.MAX_VALUE})assertFalse(WindBindSpec.pulse(remaining));
	}
	@Test void frozenLegacyPredicateMatchesEveryRemainingValueAroundDuration(){
		for(int r=-1000;r<=1000;r++)assertEquals(r==1||r<=80&&r>0&&r%20==0,WindBindSpec.pulse(r));
	}
}
