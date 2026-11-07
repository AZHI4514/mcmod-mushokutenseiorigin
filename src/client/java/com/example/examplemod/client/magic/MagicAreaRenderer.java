package com.example.examplemod.client.magic;

import com.example.examplemod.magic.MagicAreaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

public final class MagicAreaRenderer extends EntityRenderer<MagicAreaEntity, MagicAreaRenderer.State> {
	private final TextureAtlasSprite water, fire, glass;
	public MagicAreaRenderer(EntityRendererProvider.Context context) {
		super(context);
		var atlas = context.getAtlas(AtlasIds.BLOCKS);
		water = atlas.getSprite(Identifier.withDefaultNamespace("block/water_still"));
		fire = atlas.getSprite(Identifier.withDefaultNamespace("block/fire_0"));
		glass = atlas.getSprite(Identifier.withDefaultNamespace("block/light_blue_stained_glass"));
	}
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(MagicAreaEntity entity) { return entity.areaBounds(); }
	public static final class State extends EntityRenderState { String spell; float yaw,pitch; }
	@Override public State createRenderState() { return new State(); }
	@Override public void extractRenderState(MagicAreaEntity entity, State state, float delta) {
		super.extractRenderState(entity, state, delta); state.spell = entity.spell().id(); state.yaw=entity.getYRot();state.pitch=entity.wallPitch();
	}
	@Override public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poses, collector, camera); poses.pushPose();
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-state.yaw));
		boolean waterWall = state.spell.equals("water_shield"), burning = state.spell.equals("burning_place");
		if(waterWall)poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(state.pitch));
		boolean warm = state.spell.equals("heat_island");
		var sprite = waterWall ? water : burning ? fire : glass;
		int color = waterWall ? 0x803F76E4 : burning ? 0xFFFFFFFF : warm ? 0x50FFB65C : 0x4096DCFF;
		var pipeline = burning ? RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS) : RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);
		collector.submitCustomGeometry(poses, pipeline, (pose, vertices) -> {
			if (waterWall) {
				quad(pose, vertices, sprite, color, -1.5F,-1,-0.15F, 1.5F,-1,-0.15F, 1.5F,1,-0.15F, -1.5F,1,-0.15F);
				quad(pose, vertices, sprite, color, -1.5F,1,0.15F, 1.5F,1,0.15F, 1.5F,-1,0.15F, -1.5F,-1,0.15F);
			} else {
				float radius = warm ? 3 : state.spell.equals("magic_shield") ? 0.85F : 2;
				float height = warm ? 0.12F : burning ? 0.65F : 2.1F;
				for (int i=0; i<24; i++) {
					float a=(float)(i*Math.PI/12), b=(float)((i+1)*Math.PI/12);
					float x=(float)Math.cos(a)*radius,z=(float)Math.sin(a)*radius;
					float xx=(float)Math.cos(b)*radius,zz=(float)Math.sin(b)*radius;
					quad(pose,vertices,sprite,color,x,0,z,xx,0,zz,xx,height,zz,x,height,z);
					quad(pose,vertices,sprite,color,x,height,z,xx,height,zz,xx,0,zz,x,0,z);
				}
			}
		}); poses.popPose();
	}
	private static void quad(PoseStack.Pose pose, com.mojang.blaze3d.vertex.VertexConsumer out, TextureAtlasSprite s, int color, float... points) {
		for (int i=0;i<4;i++) out.addVertex(pose,points[i*3],points[i*3+1],points[i*3+2]).setColor(color)
				.setUv(i==0||i==3?s.getU0():s.getU1(),i<2?s.getV1():s.getV0()).setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(0xF000F0).setNormal(pose,0,1,0);
	}
}
