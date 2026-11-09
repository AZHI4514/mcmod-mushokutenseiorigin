package com.example.examplemod.magic;

import java.util.LinkedHashMap;
import java.util.Map;
import com.example.examplemod.ExampleMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;

/** Own sprite resources for every spell; these are not vanilla particle aliases. */
public final class SpellParticles {
	private static final Map<String,SimpleParticleType> TYPES=new LinkedHashMap<>();
	public static final SimpleParticleType CHANT=register("chant_glyph");
	private static SimpleParticleType register(String id) {
		var type=Registry.register(BuiltInRegistries.PARTICLE_TYPE,ExampleMod.id(id),FabricParticleTypes.simple());
		TYPES.put(id,type);return type;
	}
	public static void initialize() {
		for(var spell:SpellDefinition.ALL) for(var role:new String[]{"gather","trail","burst","haze","accent"}) register(spell.id()+"_"+role);
	}
	public static Map<String,SimpleParticleType> types() { return java.util.Collections.unmodifiableMap(TYPES); }
	public static SimpleParticleType gather(SpellDefinition spell) { return TYPES.get(spell.id()+"_gather"); }
	public static SimpleParticleType trail(SpellDefinition spell) { return TYPES.get(spell.id()+"_trail"); }
	public static SimpleParticleType burst(SpellDefinition spell) { return TYPES.get(spell.id()+"_burst"); }
	public static SimpleParticleType haze(SpellDefinition spell) { return TYPES.get(spell.id()+"_haze"); }
	public static SimpleParticleType accent(SpellDefinition spell) { return TYPES.get(spell.id()+"_accent"); }
	private SpellParticles() { }
}
