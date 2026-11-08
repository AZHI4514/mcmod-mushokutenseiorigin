package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import com.example.examplemod.magic.MagicAreaEntity;

public final class MagicAreaRenderer extends EntityRenderer<MagicAreaEntity,MagicAreaRenderer.State> {
	public MagicAreaRenderer(EntityRendererProvider.Context context) { super(context); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(MagicAreaEntity entity) { return entity.areaBounds(); }
	public static final class State extends EntityRenderState { String spell;float yaw,pitch,phase; }
	@Override public State createRenderState() { return new State(); }
	@Override public void extractRenderState(MagicAreaEntity entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);state.spell=entity.spell().id();state.phase=entity.tickCount+delta;state.yaw=entity.getYRot();state.pitch=entity.wallPitch();
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera);poses.pushPose();poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-state.yaw));
		if(state.spell.equals("water_shield"))poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(state.pitch));
		SpellTextureAnimation.area(poses,collector,state.spell,state.phase);poses.popPose();
	}
}
