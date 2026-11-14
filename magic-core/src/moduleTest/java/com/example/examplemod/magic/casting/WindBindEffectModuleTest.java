package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.wind.wind_bind.*;
class WindBindEffectModuleTest {
	@Test void successAppliesBeforeVisualWithoutImmediateDamage(){
		var events=new ArrayList<String>();assertTrue(WindBindEffect.apply(new WindBindEffect.Target(){
			public boolean apply(int ticks){events.add("apply:"+ticks);return true;}
			public void showBound(){events.add("visual");}
		}));assertEquals(List.of("apply:100","visual"),events);
		var pulses=new ArrayList<Integer>();for(int ticks=100;ticks>0;ticks--)if(WindBindSpec.pulse(ticks))pulses.add(ticks);
		assertEquals(List.of(80,60,40,20,1),pulses);
	}
	@Test void rejectedEffectDoesNotShowVisual(){
		assertFalse(WindBindEffect.apply(new WindBindEffect.Target(){public boolean apply(int ticks){return false;}public void showBound(){fail();}}));
	}
	@Test void effectExceptionDoesNotPublishVisualAndVisualExceptionDoesNotUndoEffect(){
		assertThrows(IllegalStateException.class,()->WindBindEffect.apply(new WindBindEffect.Target(){public boolean apply(int ticks){throw new IllegalStateException();}public void showBound(){fail();}}));
		var events=new ArrayList<String>();assertThrows(IllegalStateException.class,()->WindBindEffect.apply(new WindBindEffect.Target(){public boolean apply(int ticks){events.add("applied");return true;}public void showBound(){throw new IllegalStateException();}}));assertEquals(List.of("applied"),events);
	}
}
