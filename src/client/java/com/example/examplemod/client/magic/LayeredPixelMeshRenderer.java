package com.example.examplemod.client.magic;

import com.example.examplemod.magic.domain.visual.mesh.LayeredPixelMesh;
import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Client-only render submission for pure deterministic pixel geometry. */
final class LayeredPixelMeshRenderer {
	static boolean handles(String id){return LayeredPixelMesh.handles(id);}
	static void submit(PoseStack poses,SubmitNodeCollector collector,String id,float age,float width,float height,float depth,float opacity,boolean impact){
		var faces=LayeredPixelMesh.build(id,age,opacity,impact);
		if(faces.isEmpty())return;

		poses.pushPose();poses.scale(width,height,depth);final int frame=Math.floorMod((int)age/2,24);
		collector.submitCustomGeometry(poses,RenderTypes.entityTranslucent(ExampleMod.id("textures/effect/expansion/"+LayeredPixelMesh.texture(id)+".png")),(pose,out)->{
			for(var face:faces){var q=face.points();float ax=q[1][0]-q[0][0],ay=q[1][1]-q[0][1],az=q[1][2]-q[0][2],bx=q[2][0]-q[0][0],by=q[2][1]-q[0][1],bz=q[2][2]-q[0][2];float nx=ay*bz-az*by,ny=az*bx-ax*bz,nz=ax*by-ay*bx,len=(float)Math.sqrt(nx*nx+ny*ny+nz*nz);if(len<1E-8F)continue;
				for(int side=0;side<2;side++)for(int k=0;k<4;k++){int j=side==0?k:3-k;var v=q[j];out.addVertex(pose,v[0],v[1],v[2]).setColor(face.color()).setUv(j==0||j==3?0:1,(frame+(j<2?0:1))/24F).setLight(0xF000F0).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(pose,nx/len*(side==0?1:-1),ny/len*(side==0?1:-1),nz/len*(side==0?1:-1));}
			}
		});poses.popPose();
	}
	private LayeredPixelMeshRenderer(){}
}
