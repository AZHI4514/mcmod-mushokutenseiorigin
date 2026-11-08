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
		boolean blade=spell.equals("ice_blade"),arrow=spell.equals("water_arrow");
		float size=switch(spell) {
			case "water_ball" -> .42F;
			case "wind_ball" -> .45F;
			case "fireball" -> .45F;
			case "ice_smash" -> .6F;
			case "rock_bullet" -> .4F;
			default -> .25F;
		};
		if(!blade&&!arrow)poses.translate(0,size/2,0);
		poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw));
		poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		SpellTextureAnimation.body(poses,collector,spell,state.phase,blade?.14F:arrow?.16F:size,blade?.65F:arrow?.16F:size,blade?.95F:arrow?.65F:size);
		poses.popPose();
	}
}
