package com.example.examplemod.bootstrap;
import java.util.Map;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.infrastructure.fabric.magic.*;
/** Immutable side-effect strategies; no per-world state. */
public final class GroundContactComposition {
	private static final Map<SpellDefinition,GroundContactEffect> EFFECTS=Map.of(
		SpellDefinition.FLAME_STORM,new IgnitingGroundContact(500),
		SpellDefinition.APOCALYPSE_FLAME,new IgnitingGroundContact(700),
		SpellDefinition.EARTHQUAKE,new EarthquakeGroundContact(),
		SpellDefinition.CONTINENTAL_SINK,new ContinentalSinkGroundContact(),
		SpellDefinition.TERRA_FALL,new TerraFallGroundContact());
	public static GroundContactEffect effect(SpellDefinition spell){
		var effect=EFFECTS.get(spell);if(effect==null)throw new IllegalArgumentException("No ground contact: "+spell);return effect;
	}
	private GroundContactComposition() {}
}
