package com.example.examplemod.client.magic;
import com.example.examplemod.magic.domain.visual.motion.MeshMotion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/** Loads immutable authored mesh resources; contains no render submission or animation policy. */
final class ReferenceMeshRepository {
	private record SourcePart(String name, float[][][] quads) { }
	private record Source(List<SourcePart> parts) { }
	record Part(String role, int index, float[][][] quads, float cx, float cy, float cz) { }
	record Asset(List<Part> parts, Identifier texture) { }
	private static final Map<String, Asset> ASSETS = load();
	private static Map<String, Asset> load() {
		var assets = new HashMap<String, Asset>();
		for (String id : List.of("water_ball", "fireball", "wind_ball", "rock_bullet", "water_arrow", "ice_smash", "flame_slice", "healing", "water_shield", "ice_blade", "heat_hand", "heat_island", "burning_place", "detoxification", "magic_shield", "water_cannon", "water_splash", "icicle_field", "ex_flame", "sonic_boom", "stone_cannon", "ice_pillar", "waterfall", "ice_lance", "icicle_break", "icicle_edge", "flood_flush", "ice_fortress", "explosion", "flame_pillar", "flamethrower", "wind_slice", "wind_bind", "wind_blast", "earth_lance", "earth_pillar", "ex_healing", "antidote_ii", "physical_shield", "blizzard_storm", "exodus_flame", "tornado_impact", "cumulonimbus", "lightning", "electric", "frost_nova", "sonic_blast", "earth_hedgehog", "quagmire", "blast", "bora_blast", "magma_gush", "violent_storm", "tornado", "vacuum_storm", "sky_tempest", "flash_over", "flame_tornado", "inferno", "absolute_zero")) {
			String path = "/assets/mushokutenseiorigin/models/effect/reference/" + id + ".json";
			try (var stream = ReferenceMeshRepository.class.getResourceAsStream(path)) {
				if (stream == null) throw new IllegalStateException("Missing reference mesh " + path);
				var source = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), Source.class);
				var parts = new ArrayList<Part>();
				for (var p : source.parts) {
					int split = p.name.lastIndexOf('_');
					float cx = 0, cy = 0, cz = 0; int count = 0;
					for (var q : p.quads) for (var v : q) { cx += v[0]; cy += v[1]; cz += v[2]; count++; }
					parts.add(new Part(p.name.substring(0, split), Integer.parseInt(p.name.substring(split + 1)), p.quads, cx / count, cy / count, cz / count));
				}
				assets.put(id, new Asset(List.copyOf(parts), ExampleMod.id("textures/effect/reference/" + id + ".png")));
			} catch (Exception error) { throw new IllegalStateException("Cannot load reference VFX " + id, error); }
		}
		return Map.copyOf(assets);
	}
	static boolean handles(String id){return ASSETS.containsKey(id);}
	static Asset get(String id){return ASSETS.get(id);}
	private ReferenceMeshRepository(){}
}
