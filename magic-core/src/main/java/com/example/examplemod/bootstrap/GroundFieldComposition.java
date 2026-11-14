package com.example.examplemod.bootstrap;
import java.util.Map;
import com.example.examplemod.magic.mechanic.field.GroundFieldPolicy;
public final class GroundFieldComposition {
	private static final Map<String,GroundFieldPolicy> POLICIES=Map.of(
		"flame_storm",new com.example.examplemod.magic.spell.fire.flame_storm.FlameStormPolicy(),
		"apocalypse_flame",new com.example.examplemod.magic.spell.fire.apocalypse_flame.ApocalypseFlamePolicy(),
		"earthquake",new com.example.examplemod.magic.spell.earth.earthquake.EarthquakePolicy(),
		"terra_fall",new com.example.examplemod.magic.spell.earth.terra_fall.TerraFallPolicy(),
		"continental_sink",new com.example.examplemod.magic.spell.earth.continental_sink.ContinentalSinkPolicy());
	public static GroundFieldPolicy find(String id){return POLICIES.get(id);}
	public static GroundFieldPolicy policy(String id){
		var policy=find(id);if(policy==null)throw new IllegalArgumentException("Not a ground field: "+id);return policy;
	}
	private GroundFieldComposition() {}
}
