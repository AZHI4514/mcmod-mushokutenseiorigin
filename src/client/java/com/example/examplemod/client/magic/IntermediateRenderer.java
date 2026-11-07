package com.example.examplemod.client.magic;

import com.example.examplemod.magic.IntermediateProjectile;
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

public final class IntermediateRenderer extends EntityRenderer<IntermediateProjectile,IntermediateRenderer.State> {
	private final TextureAtlasSprite sprite;
	private final float width,height,depth;
	private final int color;
	private final boolean fire;
	public IntermediateRenderer(EntityRendererProvider.Context context,String spell) {
		super(context); fire=spell.equals("ex_flame");
		boolean wind=spell.equals("sonic_boom"),rock=spell.equals("stone_cannon");
		width=fire?2:wind?3:0.25F; height=fire?2:wind?1:0.25F; depth=fire?2:wind?0.25F:rock?0.25F:1;
		String texture=fire?"block/fire_0":wind?"block/white_stained_glass":rock?"block/stone":"block/water_still";
		sprite=context.getAtlas(AtlasIds.BLOCKS).getSprite(Identifier.withDefaultNamespace(texture));
		color=fire||rock?0xFFFFFFFF:wind?0xB0E8FFFF:0xC03F76E4;
	}
	public static final class State extends EntityRenderState { float yaw,pitch; }
	@Override public State createRenderState() { return new State(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(IntermediateProjectile entity) { return entity.getBoundingBox().inflate(2); }
	@Override public void extractRenderState(IntermediateProjectile entity,State state,float delta) {
		super.extractRenderState(entity,state,delta); var motion=entity.getDeltaMovement();
		state.yaw=(float)Math.toDegrees(Math.atan2(motion.x,motion.z)); state.pitch=(float)Math.toDegrees(Math.atan2(motion.y,motion.horizontalDistance()));
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera); poses.pushPose(); poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw)); poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		collector.submitCustomGeometry(poses,fire?RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS):RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS),(pose,out)->{
			float[][][] faces={{{0,0,0},{0,1,0},{1,1,0},{1,0,0}},{{1,0,1},{1,1,1},{0,1,1},{0,0,1}},{{0,0,1},{0,1,1},{0,1,0},{0,0,0}},{{1,0,0},{1,1,0},{1,1,1},{1,0,1}},{{0,1,0},{0,1,1},{1,1,1},{1,1,0}},{{0,0,1},{0,0,0},{1,0,0},{1,0,1}}};
			for(var face:faces) for(int i=0;i<4;i++) { var p=face[i]; out.addVertex(pose,(p[0]-0.5F)*width,(p[1]-0.5F)*height,(p[2]-0.5F)*depth).setColor(color)
					.setUv(i<2?sprite.getU0():sprite.getU1(),i==0||i==3?sprite.getV1():sprite.getV0()).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose,0,1,0); }
		}); poses.popPose();
	}
}
