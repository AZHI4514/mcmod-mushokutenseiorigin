package com.example.examplemod.client.magic;

import com.example.examplemod.magic.PhaseTwoEntity;
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

public final class PhaseTwoRenderer extends EntityRenderer<PhaseTwoEntity,PhaseTwoRenderer.State> {
	private final java.util.Map<String,TextureAtlasSprite> sprites=new java.util.HashMap<>();
	public PhaseTwoRenderer(EntityRendererProvider.Context context) {
		super(context);
		for(var name:java.util.List.of("ice","water_still","stone","fire_0","white_stained_glass"))
			sprites.put(name,context.getAtlas(AtlasIds.BLOCKS).getSprite(Identifier.withDefaultNamespace("block/"+name)));
	}
	public static final class State extends EntityRenderState { float yaw,pitch,width,height,depth,offset; int color; boolean fire, crescent; String texture; boolean ray; }

	@Override public State createRenderState() { return new State(); }
	@Override protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(PhaseTwoEntity entity) { return entity.getBoundingBox().inflate(2); }
	@Override public void extractRenderState(PhaseTwoEntity entity,State state,float delta) {
		super.extractRenderState(entity,state,delta);state.ray=entity.ray(); var motion=entity.direction();
		state.yaw=(float)Math.toDegrees(Math.atan2(motion.x,motion.z)); state.pitch=entity.stationary()&&!entity.shield()?0:(float)Math.toDegrees(Math.atan2(motion.y,motion.horizontalDistance()));
		state.width=(float)entity.width(); state.height=(float)entity.height(); state.depth=(float)entity.depth();
		state.offset=(entity.stationary()&&!entity.shield())||entity.spell()==com.example.examplemod.magic.SpellDefinition.EARTH_PILLAR?state.height/2:0;
		if(state.ray){state.width=state.height=state.depth=0.12F;state.offset=0;}
		String id=entity.spell().id(); state.fire=id.equals("flame_pillar");state.crescent=id.equals("icicle_edge");
		state.texture=state.fire?"fire_0":id.equals("earth_pillar")?"stone":entity.spell().icy()?"ice":entity.spell().element().equals("water")?"water_still":"white_stained_glass";
		state.color=state.texture.equals("water_still")?0xB03F76E4:id.equals("physical_shield")?0x7095D3FF:0xDDFFFFFF;
	}
	@Override public void submit(State state,PoseStack poses,SubmitNodeCollector collector,CameraRenderState camera) {
		super.submit(state,poses,collector,camera); poses.pushPose(); poses.translate(0,state.offset,0); poses.mulPose(com.mojang.math.Axis.YP.rotationDegrees(state.yaw)); poses.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-state.pitch));
		var sprite=sprites.get(state.texture);
		collector.submitCustomGeometry(poses,state.fire?RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS):RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS),(pose,out)->{
			float[][][] faces={{{0,0,0},{0,1,0},{1,1,0},{1,0,0}},{{1,0,1},{1,1,1},{0,1,1},{0,0,1}},{{0,0,1},{0,1,1},{0,1,0},{0,0,0}},{{1,0,0},{1,1,0},{1,1,1},{1,0,1}},{{0,1,0},{0,1,1},{1,1,1},{1,1,0}},{{0,0,1},{0,0,0},{1,0,0},{1,0,1}}};
			for(int segment=0;segment<(state.crescent?7:1);segment++) for(var face:faces) for(int i=0;i<4;i++) {
				var p=face[i]; float x=state.crescent?((segment+p[0])/7F-0.5F)*state.width:(p[0]-0.5F)*state.width;
				float y=(p[1]-0.5F)*state.height*(state.crescent?0.35F:1);
				if(state.crescent)y+=(float)(0.35-0.7*Math.pow((segment+0.5)/3.5-1,2));
				out.addVertex(pose,x,y,(p[2]-0.5F)*state.depth).setColor(state.color)
					.setUv(i<2?sprite.getU0():sprite.getU1(),i==0||i==3?sprite.getV1():sprite.getV0()).setLight(state.lightCoords).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose,0,1,0); }
		}); poses.popPose();
	}
}
