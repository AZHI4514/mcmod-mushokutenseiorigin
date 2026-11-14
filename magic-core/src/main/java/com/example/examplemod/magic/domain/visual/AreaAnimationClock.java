package com.example.examplemod.magic.domain.visual;
import com.example.examplemod.magic.SpellDefinition;
import java.util.Set;
/** Explicit clock source for authored fields; entity age remains the default. */
public final class AreaAnimationClock {
	private static final Set<String> WORLD_START=Set.of("earth_hedgehog","magma_gush","absolute_zero","inferno","flash_over","sacred_nova","god_purge");
	public static boolean usesWorldStart(SpellDefinition spell){return WORLD_START.contains(spell.id())||switch(spell.execution()){
		case WIND_FIELD,EXPANSION_FIELD,MODE_FIELD,SMOKE_FIELD -> true;
		default -> false;
	};}
	private AreaAnimationClock(){}
}
