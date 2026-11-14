package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import com.example.examplemod.magic.MagicAreaEntity;

public final class MagicAreaRenderer extends EntityRenderer<MagicAreaEntity,MagicAreaRenderer.State> {
	private final net.minecraft.client.renderer.block.BlockModelResolver blocks;
	public MagicAreaRenderer(EntityRendererProvider.Context context) { super(context);blocks=context.getBlockModelResolver(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(MagicAreaEntity entity) { return entity.areaBounds(); }
	public static final class State extends EntityRenderState { java.util.List<IllusionProjection.Cell> projection=java.util.List.of();String spell;float yaw,pitch,phase;java.util.List<net.minecraft.world.phys.Vec3> spikes=java.util.List.of(); }
	@Override public State createRenderState() { return new State(); }
	@Override public void extractRenderState(MagicAreaEntity entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);state.projection=entity.spell()==com.example.examplemod.magic.SpellDefinition.ILLUSION_BARRIER?IllusionProjection.extract(entity,blocks):java.util.List.of();state.spell=entity.spell().id();state.phase=com.example.examplemod.magic.domain.visual.AreaAnimationClock.usesWorldStart(entity.spell())?entity.level().getGameTime()-entity.getAttachedOrCreate(com.example.examplemod.magic.MagicAttachments.SPIKES_STARTED)+delta:entity.tickCount+delta;state.yaw=entity.getYRot();state.pitch=entity.wallPitch();state.spikes=entity.getAttachedOrCreate(com.example.examplemod.magic.MagicAttachments.SPIKE_BASES).stream().map(p->net.minecraft.world.phys.Vec3.atBottomCenterOf(p).subtract(entity.position())).toList();
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera);if(state.spell.equals("illusion_barrier")){IllusionProjection.submit(state.projection,poses,collector);return;}if(state.spell.equals("earth_hedgehog")){for(int i=0;i<state.spikes.size();i++){float age=state.phase-i*2;if(age<0)continue;var at=state.spikes.get(i);poses.pushPose();poses.translate(at.x,at.y+1.5,at.z);ReferenceSpellModels.submit(poses,collector,state.spell,age,1,3,1,1,ReferenceSpellModels.FLIGHT);poses.popPose();}return;}poses.pushPose();poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-state.yaw));
		if(state.spell.equals("water_shield"))poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(state.pitch));
		SpellTextureAnimation.area(poses,collector,state.spell,state.phase);poses.popPose();
	}
}
