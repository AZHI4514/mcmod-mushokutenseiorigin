package com.example.examplemod.magic.domain.visual;
import java.util.Map;
import java.util.function.DoubleFunction;
/** Pure authored visual extent, independent of world collision and renderer APIs. */
public record AreaMeshLayout(double y,float width,float height,float depth,float opacity) {
	private static final Map<String,DoubleFunction<AreaMeshLayout>> LAYOUTS=Map.ofEntries(
		Map.entry("flame_storm", time -> {float age=(float)time;String id="flame_storm";return new AreaMeshLayout(4,16,8,4,1);}),
		Map.entry("apocalypse_flame", time -> {float age=(float)time;String id="apocalypse_flame";return new AreaMeshLayout(12,96,24,96,age<60?.12F:1);}),
		Map.entry("earthquake", time -> {float age=(float)time;String id="earthquake";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.GroundFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.GroundFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("terra_fall", time -> {float age=(float)time;String id="terra_fall";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.GroundFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.GroundFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("continental_sink", time -> {float age=(float)time;String id="continental_sink";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.GroundFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.GroundFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("sand_cloud", time -> {float age=(float)time;String id="sand_cloud";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.65F);}),
		Map.entry("dust_storm", time -> {float age=(float)time;String id="dust_storm";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.65F);}),
		Map.entry("sandstorm", time -> {float age=(float)time;String id="sandstorm";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.65F);}),
		Map.entry("deep_mist", time -> {float age=(float)time;String id="deep_mist";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.65F);}),
		Map.entry("mud_rain", time -> {float age=(float)time;String id="mud_rain";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).radius(),h=(float)com.example.examplemod.bootstrap.SmokeFieldComposition.policy(id).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.65F);}),
		Map.entry("dual_shield", time -> {float age=(float)time;String id="dual_shield";return new AreaMeshLayout(0,4,4,.2F,.8F);}),
		Map.entry("spirit_sealing_barrier", time -> {float age=(float)time;String id="spirit_sealing_barrier";float r=id.equals("spirit_sealing_barrier")?8:3;return new AreaMeshLayout(0,r*2,r*2,r*2,.85F);}),
		Map.entry("confinement_barrier", time -> {float age=(float)time;String id="confinement_barrier";float r=id.equals("spirit_sealing_barrier")?8:3;return new AreaMeshLayout(0,r*2,r*2,r*2,.85F);}),
		Map.entry("demon_god_sealing_barrier", time -> {float age=(float)time;String id="demon_god_sealing_barrier";float r=id.equals("spirit_sealing_barrier")?8:3;return new AreaMeshLayout(0,r*2,r*2,r*2,.85F);}),
		Map.entry("fortress_barrier", time -> {float age=(float)time;String id="fortress_barrier";return new AreaMeshLayout(12,48,24,48,.75F);}),
		Map.entry("gravity_magic", time -> {float age=(float)time;String id="gravity_magic";return new AreaMeshLayout(6,12,12,12,.7F);}),
		Map.entry("atmosphere_dominion", time -> {float age=(float)time;String id="atmosphere_dominion";return new AreaMeshLayout(32,96,64,96,.35F);}),
		Map.entry("nuclear_explosion", time -> {float age=(float)time;String id="nuclear_explosion";float size=age<100?2:Math.min(64,(age-100)*5);return new AreaMeshLayout(0,size,size,size,.85F);}),
		Map.entry("squall", time -> {float age=(float)time;String id="squall";return new AreaMeshLayout(16,32,32,32,.8F);}),
		Map.entry("ocean_freeze", time -> {float age=(float)time;String id="ocean_freeze";return new AreaMeshLayout(0,128,2,128,.85F);}),
		Map.entry("sacred_nova", time -> {float age=(float)time;String id="sacred_nova";float size=id.equals("god_purge")?96:32;return new AreaMeshLayout(0,size,2,size,.8F);}),
		Map.entry("god_purge", time -> {float age=(float)time;String id="god_purge";float size=id.equals("god_purge")?96:32;return new AreaMeshLayout(0,size,2,size,.8F);}),
		Map.entry("divine_judge", time -> {float age=(float)time;String id="divine_judge";return new AreaMeshLayout(16,16,32,16,.85F);}),
		Map.entry("absolute_zero", time -> {float age=(float)time;String id="absolute_zero";return new AreaMeshLayout(0,32,32,32,1);}),
		Map.entry("inferno", time -> {float age=(float)time;String id="inferno";return new AreaMeshLayout(4,40,8,40,1);}),
		Map.entry("flash_over", time -> {float age=(float)time;String id="flash_over";return new AreaMeshLayout(3,32,6,32,1);}),
		Map.entry("magma_gush", time -> {float age=(float)time;String id="magma_gush";return new AreaMeshLayout(2.5,8,5,8,1);}),
		Map.entry("flame_tornado", time -> {float age=(float)time;String id="flame_tornado";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).radius(),h=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("violent_storm", time -> {float age=(float)time;String id="violent_storm";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).radius(),h=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("tornado", time -> {float age=(float)time;String id="tornado";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).radius(),h=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("vacuum_storm", time -> {float age=(float)time;String id="vacuum_storm";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).radius(),h=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("sky_tempest", time -> {float age=(float)time;String id="sky_tempest";var spell=com.example.examplemod.magic.SpellDefinition.find(id);float r=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).radius(),h=(float)com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).height();return new AreaMeshLayout(h/2,r*2,h,r*2,.8F);}),
		Map.entry("quagmire", time -> {float age=(float)time;String id="quagmire";return new AreaMeshLayout(0,12,.35F,12,1);}),
		Map.entry("cumulonimbus", time -> {float age=(float)time;String id="cumulonimbus";return new AreaMeshLayout(24,48,48,48,.7F);}),
		Map.entry("blizzard_storm", time -> {float age=(float)time;String id="blizzard_storm";return new AreaMeshLayout(1,7.5F,7.5F,7.5F,.85F);}),
		Map.entry("water_shield", time -> {float age=(float)time;String id="water_shield";return new AreaMeshLayout(0,3, 2, 1, 1);}),
		Map.entry("magic_shield", time -> {float age=(float)time;String id="magic_shield";return new AreaMeshLayout(1,1.8F,2.1F,1.8F,1);}),
		Map.entry("burning_place", time -> {float age=(float)time;String id="burning_place";return new AreaMeshLayout(.9,1F, 1.9F, 1F, 1);}),
		Map.entry("heat_island", time -> {float age=(float)time;String id="heat_island";return new AreaMeshLayout(.08,5.8F, .7F, 5.8F, .6F);}));
	public static AreaMeshLayout at(String id,float age){var layout=LAYOUTS.get(id);return layout==null?null:layout.apply(age);}
}
