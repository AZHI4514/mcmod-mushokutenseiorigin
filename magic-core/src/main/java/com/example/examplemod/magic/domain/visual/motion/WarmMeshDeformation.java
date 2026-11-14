package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class WarmMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			if (role.equals("thermal")) {
				float rise = fraction(t * .035F + index * .13F); y += rise * .18F;
				x += .015F * (float) Math.sin(y * 11 + t * .3F + index); z += .012F * (float) Math.sin(y * 13 - t * .23F);
			} else { y += .035F * (float) Math.sin(phase); float r = 1 + .025F * (float) Math.sin(phase); x *= r; z *= r; }
		
		return new float[]{x,y,z};
	}
}
