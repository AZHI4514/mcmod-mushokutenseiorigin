package com.example.examplemod.client.magic;

import com.example.examplemod.magic.FlameSliceEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/** Animated curved fire edge, layered wake and ragged flame tongues; hit volume stays on the server. */
public final class FlameSliceRenderer extends EntityRenderer<FlameSliceEntity,FlameSliceRenderer.State> {
	public FlameSliceRenderer(EntityRendererProvider.Context context) { super(context); }
	public static final class State extends EntityRenderState { float yaw,pitch,phase; }
	@Override public State createRenderState() { return new State(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(FlameSliceEntity entity) { return entity.getBoundingBox().inflate(2); }
	@Override public void extractRenderState(FlameSliceEntity entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);
		state.phase=entity.tickCount+delta;
		var velocity=entity.getDeltaMovement();
		state.yaw=(float)Math.toDegrees(Math.atan2(velocity.x,velocity.z));
		state.pitch=(float)Math.toDegrees(Math.atan2(velocity.y,velocity.horizontalDistance()));
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera);
		poses.pushPose();
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));
		poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		SpellTextureAnimation.body(poses,collector,"flame_slice",state.phase,3,1,1);
		poses.popPose();
	}
}
