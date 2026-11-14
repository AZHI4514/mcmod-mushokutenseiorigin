package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

/** Shared entity/area entry point; all 39 spells now use reference meshes. */
final class SpellMeshModel {
	static void submit(PoseStack poses, SubmitNodeCollector collector, String id, float age, float width, float height, float depth, float opacity) {
		if(LayeredPixelMeshRenderer.handles(id))LayeredPixelMeshRenderer.submit(poses,collector,id,age,width,height,depth,opacity,false);else ReferenceSpellModels.submit(poses, collector, id, age, width, height, depth, opacity, ReferenceSpellModels.FLIGHT);
	}
	private SpellMeshModel() { }
}
