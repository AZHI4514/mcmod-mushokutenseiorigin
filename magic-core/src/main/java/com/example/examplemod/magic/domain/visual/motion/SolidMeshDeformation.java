package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class SolidMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			boolean grows = role.equals("growth") || role.equals("groundspike") || role.equals("wallice");
			if (grows) {
				float grow = .01F + .99F * SpellAtmosphereProfile.smooth((t - index * .12F) / (id.equals("icicle_field") ? 6 : 4));
				if (stage == MeshStage.FLIGHT && id.equals("ice_lance")) grow = .01F + .99F * SpellAtmosphereProfile.smooth(t / 5);
				y = -.5F + (y + .5F) * grow;
			} else if (impact) {
				float length = (float) Math.sqrt(cx * cx + cy * cy + cz * cz);
				if (length > .03F) {
					float a = t * (.07F + index % 3 * .035F), dx = x - cx, dy = y - cy;
					x = dx * (float) Math.cos(a) - dy * (float) Math.sin(a) + cx * (1 + spread / length);
					y = dx * (float) Math.sin(a) + dy * (float) Math.cos(a) + cy * (1 + spread / length) - progress * progress;
					z += cz * spread / length;
				}
			} else if (id.equals("stone_cannon") || id.equals("icicle_break")) {
				float a = t * (id.equals("stone_cannon") ? .36F : .18F), oldX = x;
				x = x * (float) Math.cos(a) - y * (float) Math.sin(a); y = oldX * (float) Math.sin(a) + y * (float) Math.cos(a);
				if (role.equals("rubble")) z -= fraction(t * .09F + index * .13F) * .45F;
			} else if (form) { float grow = .01F + .99F * SpellAtmosphereProfile.smooth((t - index * .12F) / 8); x *= grow; y *= grow; z *= grow; }
		
		return new float[]{x,y,z};
	}
}
