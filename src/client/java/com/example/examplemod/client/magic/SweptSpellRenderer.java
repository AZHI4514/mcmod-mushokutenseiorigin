package com.example.examplemod.client.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import com.example.examplemod.magic.SweptSpellProjectile;

public final class SweptSpellRenderer extends EntityRenderer<SweptSpellProjectile,SweptSpellRenderer.State> {
	private final String spell;
	private final float width,height,depth;
	public SweptSpellRenderer(EntityRendererProvider.Context context,String spell) {
		super(context);this.spell=spell;
		var spec=com.example.examplemod.magic.domain.visual.ProjectileMeshSpec.swept(spell);
		width=spec.width();height=spec.height();depth=spec.depth();
	}
	public static final class State extends EntityRenderState { float yaw,pitch,phase; }
	@Override public State createRenderState() { return new State(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(SweptSpellProjectile entity) { return entity.getBoundingBox().inflate(2); }
	@Override public void extractRenderState(SweptSpellProjectile entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);state.phase=entity.tickCount+delta;var motion=entity.getDeltaMovement();
		state.yaw=(float)Math.toDegrees(Math.atan2(motion.x,motion.z));state.pitch=(float)Math.toDegrees(Math.atan2(motion.y,motion.horizontalDistance()));
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera);poses.pushPose();
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		SpellTextureAnimation.body(poses,collector,spell,state.phase,width,height,depth);poses.popPose();
	}
}
