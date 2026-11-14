package com.example.examplemod.magic.domain.visual.motion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshMotion.*;
final class WaterMeshDeformation implements MeshDeformation {
	public float[] deform(String id,String role,int index,float cx,float cy,float cz,float[] v,float t,int stage){
		float x = v[0], y = v[1], z = v[2], phase = t * (.23F + (index % 5) * .013F) + index * 1.73F;
		boolean impact = stage == MeshStage.IMPACT, form = stage == MeshStage.FORM;
		float progress = Math.clamp(t / lifetime(id), 0, 1);
		float spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);

			if (id.equals("water_splash")) {
				float expand = .1F + .9F * SpellAtmosphereProfile.smooth(t / 12);
				x *= expand; z *= expand;
				if (role.equals("crown")) y = -.15F + (y + .15F) * (float) Math.sin(Math.min(1, t / 25) * Math.PI);
				if (role.equals("droplet")) { y += .65F * progress - 1.25F * progress * progress; x *= 1 + progress * .3F; z *= 1 + progress * .3F; }
				if (role.equals("ripple")) { x *= 1 + progress * .25F; z *= 1 + progress * .25F; }
			} else if (id.equals("waterfall")) {
				if (role.equals("droplet")) { float cycle = fraction(t * .065F + index * .097F); y = .46F - cycle; }
				else { x += .008F * (float) Math.sin(y * 14 + t * .35F + index); z += .022F * (float) Math.sin(y * 16 + x * 9 - t * .4F); }
				float grow = .01F + .99F * SpellAtmosphereProfile.smooth(t / 5); y = .5F - (.5F - y) * grow;
			} else if (impact) {
				float a = index * 2.39996F, radius = spread * (role.equals("droplet") ? 1.6F : .95F);
				x = (x - cx) * (1 + spread) + (float) Math.cos(a) * radius;
				y = y - cy + (float) Math.sin(a) * radius - progress * progress * .8F;
				z = (z - cz) * .4F;
			} else if (id.equals("flood_flush")) {
				if (role.equals("droplet")) { float c = fraction(t * .08F + index * .107F); y += c * .2F - c * c * .3F; z -= c * .3F; }
				else { float crest = Math.clamp(y + .5F, 0, 1); z += .035F * (float) Math.sin(x * 12 + t * .3F) * crest; y += .02F * (float) Math.sin(x * 18 - t * .3F) * crest; }
			} else {
				if (role.equals("droplet")) {
					float cycle = fraction(t * .075F + index * .137F);
					if (form) { float a = phase; x = x - cx + (float) Math.cos(a) * (.4F + cycle * .3F); y = y - cy + (float) Math.sin(a) * (.4F + cycle * .3F); z -= cz; }
					else z -= cycle * .8F;
				} else {
					float wave = .025F * (float) Math.sin(z * 11 - t * .4F + index); x *= 1 + wave; y *= 1 - wave;
				}
			}
		
		return new float[]{x,y,z};
	}
}
