package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class FireMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			if (id.equals("explosion") && impact) {
				float grow = .18F + .82F * (1 - (float) Math.pow(1 - Math.clamp(t / 7, 0, 1), 2));
				x *= grow; z *= grow; y *= grow;
				if (role.equals("firebloom")) { y += progress * .35F; x += cx * progress * .5F; z += cz * progress * .5F; }
				if (role.equals("blastwave")) { x *= 1 + progress * .5F; z *= 1 + progress * .5F; y = -.3F; }
				if (role.equals("ember")) { x += cx * progress; z += cz * progress; y += progress * .6F; }
			} else if (impact && !id.equals("flame_pillar")) {
				float a = index * 2.39996F;
				if (role.equals("hotcore")) { float grow = 1 + t * .24F; x *= grow; y *= grow; z *= grow; }
				else { x += (float) Math.cos(a) * spread; y += (float) Math.sin(a) * spread + progress * .5F; z *= .5F; }
			} else if (id.equals("burning_place") || id.equals("flame_pillar")) {
				if (role.equals("ember")) { float c = fraction(t * .065F + index * .127F); y = -.35F + c * .9F; x += .018F * (float) Math.sin(phase); }
				else if (!role.equals("emberbed")) {
					float h = y + .5F;
					x += .025F * (float) Math.sin(h * 9 - t * .5F + index) * h;
					z += .025F * (float) Math.cos(h * 8 - t * .43F + index) * h;
					y = -.5F + h * (1 + .11F * (float) Math.sin(phase));
				}
			} else if (id.equals("flamethrower")) {
				float dist = z + .5F; float angle = .14F * (float) Math.sin(t * .6F - z * 14 + index);
				float oldX = x; x = x * (float) Math.cos(angle) - y * (float) Math.sin(angle); y = oldX * (float) Math.sin(angle) + y * (float) Math.cos(angle);
				x += .03F * dist * (float) Math.sin(t * .5F - z * 17 + index); y += .03F * dist * (float) Math.cos(t * .43F - z * 13 + index);
			} else {
				float tail = Math.clamp(.2F - z, 0, 1.5F), wave = (float) Math.sin(t * .6F - z * 11 + index * 2.4F);
				x += tail * wave * .025F; y += tail * (float) Math.cos(phase - z * 7) * .04F;
				if (role.equals("firetongue")) z *= 1 + wave * .15F;
				if (role.equals("firewake")) z -= .05F + index * .05F;
				// Keep the trailing flame facing back along the aim.
			}
		
		return new float[]{x,y,z};
	}
}
