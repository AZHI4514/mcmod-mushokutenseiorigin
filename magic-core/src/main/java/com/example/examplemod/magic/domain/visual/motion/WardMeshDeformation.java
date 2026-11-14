package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class WardMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			float pulse = 1 + .008F * (float) Math.sin(t * .2F);
			x *= pulse; y *= pulse; z *= pulse;
			// Keep engraved directions stable, unlike a spinning physical shield.
			if (role.equals("wardmote")) { float a = t * .035F; float oldX = x; x = x * (float) Math.cos(a) + z * (float) Math.sin(a); z = -oldX * (float) Math.sin(a) + z * (float) Math.cos(a); }
			if (role.equals("wardface") || role.equals("barrierveil")) z += .002F * (float) Math.sin(x * 14 + y * 9 - t * .3F);
		
		return new float[]{x,y,z};
	}
}
