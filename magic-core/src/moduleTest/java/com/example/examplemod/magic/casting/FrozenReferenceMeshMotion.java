package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.visual.motion.MeshMotion;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;
import static com.example.examplemod.magic.domain.visual.motion.MeshStage.*;
final class FrozenReferenceMeshMotion {
	public static float alpha(String id, String role, int index, float age, int stage) {
		if(id.equals("blast")||id.equals("bora_blast"))return Math.clamp(1-age/20,0,1)*.65F;
		if(id.equals("magma_gush"))return role.equals("crust")?.85F:Math.clamp((161-age)/12,0,1)*.85F;
		if(id.equals("flame_tornado")||id.equals("violent_storm")||id.equals("tornado")||id.equals("vacuum_storm")||id.equals("sky_tempest"))return .45F+.2F*(float)Math.pow(Math.sin(age*.12F+index),2);
		if(id.equals("flame_tornado"))return Math.clamp(age/8,0,1)*Math.clamp((240-age)/15,0,1)*(.5F+.2F*(float)Math.pow(Math.sin(age*.14F+index),2));
		if(id.equals("absolute_zero"))return Math.clamp((160-age)/20,0,1)*(role.equals("coldstream")?.3F:Math.clamp(1-age/30,0,1));
		if(id.equals("inferno"))return Math.clamp(age/6,0,1)*Math.clamp((401-age)/20,0,1)*(role.equals("smoke")?.35F:.85F);
		if(id.equals("flash_over"))return Math.clamp(age/5,0,1)*Math.clamp((160-age)/16,0,1)*(role.equals("smoke")?.35F:.8F);
		if(id.equals("quagmire"))return .9F;
		if(id.equals("earth_hedgehog"))return age<0?0:Math.clamp((400-age)/12,0,1);
		if(id.equals("sonic_blast"))return Math.clamp(1-age/18,0,1)*.8F;
		if(id.equals("frost_nova"))return Math.clamp(1-age/24,0,1)*(role.equals("coldstream")?.28F:.9F);
		if(id.equals("lightning")||id.equals("electric")){float fade=stage==IMPACT?Math.clamp(1-age/12,0,1):1;return fade*(.25F+.75F*(float)Math.pow(Math.sin(age*1.9F+index*2.4F),2));}
		if(id.equals("cumulonimbus"))return role.equals("bolt")?(stage==IMPACT?Math.clamp(1-age/8,0,1):0):(stage==IMPACT?0:.65F);
		if(id.equals("tornado_impact"))return stage==IMPACT?Math.clamp(1-age/20,0,1):.65F+.3F*(float)Math.sin(age*.25F+index)* (float)Math.sin(age*.25F+index);
		if(id.equals("exodus_flame")) return alpha("fireball",role,index,age,stage)*(role.equals("combustion")?.65F:1);
		if (id.equals("blizzard_storm")) return role.equals("coldstream") ? .22F : .9F;
		if (MeshMotion.handles(id)) return MeshMotion.alpha(id, role, index, age, stage);
		if (stage != IMPACT) return role.equals("fracture") || role.equals("splash") ? 0 : 1;
		float end = id.equals("fireball") ? 14 : 20;
		float fade = Math.clamp(1 - age / end, 0, 1);
		if (role.equals("liquid")) return Math.clamp(1 - age / 4, 0, 1);
		if (role.equals("heart") || role.equals("combustion")) return Math.clamp(1 - age / 5, 0, 1);
		if (role.equals("icecore") || role.equals("rock")) return Math.clamp(1 - age / 3, 0, 1);
		return fade * fade;
	}
	public static float[] deform(String id, String role,int index,float cx,float cy,float cz,float[] v, float t, int stage) {
		if(id.equals("blast")||id.equals("bora_blast")){float grow=.1F+.9F*Math.clamp(t/9,0,1);return new float[]{v[0]*grow,v[1]*grow,v[2]*grow};}
		if(id.equals("magma_gush")){if(role.equals("crust"))return new float[]{v[0],v[1],v[2]};float pulse=.75F+.25F*(float)Math.sin(t*.27F+index);float growth=Math.clamp(t/6,0,1);float y=-.5F+(v[1]+.5F)*pulse*growth;if(role.equals("droplet")){float cycle=(t*.045F+index*.17F)%1;y=-.4F+1.7F*cycle*(1-cycle);}return new float[]{v[0],y,v[2]};}
		if(id.equals("flame_tornado")||id.equals("violent_storm")||id.equals("tornado")||id.equals("vacuum_storm")||id.equals("sky_tempest")){float a=t*.11F+index*.07F;return new float[]{v[0]*(float)Math.cos(a)-v[2]*(float)Math.sin(a),v[1]+.015F*(float)Math.sin(t*.1F+index),v[0]*(float)Math.sin(a)+v[2]*(float)Math.cos(a)};}
		if(id.equals("inferno")||id.equals("flash_over")){float cycle=(t*.045F+index*.137F)%1;float sway=.012F*(float)Math.sin(t*.3F+index+v[1]*8);float y=role.equals("front")?v[1]:role.equals("smoke")?v[1]+cycle*.2F:-.5F+(v[1]+.5F)*(.6F+.4F*(float)Math.sin(cycle*Math.PI));return new float[]{v[0]+sway,y,v[2]-sway};}
		if(id.equals("quagmire")){float a=.015F*(float)Math.sin(t*.08F+index);return new float[]{v[0],v[1]+a,v[2]};}
		if(id.equals("earth_hedgehog")){float rise=1-(float)Math.pow(1-Math.clamp(t/5,0,1),3);float kick=role.equals("rubble")?(float)Math.sin(Math.clamp(t/14,0,1)*Math.PI)*.18F:0;return new float[]{v[0],-.5F+(v[1]+.5F)*rise+kick,v[2]};}
		if(id.equals("lightning")||id.equals("electric")){float wave=.006F*(float)Math.sin(Math.floor(t*1.5F)*3+index+v[1]*37);return new float[]{v[0]+wave,v[1],v[2]-wave};}
		if(id.equals("sonic_blast")){float grow=.12F+.88F*Math.clamp(t/8,0,1);return new float[]{v[0]*grow,v[1]*grow,v[2]*grow};}
		if(id.equals("absolute_zero")){float g=.05F+.95F*(1-(float)Math.pow(1-Math.clamp(t/14,0,1),3));return new float[]{v[0]*g,v[1]*g,v[2]*g};}
		if(id.equals("frost_nova")){float growth=.08F+.92F*(1-(float)Math.pow(1-Math.clamp(t/12,0,1),3));return new float[]{v[0]*growth,v[1]*growth,v[2]*growth};}
		if(id.equals("cumulonimbus")){float wave=.012F*(float)Math.sin(t*.035F+index);return new float[]{v[0]+wave,v[1]+wave*.3F,v[2]+wave};}
		if(id.equals("tornado_impact")){float a=t*.24F+v[2]*2;float grow=stage==IMPACT?1+t*.09F:1;return new float[]{(v[0]*(float)Math.cos(a)-v[1]*(float)Math.sin(a))*grow,(v[0]*(float)Math.sin(a)+v[1]*(float)Math.cos(a))*grow,v[2]*(stage==IMPACT?.4F:1)};}
		if(id.equals("exodus_flame"))return deform("fireball",role,index,cx,cy,cz,v,t,stage);
		if (id.equals("blizzard_storm")) {
			float cycle = (t * .028F + index * .137F) % 1;
			float radius = .15F + cycle * .9F;
			float a = t * .035F + index * 2.39996F;
			if (role.equals("icegravel")) {
				float length = Math.max(.01F,(float)Math.sqrt(cx*cx+cy*cy+cz*cz));
				return new float[]{(v[0]-cx)*.7F+cx/length*radius,(v[1]-cy)*.7F+cy/length*radius,(v[2]-cz)*.7F+cz/length*radius};
			}
			return new float[]{v[0]*(float)Math.cos(a)-v[2]*(float)Math.sin(a),v[1]+.04F*(float)Math.sin(t*.13F+index),v[0]*(float)Math.sin(a)+v[2]*(float)Math.cos(a)};
		}
		if (MeshMotion.handles(id)) return MeshMotion.deform(id, role, index, cx, cy, cz, v, t, stage);
		float x = v[0], y = v[1], z = v[2];
		float phase = t * .24F + index * 1.73F;
		if (stage == IMPACT) {
			float progress = Math.clamp(t / 20, 0, 1), spread = 1 - (1 - progress) * (1 - progress) * (1 - progress);
			if (id.equals("water_ball")) {
				if (role.equals("liquid")) { x *= 1 + t * .45F; y *= 1 + t * .45F; z *= .2F; }
				else {
					float a = index * 2.39996F;
					float radius = spread * (role.equals("drop") ? 1.7F : 1.15F);
					x = (x - cx) * (role.equals("drop") ? 1.4F : 1 + spread) + (float) Math.cos(a) * radius;
					y = (y - cy) + (float) Math.sin(a) * radius - .9F * progress * progress;
					z = (z - cz) * .5F - spread * .3F;
				}
			} else if (id.equals("fireball")) {
				if (role.equals("heart") || role.equals("combustion")) { float s = 1 + t * .3F; x *= s; y *= s; z *= s * .45F; }
				else {
					float a = index * 2.39996F;
					x += (float) Math.cos(a) * spread * 1.2F;
					y += (float) Math.sin(a) * spread * 1.2F + progress * .6F;
					z *= .45F;
				}
			} else {
				float length = (float) Math.sqrt(cx * cx + cy * cy + cz * cz);
				float force = id.equals("ice_smash") ? 1.1F : .8F;
				if (length > .05F) {
					float spin = t * (.08F + index % 3 * .03F), dx = x - cx, dy = y - cy;
					x = dx * (float) Math.cos(spin) - dy * (float) Math.sin(spin) + cx * (1 + spread * force / length);
					y = dx * (float) Math.sin(spin) + dy * (float) Math.cos(spin) + cy * (1 + spread * force / length) - progress * progress * 1.3F;
					z += cz * spread * force / length;
				}
			}
			return new float[] { x, y, z };
		}
		switch (id) {
			case "water_ball" -> {
				if (role.equals("drop")) {
					float cycle = (t * .055F + index * .127F) % 1;
					if (stage == FORM) {
						float a = phase; float radius = .8F - cycle * .4F;
						x = x - cx + (float) Math.cos(a) * radius; y = y - cy + (float) Math.sin(a) * radius; z = z - cz;
					} else { z = z - cz - .45F - cycle * .8F; x += .05F * (float) Math.sin(phase); }
				} else {
					float wave = .025F * (float) Math.sin(y * 10 + z * 7 + t * .3F) + .018F * (float) Math.sin(x * 12 - t * .21F);
					x *= 1 + wave; y *= 1 - wave * .7F; z *= 1 + wave;
				}
			}
			case "fireball" -> {
				float wave = (float) Math.sin(t * .55F - z * 9 + index * 2.4F);
				if (role.equals("tongue")) {
					float tail = Math.clamp((.2F - z) / 1.6F, 0, 1);
					x += wave * .07F * tail; y += (float) Math.cos(phase + z * 6) * .06F * tail;
					z *= 1 + wave * .16F;
					// Tail stays on -Z, toward the caster, during preparation too.
				} else if (role.equals("spark")) {
					float cycle = (t * .08F + index * .137F) % 1;
					y += stage == FORM ? cycle * .8F : .08F * (float) Math.sin(phase); z -= stage == FORM ? 0 : cycle;
				} else { float pulse = 1 + .045F * wave; x *= pulse; y *= pulse; z *= pulse; }
			}
			case "ice_smash" -> {
				if (stage == FORM && role.equals("crystal")) {
					float grow = .01F + .99F * SpellAtmosphereProfile.smooth((t - index * .18F) / 7);
					x *= grow; y *= grow; z *= grow;
				}
			}
			case "rock_bullet" -> {
				// Rigid solid. The small roll is not the liquid wobble used for water.
				float angle = t * .055F, oldX = x;
				x = oldX * (float) Math.cos(angle) - y * (float) Math.sin(angle); y = oldX * (float) Math.sin(angle) + y * (float) Math.cos(angle);
				if (role.equals("chip")) z -= ((t * .06F + index * .13F) % 1) * .35F;
			}
			case "water_shield" -> {
				if (role.equals("spray")) {
					float cycle = (t * .045F + index * .061F) % 1;
					x *= 1 + cycle * .2F; y += cycle * .12F - cycle * cycle * .2F; z -= cycle * .1F;
				} else {
					z += .016F * (float) Math.sin(y * 21 - t * .35F + x * 9) + .008F * (float) Math.sin(x * 34 + t * .19F);
					if (role.equals("current")) { y += .013F * (float) Math.sin(t * .21F + x * 11 + index); }
				}
				float emerge = Math.clamp(t / 6, .01F, 1); y *= emerge;
			}
			default -> { }
		}
		return new float[] { x, y, z };
	}
}
