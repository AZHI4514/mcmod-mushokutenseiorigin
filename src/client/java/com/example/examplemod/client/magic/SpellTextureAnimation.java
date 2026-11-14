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
		var layout=com.example.examplemod.magic.domain.visual.AreaMeshLayout.at(id,age);
		if(layout==null)return;
		poses.pushPose();
		poses.translate(0,layout.y(),0);
		SpellMeshModel.submit(poses,collector,id,age,layout.width(),layout.height(),layout.depth(),layout.opacity());
		poses.popPose();
	}
	private SpellTextureAnimation() {}
}
