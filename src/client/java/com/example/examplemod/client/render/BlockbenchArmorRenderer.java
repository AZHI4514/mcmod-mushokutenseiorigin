package com.example.examplemod.client.render;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

/**
 * 渲染由工具导出的静态 Blockbench 网格，保留任意四边形和退化四边形表示的三角形。
 * 只读取模组资源，不依赖开发者电脑上的 bbmodel 绝对路径。
 */
public final class BlockbenchArmorRenderer implements ArmorRenderer {

	private final HumanoidModel<HumanoidRenderState> poseModel;
	private final List<Batch> batches;

	public BlockbenchArmorRenderer(EntityRendererProvider.Context context, Identifier modelId) {
		// 独立姿态模型避免读取或修改延迟提交队列里共用的玩家模型。
		// 普通和纤细玩家头部结构相同；手臂宽度不参与此渲染。
		poseModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER));
		try (Reader reader = context.getResourceManager().getResourceOrThrow(modelId).openAsReader()) {
			batches = readBatches(JsonParser.parseReader(reader).getAsJsonObject());
		} catch (IOException | RuntimeException exception) {
			throw new IllegalStateException("Unable to load equipment mesh " + modelId, exception);
		}
	}

	@Override
	public void render(PoseStack poses, SubmitNodeCollector collector, ItemStack stack,
			HumanoidRenderState state, EquipmentSlot slot, int light,
			HumanoidModel<HumanoidRenderState> contextModel) {
		if (slot != EquipmentSlot.HEAD) {
			return;
		}

		poseModel.resetPose();
		poseModel.setupAnim(state);
		poses.pushPose();
		poseModel.head.translateAndRotate(poses);
		int overlay = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
		for (Batch batch : batches) {
			// callback 只捕获不可变几何及当前实体的光照，不捕获可变模型姿态。
			collector.submitCustomGeometry(poses, RenderTypes.armorCutoutNoCull(batch.texture()),
					(pose, vertices) -> batch.render(pose, vertices, light, overlay));
		}
		poses.popPose();
	}

	@Override
	public boolean shouldRenderDefaultHeadItem(LivingEntity entity, ItemStack stack) {
		return false;
	}

	private static List<Batch> readBatches(JsonObject model) {
		if (model.get("format_version").getAsInt() != 1) {
			throw new IllegalArgumentException("Unsupported equipment mesh version");
		}
		List<Batch> result = new ArrayList<>();
		for (JsonElement element : model.getAsJsonArray("batches")) {
			JsonObject batch = element.getAsJsonObject();
			List<Vertex> vertices = new ArrayList<>();
			for (JsonElement quadElement : batch.getAsJsonArray("quads")) {
				JsonArray quad = quadElement.getAsJsonArray();
				if (quad.size() != 4) {
					throw new IllegalArgumentException("Each submitted face must have four vertices");
				}
				for (JsonElement vertexElement : quad) {
					JsonArray v = vertexElement.getAsJsonArray();
					if (v.size() != 8) {
						throw new IllegalArgumentException("Expected position, UV and normal");
					}
					float[] values = new float[8];
					for (int i = 0; i < values.length; i++) {
						values[i] = v.get(i).getAsFloat();
						if (!Float.isFinite(values[i])) {
							throw new IllegalArgumentException("Non-finite vertex value");
						}
					}
					vertices.add(new Vertex(values[0], values[1], values[2], values[3],
							values[4], values[5], values[6], values[7]));
				}
			}
			result.add(new Batch(Identifier.parse(batch.get("texture").getAsString()), List.copyOf(vertices)));
		}
		return List.copyOf(result);
	}

	private record Vertex(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
	}

	private record Batch(Identifier texture, List<Vertex> vertices) {
		void render(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay) {
			for (Vertex v : vertices) {
				consumer.addVertex(pose, v.x(), v.y(), v.z())
						.setColor(0xFFFFFFFF)
						.setUv(v.u(), v.v())
						.setOverlay(overlay)
						.setLight(light)
						.setNormal(pose, v.nx(), v.ny(), v.nz());
			}
		}
	}
}
