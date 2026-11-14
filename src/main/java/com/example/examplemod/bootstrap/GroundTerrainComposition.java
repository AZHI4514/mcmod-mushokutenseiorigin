package com.example.examplemod.bootstrap;
import java.util.Map;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.infrastructure.fabric.magic.*;
/** Explicit terrain choices. Flame wall intentionally does not alter terrain. */
public final class GroundTerrainComposition {
	private static final Map<SpellDefinition,GroundTerrainEffect> EFFECTS=Map.of(
		SpellDefinition.FLAME_STORM,(owner,area,pulse)->{},
		SpellDefinition.APOCALYPSE_FLAME,new ExcavatingGroundTerrain(1,8192),
		SpellDefinition.EARTHQUAKE,new ExcavatingGroundTerrain(2,512),
		SpellDefinition.CONTINENTAL_SINK,new ExcavatingGroundTerrain(4,8192),
		SpellDefinition.TERRA_FALL,new TerraFallGroundTerrain());
	public static GroundTerrainEffect effect(SpellDefinition spell){
		var effect=EFFECTS.get(spell);if(effect==null)throw new IllegalArgumentException("No ground terrain: "+spell);return effect;
	}
	private GroundTerrainComposition() {}
}
