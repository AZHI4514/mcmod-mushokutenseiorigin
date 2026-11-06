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
import com.example.examplemod.magic.WaterBallEntity;

/** 使用原版方块图集的动画水纹理，不复制整张动画帧条带。 */
public final class WaterBallRenderer extends EntityRenderer<WaterBallEntity, WaterBallRenderer.State> {
	private final TextureAtlasSprite water;
	private final int color;
	private final boolean arrow;
	private final boolean blade;
	private final float size;
	public WaterBallRenderer(EntityRendererProvider.Context context, String spell) {
		super(context);
		blade = spell.equals("ice_blade");
		arrow = spell.equals("water_arrow") || spell.equals("ice_blade");
		size = spell.equals("ice_smash") ? 0.4F : 0.25F;
		// 26.1 查询使用逻辑图集 ID；渲染绑定仍使用下方的纹理路径。
		String texture = switch (spell) {
			case "fireball" -> "block/fire_0";
			case "wind_ball" -> "block/white_stained_glass";
			case "rock_bullet" -> "block/stone";
			case "ice_smash", "ice_blade" -> "block/ice";
			default -> "block/water_still";
		};
		color = switch (spell) {
			case "fireball", "rock_bullet" -> 0xFFFFFFFF;
			case "wind_ball" -> 0xA0DEFFFF;
			case "ice_smash", "ice_blade" -> 0xD0C7EBFF;
			default -> 0xB33F76E4;
		};
		water = context.getAtlas(AtlasIds.BLOCKS).getSprite(Identifier.withDefaultNamespace(texture));
	}
	@Override
	public State createRenderState() { return new State(); }
	public static final class State extends EntityRenderState { float yaw; float pitch; }
	@Override
	public void extractRenderState(WaterBallEntity entity, State state, float delta) {
		super.extractRenderState(entity, state, delta);
		state.yaw = entity.getYRot(); state.pitch = entity.getXRot();
	}
	@Override
	public void submit(State state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poses, collector, camera);
		int light = state.lightCoords;
		poses.pushPose();
		if (arrow) {
			poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));
			poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		}
		collector.submitCustomGeometry(poses, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (pose, vertices) -> {
			face(pose, vertices, light, new float[][]{{0,0,0},{0,1,0},{1,1,0},{1,0,0}}, 0,0,-1);
			face(pose, vertices, light, new float[][]{{1,0,1},{1,1,1},{0,1,1},{0,0,1}}, 0,0,1);
			face(pose, vertices, light, new float[][]{{0,0,1},{0,1,1},{0,1,0},{0,0,0}}, -1,0,0);
			face(pose, vertices, light, new float[][]{{1,0,0},{1,1,0},{1,1,1},{1,0,1}}, 1,0,0);
			face(pose, vertices, light, new float[][]{{0,1,0},{0,1,1},{1,1,1},{1,1,0}}, 0,1,0);
			face(pose, vertices, light, new float[][]{{0,0,1},{0,0,0},{1,0,0},{1,0,1}}, 0,-1,0);
		});
		poses.popPose();
	}
	private void face(PoseStack.Pose pose, VertexConsumer out, int light, float[][] points, float nx, float ny, float nz) {
		for (int i = 0; i < 4; i++) {
			float[] p = points[i];
			out.addVertex(pose, (p[0] - 0.5F) * (blade ? 0.08F : arrow ? 0.075F : size), (p[1] - (arrow ? 0.5F : 0)) * (blade ? 0.4F : arrow ? 0.075F : size), (p[2] - 0.5F) * (blade ? 0.8F : arrow ? 0.4F : size))
					.setColor(color).setUv(i < 2 ? water.getU0() : water.getU1(), i == 0 || i == 3 ? water.getV1() : water.getV0())
					.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
		}
	}
}
