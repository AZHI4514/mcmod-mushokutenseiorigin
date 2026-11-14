package com.example.examplemod.magic.mechanic.projectile;
import java.util.Map;
/** Explicit shape specifications preserve each existing effect carrier's collision volume. */
public final class EffectShapeCatalog {
	private static final EffectShape DEFAULT=new EffectShape(false,.25,.25,.5);
	private static final Map<String,EffectShape> SHAPES=Map.ofEntries(
		Map.entry("ice_pillar",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("waterfall",new EffectShape(true,3,4,0.2)),
		Map.entry("ice_lance",new EffectShape(true,1,3,1)),
		Map.entry("icicle_break",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("icicle_edge",new EffectShape(false,3,1,1)),
		Map.entry("flood_flush",new EffectShape(false,3,1,1)),
		Map.entry("ice_fortress",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("explosion",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("flame_pillar",new EffectShape(true,2,4,2)),
		Map.entry("flamethrower",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("wind_slice",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("wind_bind",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("wind_blast",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("earth_lance",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("earth_pillar",new EffectShape(false,3,3,3)),
		Map.entry("ex_healing",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("antidote_ii",new EffectShape(false,0.25,0.25,0.5)),
		Map.entry("physical_shield",new EffectShape(true,3,3,0.2)));
	public static EffectShape of(String id){return SHAPES.getOrDefault(id,DEFAULT);}
	public static java.util.Set<String> ids(){return SHAPES.keySet();}
	private EffectShapeCatalog() {}
}
