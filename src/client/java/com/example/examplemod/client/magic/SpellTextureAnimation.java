package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

/** Entity/area entry points for reference-authored solid meshes and transparent flow layers. */
final class SpellTextureAnimation {
	static void body(PoseStack poses, SubmitNodeCollector collector, String id, float age, float w, float h, float d) {
		if (id.equals("physical_shield")) d = 1;
		SpellMeshModel.submit(poses, collector, id, age, w, h, d, 1);
	}
	static void area(PoseStack poses, SubmitNodeCollector collector, String id, float age) {
		poses.pushPose();
		switch (id) {
			case "water_shield" -> SpellMeshModel.submit(poses, collector, id, age, 3, 2, 1, 1);
			case "magic_shield" -> {
				poses.translate(0, 1, 0);
				SpellMeshModel.submit(poses, collector, id, age, 1.8F, 2.1F, 1.8F, 1);
			}
			case "burning_place" -> { poses.translate(0, .9, 0); SpellMeshModel.submit(poses, collector, id, age, 1F, 1.9F, 1F, 1); }
			case "heat_island" -> { poses.translate(0, .08, 0); SpellMeshModel.submit(poses, collector, id, age, 5.8F, .7F, 5.8F, .6F); }
			default -> { }
		}
		poses.popPose();
	}
	private SpellTextureAnimation() { }
}
