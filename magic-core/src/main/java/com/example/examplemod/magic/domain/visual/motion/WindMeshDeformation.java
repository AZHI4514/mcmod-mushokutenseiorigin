package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class WindMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			if (id.equals("wind_bind")) {
				float a = t * .13F, oldX = x; x = x * (float) Math.cos(a) + z * (float) Math.sin(a); z = -oldX * (float) Math.sin(a) + z * (float) Math.cos(a);
				y += .015F * (float) Math.sin(phase + y * 8);
			} else {
				if (id.equals("wind_ball")) { float a = t * .16F, oldX = x; x = x * (float) Math.cos(a) - y * (float) Math.sin(a); y = oldX * (float) Math.sin(a) + y * (float) Math.cos(a); }
				else if (role.equals("gust")) { float a = t * .06F - z * .3F, oldX = x; x = x * (float) Math.cos(a) - y * (float) Math.sin(a); y = oldX * (float) Math.sin(a) + y * (float) Math.cos(a); }
				else z -= .04F * (float) Math.sin(phase);
				if (impact) { float r = 1 + spread * 1.5F; x *= r; y *= r; z *= .4F; }
			}
		
		return new float[]{x,y,z};
	}
}
