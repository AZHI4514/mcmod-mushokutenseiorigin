package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import com.example.examplemod.magic.DirectionalSpellCarrier;
import com.example.examplemod.magic.SpellDefinition;

public final class DirectionalSpellRenderer extends EntityRenderer<DirectionalSpellCarrier,DirectionalSpellRenderer.State> {
	public DirectionalSpellRenderer(EntityRendererProvider.Context context) { super(context); }
	public static final class State extends EntityRenderState { String spell;float yaw,pitch,width,height,depth,offset,phase; }
	@Override public State createRenderState() { return new State(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(DirectionalSpellCarrier entity) { return entity.getBoundingBox().inflate(2); }
	@Override public void extractRenderState(DirectionalSpellCarrier entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);state.phase=entity.tickCount+delta;state.spell=entity.spell().id();var motion=entity.direction();
		state.yaw=(float)Math.toDegrees(Math.atan2(motion.x,motion.z));state.pitch=com.example.examplemod.magic.domain.visual.ProjectileMeshSpec.carrierPitch(entity.fallingRock(),entity.stationary(),entity.shield(),motion.y,motion.horizontalDistance());
		state.width=(float)entity.width();state.height=(float)entity.height();state.depth=(float)entity.depth();
		state.offset=com.example.examplemod.magic.domain.visual.ProjectileMeshSpec.carrierOffset(entity.stationary(),entity.shield(),entity.spell()==SpellDefinition.EARTH_PILLAR,state.height);
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera);poses.pushPose();poses.translate(0,state.offset,0);
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		SpellTextureAnimation.body(poses,collector,state.spell,state.phase,state.width,state.height,state.depth);poses.popPose();
	}
}
