package com.example.examplemod.bootstrap;
import java.util.Map;
import com.example.examplemod.magic.mechanic.field.SmokeFieldPolicy;
public final class SmokeFieldComposition {
	private static final Map<String,SmokeFieldPolicy> POLICIES=Map.of(
		"sand_cloud",new com.example.examplemod.magic.spell.earth.sand_cloud.SandCloudPolicy(),
		"dust_storm",new com.example.examplemod.magic.spell.earth.dust_storm.DustStormPolicy(),
		"sandstorm",new com.example.examplemod.magic.spell.earth.sandstorm.SandstormPolicy(),
		"deep_mist",new com.example.examplemod.magic.spell.mixed.deep_mist.DeepMistPolicy(),
		"mud_rain",new com.example.examplemod.magic.spell.mixed.mud_rain.MudRainPolicy());
	public static SmokeFieldPolicy policy(String id){
		var policy=POLICIES.get(id);if(policy==null)throw new IllegalArgumentException("Not a smoke field: "+id);return policy;
	}
	private SmokeFieldComposition() {}
}
