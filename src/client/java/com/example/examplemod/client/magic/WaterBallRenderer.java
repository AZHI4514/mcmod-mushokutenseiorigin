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
public final class WaterBallRenderer extends EntityRenderer<WaterBallEntity, EntityRenderState> {
	private final TextureAtlasSprite water;
	private final int color;
	public WaterBallRenderer(EntityRendererProvider.Context context, String spell) {
		super(context);
		// 26.1 查询使用逻辑图集 ID；渲染绑定仍使用下方的纹理路径。
		String texture = switch (spell) {
			case "fireball" -> "block/fire_0";
			case "wind_ball" -> "block/white_stained_glass";
			case "rock_bullet" -> "block/stone";
			default -> "block/water_still";
		};
		color = switch (spell) {
			case "fireball", "rock_bullet" -> 0xFFFFFFFF;
			case "wind_ball" -> 0xA0DEFFFF;
			default -> 0xB33F76E4;
		};
		water = context.getAtlas(AtlasIds.BLOCKS).getSprite(Identifier.withDefaultNamespace(texture));
	}
	@Override
	public EntityRenderState createRenderState() { return new EntityRenderState(); }
	@Override
	public void submit(EntityRenderState state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poses, collector, camera);
		int light = state.lightCoords;
		collector.submitCustomGeometry(poses, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (pose, vertices) -> {
			face(pose, vertices, light, new float[][]{{0,0,0},{0,1,0},{1,1,0},{1,0,0}}, 0,0,-1);
			face(pose, vertices, light, new float[][]{{1,0,1},{1,1,1},{0,1,1},{0,0,1}}, 0,0,1);
			face(pose, vertices, light, new float[][]{{0,0,1},{0,1,1},{0,1,0},{0,0,0}}, -1,0,0);
			face(pose, vertices, light, new float[][]{{1,0,0},{1,1,0},{1,1,1},{1,0,1}}, 1,0,0);
			face(pose, vertices, light, new float[][]{{0,1,0},{0,1,1},{1,1,1},{1,1,0}}, 0,1,0);
			face(pose, vertices, light, new float[][]{{0,0,1},{0,0,0},{1,0,0},{1,0,1}}, 0,-1,0);
		});
	}
	private void face(PoseStack.Pose pose, VertexConsumer out, int light, float[][] points, float nx, float ny, float nz) {
		for (int i = 0; i < 4; i++) {
			float[] p = points[i];
			out.addVertex(pose, (p[0] - 0.5F) * 0.25F, p[1] * 0.25F, (p[2] - 0.5F) * 0.25F)
					.setColor(color).setUv(i < 2 ? water.getU0() : water.getU1(), i == 0 || i == 3 ? water.getV1() : water.getV0())
					.setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
		}
	}
}
