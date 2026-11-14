package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import com.example.examplemod.magic.WaterBallEntity;

/** Existing projectile volumes, rendered exclusively with source-derived spell textures. */
public final class WaterBallRenderer extends EntityRenderer<WaterBallEntity,WaterBallRenderer.State> {
	private final String spell;
	public WaterBallRenderer(EntityRendererProvider.Context context,String spell) { super(context);this.spell=spell; }
	public static final class State extends EntityRenderState { float yaw,pitch,phase; }
	@Override public State createRenderState() { return new State(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(WaterBallEntity entity) { return entity.getBoundingBox().inflate(1); }
	@Override public void extractRenderState(WaterBallEntity entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);state.phase=entity.tickCount+delta;
		var motion=entity.getDeltaMovement();
		state.yaw=(float)Math.toDegrees(Math.atan2(motion.x,motion.z));
		state.pitch=(float)Math.toDegrees(Math.atan2(motion.y,motion.horizontalDistance()));
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera);poses.pushPose();
		var spec=com.example.examplemod.magic.domain.visual.ProjectileMeshSpec.basic(spell);
		if(spec.offset()!=0)poses.translate(0,spec.offset(),0);
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));
		poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		SpellTextureAnimation.body(poses,collector,spell,state.phase,spec.width(),spec.height(),spec.depth());
		poses.popPose();
	}
}
