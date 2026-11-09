package com.example.examplemod.client.magic;

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

/** All 39 reference-led spell meshes. All motion is client-only and independent of the damage volume. */
final class ReferenceSpellModels {
	static final int FORM = 0, FLIGHT = 1, IMPACT = 2;
	private record SourcePart(String name, float[][][] quads) { }
	private record Source(List<SourcePart> parts) { }
	private record Part(String role, int index, float[][][] quads, float cx, float cy, float cz) { }
	private record Asset(List<Part> parts, Identifier texture) { }
	private static final Map<String, Asset> ASSETS = load();
	private static Map<String, Asset> load() {
		var assets = new HashMap<String, Asset>();
		for (String id : List.of("water_ball", "fireball", "wind_ball", "rock_bullet", "water_arrow", "ice_smash", "flame_slice", "healing", "water_shield", "ice_blade", "heat_hand", "heat_island", "burning_place", "detoxification", "magic_shield", "water_cannon", "water_splash", "icicle_field", "ex_flame", "sonic_boom", "stone_cannon", "ice_pillar", "waterfall", "ice_lance", "icicle_break", "icicle_edge", "flood_flush", "ice_fortress", "explosion", "flame_pillar", "flamethrower", "wind_slice", "wind_bind", "wind_blast", "earth_lance", "earth_pillar", "ex_healing", "antidote_ii", "physical_shield")) {
			String path = "/assets/mushokutenseiorigin/models/effect/reference/" + id + ".json";
			try (var stream = ReferenceSpellModels.class.getResourceAsStream(path)) {
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
	static boolean handles(String id) { return ASSETS.containsKey(id); }
	static void submit(PoseStack poses, SubmitNodeCollector collector, String id, float age, float width, float height, float depth, float opacity, int stage) {
		var asset = ASSETS.get(id);
		if (asset == null || opacity <= .005F || width <= 0 || height <= 0 || depth <= 0) return;
		if ((id.equals("water_shield") || ExtendedSpellMotion.persistent(id)) && stage == IMPACT) return;
		poses.pushPose();
		poses.scale(width, height, depth);
		// Build immutable deformed geometry before deferred rendering; no live entity is captured.
		var geometry = new ArrayList<float[][]>();
		var colors = new ArrayList<Integer>();
		int frame = Math.floorMod((int) Math.floor(age), 24);
		for (var part : asset.parts) {
			float alpha = opacity * alpha(id, part.role, part.index, age, stage);
			if (alpha <= .005F) continue;
			for (var source : part.quads) {
				float[][] q = new float[4][5];
				for (int k = 0; k < 4; k++) {
					var v = source[k];
					float[] xyz = deform(id, part, v, age, stage);
					q[k] = new float[] { xyz[0], xyz[1], xyz[2], v[3], (frame + v[4]) / 24 };
				}
				geometry.add(q);
				colors.add(Math.clamp((int) (255 * alpha), 0, 255));
			}
		}
		collector.submitCustomGeometry(poses, RenderTypes.entityTranslucent(asset.texture), (pose, out) -> {
			for (int i = 0; i < geometry.size(); i++) draw(pose, out, geometry.get(i), colors.get(i), id.equals("rock_bullet") || id.equals("ice_smash") || ExtendedSpellMotion.shaded(id));
		});
		poses.popPose();
	}
	private static float alpha(String id, String role, int index, float age, int stage) {
		if (ExtendedSpellMotion.handles(id)) return ExtendedSpellMotion.alpha(id, role, index, age, stage);
		if (stage != IMPACT) return role.equals("fracture") || role.equals("splash") ? 0 : 1;
		float end = id.equals("fireball") ? 14 : 20;
		float fade = Math.clamp(1 - age / end, 0, 1);
		if (role.equals("liquid")) return Math.clamp(1 - age / 4, 0, 1);
		if (role.equals("heart") || role.equals("combustion")) return Math.clamp(1 - age / 5, 0, 1);
		if (role.equals("icecore") || role.equals("rock")) return Math.clamp(1 - age / 3, 0, 1);
		return fade * fade;
	}
	private static float[] deform(String id, Part p, float[] v, float t, int stage) {
		if (ExtendedSpellMotion.handles(id)) return ExtendedSpellMotion.deform(id, p.role, p.index, p.cx, p.cy, p.cz, v, t, stage);
		float x = v[0], y = v[1], z = v[2];
		float phase = t * .24F + p.index * 1.73F;
		if (stage == IMPACT) {
			float progress = Math.clamp(t / 20, 0, 1), spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);
			if (id.equals("water_ball")) {
				if (p.role.equals("liquid")) { x *= 1 + t * .45F; y *= 1 + t * .45F; z *= .2F; }
				else {
					float a = p.index * 2.39996F;
					float radius = spread * (p.role.equals("drop") ? 1.7F : 1.15F);
					x = (x - p.cx) * (p.role.equals("drop") ? 1.4F : 1 + spread) + (float) Math.cos(a) * radius;
					y = (y - p.cy) + (float) Math.sin(a) * radius - .9F * progress * progress;
					z = (z - p.cz) * .5F - spread * .3F;
				}
			} else if (id.equals("fireball")) {
				if (p.role.equals("heart") || p.role.equals("combustion")) { float s = 1 + t * .3F; x *= s; y *= s; z *= s * .45F; }
				else {
					float a = p.index * 2.39996F;
					x += (float) Math.cos(a) * spread * 1.2F;
					y += (float) Math.sin(a) * spread * 1.2F + progress * .6F;
					z *= .45F;
				}
			} else {
				float length = (float) Math.sqrt(p.cx * p.cx + p.cy * p.cy + p.cz * p.cz);
				float force = id.equals("ice_smash") ? 1.1F : .8F;
				if (length > .05F) {
					float spin = t * (.08F + p.index % 3 * .03F), dx = x - p.cx, dy = y - p.cy;
					x = dx * (float) Math.cos(spin) - dy * (float) Math.sin(spin) + p.cx * (1 + spread * force / length);
					y = dx * (float) Math.sin(spin) + dy * (float) Math.cos(spin) + p.cy * (1 + spread * force / length) - progress * progress * 1.3F;
					z += p.cz * spread * force / length;
				}
			}
			return new float[] { x, y, z };
		}
		switch (id) {
			case "water_ball" -> {
				if (p.role.equals("drop")) {
					float cycle = (t * .055F + p.index * .127F) % 1;
					if (stage == FORM) {
						float a = phase; float radius = .8F - cycle * .4F;
						x = x - p.cx + (float) Math.cos(a) * radius; y = y - p.cy + (float) Math.sin(a) * radius; z = z - p.cz;
					} else { z = z - p.cz - .45F - cycle * .8F; x += .05F * (float) Math.sin(phase); }
				} else {
					float wave = .025F * (float) Math.sin(y * 10 + z * 7 + t * .3F) + .018F * (float) Math.sin(x * 12 - t * .21F);
					x *= 1 + wave; y *= 1 - wave * .7F; z *= 1 + wave;
				}
			}
			case "fireball" -> {
				float wave = (float) Math.sin(t * .55F - z * 9 + p.index * 2.4F);
				if (p.role.equals("tongue")) {
					float tail = Math.clamp((.2F - z) / 1.6F, 0, 1);
					x += wave * .07F * tail; y += (float) Math.cos(phase + z * 6) * .06F * tail;
					z *= 1 + wave * .16F;
					// Tail stays on -Z, toward the caster, during preparation too.
				} else if (p.role.equals("spark")) {
					float cycle = (t * .08F + p.index * .137F) % 1;
					y += stage == FORM ? cycle * .8F : .08F * (float) Math.sin(phase); z -= stage == FORM ? 0 : cycle;
				} else { float pulse = 1 + .045F * wave; x *= pulse; y *= pulse; z *= pulse; }
			}
			case "ice_smash" -> {
				if (stage == FORM && p.role.equals("crystal")) {
					float grow = .01F + .99F * SpellAtmosphereProfile.smooth((t - p.index * .18F) / 7);
					x *= grow; y *= grow; z *= grow;
				}
			}
			case "rock_bullet" -> {
				// Rigid solid. The small roll is not the liquid wobble used for water.
				float angle = t * .055F, oldX = x;
				x = oldX * (float) Math.cos(angle) - y * (float) Math.sin(angle); y = oldX * (float) Math.sin(angle) + y * (float) Math.cos(angle);
				if (p.role.equals("chip")) z -= ((t * .06F + p.index * .13F) % 1) * .35F;
			}
			case "water_shield" -> {
				if (p.role.equals("spray")) {
					float cycle = (t * .045F + p.index * .061F) % 1;
					x *= 1 + cycle * .2F; y += cycle * .12F - cycle * cycle * .2F; z -= cycle * .1F;
				} else {
					z += .016F * (float) Math.sin(y * 21 - t * .35F + x * 9) + .008F * (float) Math.sin(x * 34 + t * .19F);
					if (p.role.equals("current")) { y += .013F * (float) Math.sin(t * .21F + x * 11 + p.index); }
				}
				float emerge = Math.clamp(t / 6, .01F, 1); y *= emerge;
			}
			default -> { }
		}
		return new float[] { x, y, z };
	}
	private static void draw(PoseStack.Pose pose, VertexConsumer out, float[][] q, int alpha, boolean shaded) {
		float ax = q[1][0] - q[0][0], ay = q[1][1] - q[0][1], az = q[1][2] - q[0][2];
		float bx = q[2][0] - q[0][0], by = q[2][1] - q[0][1], bz = q[2][2] - q[0][2];
		float nx = ay * bz - az * by, ny = az * bx - ax * bz, nz = ax * by - ay * bx;
		float length = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
		if (length < 1E-8F) return;
		nx /= length; ny /= length; nz /= length;
		int shade = shaded ? (int) (255 * (.64F + .28F * Math.max(0, ny) + .08F * Math.abs(nx))) : 255;
		int tint = alpha << 24 | shade << 16 | shade << 8 | shade;
		for (var v : q) out.addVertex(pose, v[0], v[1], v[2]).setColor(tint).setUv(v[3], v[4])
				.setLight(0xF000F0).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose, nx, ny, nz);
	}
	private ReferenceSpellModels() { }
}
