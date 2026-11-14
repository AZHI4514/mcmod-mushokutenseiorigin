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

/** All 39 reference-led spell meshes. All motion is client-only and independent of the damage volume. */
final class ReferenceSpellModels {
	static final int FORM = 0, FLIGHT = 1, IMPACT = 2;
	static boolean handles(String id){return ReferenceMeshRepository.handles(id);}
	static void submit(PoseStack poses, SubmitNodeCollector collector, String id, float age, float width, float height, float depth, float opacity, int stage) {
		var asset = ReferenceMeshRepository.get(id);
		if (asset == null || opacity <= .005F || width <= 0 || height <= 0 || depth <= 0) return;
		if ((id.equals("water_shield") || MeshMotion.persistent(id)) && stage == IMPACT) return;
		poses.pushPose();
		poses.scale(width, height, depth);
		// Build immutable deformed geometry before deferred rendering; no live entity is captured.
		var geometry = new ArrayList<float[][]>();
		var colors = new ArrayList<Integer>();
		int frame = Math.floorMod((int) Math.floor(age), 24);
		for (var part : asset.parts()) {
			float alpha = opacity * com.example.examplemod.magic.domain.visual.motion.ReferenceMeshMotion.alpha(id, part.role(), part.index(), age, stage);
			if (alpha <= .005F) continue;
			for (var source : part.quads()) {
				float[][] q = new float[4][5];
				for (int k = 0; k < 4; k++) {
					var v = source[k];
					float[] xyz = com.example.examplemod.magic.domain.visual.motion.ReferenceMeshMotion.deform(id,part.role(),part.index(),part.cx(),part.cy(),part.cz(),v,age,stage);
					q[k] = new float[] { xyz[0], xyz[1], xyz[2], v[3], (frame + v[4]) / 24 };
				}
				geometry.add(q);
				colors.add(Math.clamp((int) (255 * alpha), 0, 255));
			}
		}
		collector.submitCustomGeometry(poses, RenderTypes.entityTranslucent(asset.texture()), (pose, out) -> {
			for (int i = 0; i < geometry.size(); i++) draw(pose, out, geometry.get(i), colors.get(i), id.equals("rock_bullet") || id.equals("ice_smash") || MeshMotion.shaded(id));
		});
		poses.popPose();
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
