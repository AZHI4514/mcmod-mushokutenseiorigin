package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.healing.ex_healing.ExHealingEffect;
class ExHealingEffectModuleTest {
	@Test void readsCurrentHealthThenMaximumBeforeHealing(){var events=new ArrayList<String>();assertTrue(ExHealingEffect.apply(new ExHealingEffect.Patient(){public float health(){events.add("health");return 17;}public float maximum(){events.add("maximum");return 20;}public void heal(float amount){assertEquals(3,amount);events.add("heal");}}));assertEquals(List.of("health","maximum","heal"),events);}
	@Test void fullHealthCompatibilityStillCallsHealZeroAndSucceeds(){assertTrue(ExHealingEffect.apply(new ExHealingEffect.Patient(){public float health(){return 20;}public float maximum(){return 20;}public void heal(float amount){assertEquals(0,amount);}}));}
	@Test void healingExceptionPropagatesWithoutRetry(){int[] calls={0};assertThrows(IllegalStateException.class,()->ExHealingEffect.apply(new ExHealingEffect.Patient(){public float health(){return 1;}public float maximum(){return 20;}public void heal(float amount){calls[0]++;throw new IllegalStateException();}}));assertEquals(1,calls[0]);}
}
