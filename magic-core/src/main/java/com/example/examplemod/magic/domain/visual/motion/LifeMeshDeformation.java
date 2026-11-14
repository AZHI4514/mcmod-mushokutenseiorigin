package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class LifeMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			if (role.equals("lifemote") || role.equals("lightseed")) {
				float rise = fraction(t * (id.equals("antidote_ii") ? .025F : .018F) + index * .079F);
				y = y - cy - .45F + rise * .95F;
				float a = index * 2.39996F + t * .035F, r = .3F + .09F * (float) Math.sin(phase);
				x = x - cx + (float) Math.cos(a) * r; z = z - cz + (float) Math.sin(a) * r;
			} else {
				float a = t * .025F, oldX = x; x = x * (float) Math.cos(a) + z * (float) Math.sin(a); z = -oldX * (float) Math.sin(a) + z * (float) Math.cos(a);
				float pulse = .92F + .08F * sinUnit(phase); x *= pulse; z *= pulse;
			}
		
		return new float[]{x,y,z};
	}
}
