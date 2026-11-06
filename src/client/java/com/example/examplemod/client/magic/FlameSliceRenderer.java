package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.data.AtlasIds;
import com.example.examplemod.magic.FlameSliceEntity;

/** 原版动画火焰面片，透明像素裁剪；没有盒体侧面或黑色周边，方向与实际速度一致。 */
public final class FlameSliceRenderer extends EntityRenderer<FlameSliceEntity, FlameSliceRenderer.State> {
	private final TextureAtlasSprite sprite;
	private final int color;
	public FlameSliceRenderer(EntityRendererProvider.Context context) {
		super(context);
		color = 0xFFFFFFFF;
		sprite = context.getAtlas(AtlasIds.BLOCKS).getSprite(Identifier.withDefaultNamespace("block/fire_0"));
	}
	@Override
	public State createRenderState() { return new State(); }
	public static final class State extends EntityRenderState { float yaw; float pitch; }
	@Override
	public void extractRenderState(FlameSliceEntity entity, State state, float delta) {
		super.extractRenderState(entity, state, delta);
		var velocity = entity.getDeltaMovement();
		state.yaw = (float) Math.toDegrees(Math.atan2(velocity.x, velocity.z));
		state.pitch = (float) Math.toDegrees(Math.atan2(velocity.y, velocity.horizontalDistance()));
	}
	@Override
	public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poses, collector, camera);
		int light = 0xF000F0;
		poses.pushPose();
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));
		poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		collector.submitCustomGeometry(poses, RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), (pose, vertices) -> {
			face(pose, vertices, light, new float[][]{{0,0,0},{0,1,0},{1,1,0},{1,0,0}}, 0,0,-1);
			face(pose, vertices, light, new float[][]{{1,0,1},{1,1,1},{0,1,1},{0,0,1}}, 0,0,1);
		});
		poses.popPose();
	}
	private void face(PoseStack.Pose pose, VertexConsumer out, int light, float[][] points, float nx, float ny, float nz) {
		for (int i = 0; i < 4; i++) {
			float[] p = points[i];
			out.addVertex(pose, (p[0] - 0.5F) * 3.0F, (p[1] - 0.5F) * 1.0F, (p[2] - 0.5F) * 1.0F)
					.setColor(color).setUv(i < 2 ? sprite.getU0() : sprite.getU1(), i == 0 || i == 3 ? sprite.getV1() : sprite.getV0())
					.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
		}
	}
}
