package com.example.examplemod.magic.domain.visual.mesh;
import java.util.*;
/** Pure deterministic geometry, selected by explicit spell-to-material Strategies. */
public final class LayeredPixelMesh {
	@FunctionalInterface private interface Geometry { void build(List<PixelFace> faces,String id,float age,float opacity,int color); }
	private record Profile(String texture,int color,Geometry geometry) {}
	private static final Map<String,Profile> PROFILES=Map.ofEntries(
		Map.entry("magic_core", new Profile("rune", 0xB18AEA, LayeredPixelMesh::core)),
		Map.entry("squall", new Profile("rune", 0xB18AEA, LayeredPixelMesh::rain)),
		Map.entry("ocean_freeze", new Profile("rune", 0xB18AEA, LayeredPixelMesh::ocean)),
		Map.entry("flame_storm", new Profile("fire", 0xFFB13D, LayeredPixelMesh::fire)),
		Map.entry("apocalypse_flame", new Profile("fire", 0xFFB13D, LayeredPixelMesh::fire)),
		Map.entry("earthquake", new Profile("earth", 0x967456, LayeredPixelMesh::earth)),
		Map.entry("terra_fall", new Profile("earth", 0x967456, LayeredPixelMesh::earth)),
		Map.entry("continental_sink", new Profile("earth", 0x967456, LayeredPixelMesh::earth)),
		Map.entry("sand_cloud", new Profile("fog", 0xBFA57C, LayeredPixelMesh::fog)),
		Map.entry("dust_storm", new Profile("fog", 0xBFA57C, LayeredPixelMesh::fog)),
		Map.entry("sandstorm", new Profile("fog", 0xBFA57C, LayeredPixelMesh::fog)),
		Map.entry("deep_mist", new Profile("fog", 0xDFE9ED, LayeredPixelMesh::fog)),
		Map.entry("mud_rain", new Profile("fog", 0xBFA57C, LayeredPixelMesh::fog)),
		Map.entry("antidote_iii", new Profile("rune", 0x8AEBC0, LayeredPixelMesh::runes)),
		Map.entry("exorcist_ray", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("exorcist_burst", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("exorcist_judge", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("holy_ray", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("sacred_nova", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("divine_judge", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("god_purge", new Profile("rune", 0xD5F6FF, LayeredPixelMesh::holy)),
		Map.entry("atmosphere_dominion", new Profile("fog", 0xBFA57C, LayeredPixelMesh::fog)),
		Map.entry("gravity_magic", new Profile("rune", 0xB18AEA, LayeredPixelMesh::seal)),
		Map.entry("nuclear_explosion", new Profile("fire", 0xFFB13D, LayeredPixelMesh::nuclear)),
		Map.entry("dual_shield", new Profile("rune", 0xB18AEA, LayeredPixelMesh::seal)),
		Map.entry("spirit_sealing_barrier", new Profile("rune", 0xB18AEA, LayeredPixelMesh::seal)),
		Map.entry("confinement_barrier", new Profile("rune", 0xB18AEA, LayeredPixelMesh::seal)),
		Map.entry("fortress_barrier", new Profile("rune", 0xB18AEA, LayeredPixelMesh::seal)),
		Map.entry("demon_god_sealing_barrier", new Profile("rune", 0xB18AEA, LayeredPixelMesh::seal)),
		Map.entry("disturb_magic", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("divination_magic", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("howling", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("hypnosis_magic", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("monster_summon", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("spirit_summon", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("lamplight_spirits", new Profile("rune", 0xFFD486, LayeredPixelMesh::lamplight)),
		Map.entry("necromancy", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("saint_dragon_battle_aura", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("communication_magic", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)),
		Map.entry("teleportation_magic", new Profile("rune", 0xB18AEA, LayeredPixelMesh::runes)));
	public static boolean handles(String id){return PROFILES.containsKey(id);}
	public static String texture(String id){var p=PROFILES.get(id);return p==null?"rune":p.texture;}
	private static float hash(int n){int x=n*374761393+668265263;x=(x^(x>>>13))*1274126177;return (x&0x7fffffff)/(float)Integer.MAX_VALUE;}
	private static float ease(float t){t=Math.clamp(t,0,1);return t*t*(3-2*t);}
	public static List<PixelFace> build(String id,float age,float opacity,boolean impact){
		var profile=PROFILES.get(id);
		if(profile==null||opacity<=0)return List.of();
		float fade=impact?ease(age/2)*(1-ease((age-8)/12)):ease(age/10);opacity*=fade;if(opacity<.003F)return List.of();
		var faces=new ArrayList<PixelFace>();
		profile.geometry.build(faces,id,age,opacity,profile.color);
		return List.copyOf(faces);
	}
	private static void nuclear(List<PixelFace> faces,String id,float age,float opacity,int color){
			if(age<100){
				for(int i=0;i<7;i++)ring(faces,.10F+i*.026F,(float)Math.sin(age*.04F+i)*.06F,age*(i%2==0?.05F:-.036F)+i,.008F,i%3==0?0xFFF5CD:0xFFAE41,opacity*.7F,i%2==0);
				orb(faces,.055F+(float)Math.sin(age*.3F)*.006F,0xFFF5D5,opacity*.9F);
			}else{
				float t=Math.clamp((age-100)/40,0,1),growth=ease(t*2),fadeOut=1-ease((t-.55F)/.45F);
				orb(faces,.12F+growth*.29F,0xFFE4A2,opacity*fadeOut*.65F);
				for(int i=0;i<52;i++){float a=hash(i*9)*6.28F,r=(.05F+hash(i+7)*.34F)*growth,y=-.32F+t*.42F+hash(i+22)*.20F;
					cube(faces,(float)Math.cos(a)*r,y,(float)Math.sin(a)*r,.04F+hash(i+13)*.065F,i%3==0?0xFFF2C8:i%3==1?0xFFB047:0xD85724,opacity*fadeOut*.65F);
					if(i%2==0)cube(faces,(float)Math.cos(a)*r,y+.15F,(float)Math.sin(a)*r,.07F+t*.07F,0x655B59,opacity*(.15F+t*.35F)*fadeOut);
				}
				for(int i=0;i<4;i++)ring(faces,Math.min(.58F,(t+i*.09F)*.8F),-.4F+i*.015F,0,.008F,0xFFE6B0,opacity*fadeOut*(1-t)*.85F,false);
			}

		
	}
	private static void fire(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<48;i++){
				float x=hash(i*7)-.5F,z=hash(i*7+2)-.5F,cycle=(age*.034F+hash(i*7+3))%1,top=.25F+hash(i*7+4)*.75F;
				for(int k=0;k<8;k++){float u=k/8F,v=(k+1)/8F,base=.025F+hash(i)*.025F,w=(1-u)*base,w2=(1-v)*base;float s1=(float)Math.sin(age*.16+i+u*5)*.055F*u,s2=(float)Math.sin(age*.16+i+v*5)*.055F*v;float breath=.72F+.28F*(float)Math.sin(age*.19F+i);
					float y1=-.5F+u*top*breath,y2=-.5F+v*top*breath;
					quad(faces,new float[][]{{x+s1-w,y1,z},{x+s1+w,y1,z},{x+s2+w2,y2,z+.012F},{x+s2-w2,y2,z+.012F}},u<.25F?0xFFE2A0:color,opacity*(.35F+.65F*(1-cycle))*.85F);
					quad(faces,new float[][]{{x,y1,z+s1-w},{x,y1,z+s1+w},{x+.012F,y2,z+s2+w2},{x+.012F,y2,z+s2-w2}},color,opacity*(1-cycle)*.6F);
				}
				if(i%2==0)cube(faces,x,-.1F+cycle*.7F,z,.035F+cycle*.10F,0x514C59,opacity*(1-cycle)*.18F);
			}
			if(id.equals("nuclear_explosion"))for(int i=0;i<5;i++)ring(faces,.1F+i*.075F,-.1F+i*.05F,age*.025F+i,.015F,0xF1C0FF,opacity*.6F,false);
		
	}
	private static void core(List<PixelFace> faces,String id,float age,float opacity,int color){
			float bob=(float)Math.sin(age*.07F)*.055F;
			cube(faces,0,bob,0,.21F,0xD2BEFF,opacity*.85F);
			for(int i=0;i<6;i++){float a=age*.025F+i*6.28F/6;cube(faces,(float)Math.cos(a)*.28F,bob+(float)Math.sin(age*.04F+i)*.16F,(float)Math.sin(a)*.28F,.065F,0x9B75DA,opacity*.9F);}
			for(int i=0;i<3;i++)ring(faces,.25F+i*.06F,bob,age*(i%2==0?.02F:-.016F)+i,.009F,0xE0CAFF,opacity*.8F,i>0);
		
	}
	private static void lamplight(List<PixelFace> faces,String id,float age,float opacity,int color){
			orb(faces,.15F,0xFFE3AB,opacity);for(int i=0;i<3;i++)ring(faces,.24F+i*.07F,0,age*.025F+i,.012F,0xFFD591,opacity*.6F,i%2==0);
		
	}
	private static void fog(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<56;i++){float phase=age*.008F+hash(i)*6.28F;float r=(float)Math.sqrt(hash(i+99))*.46F;
				float x=(float)Math.cos(phase)*r,z=(float)Math.sin(phase)*r,y=(hash(i+48)+age*.0018F)%1-.5F;
				cube(faces,x,y,z,.10F+hash(i+8)*.15F,color,opacity*(.08F+.08F*(float)Math.sin(phase*2)* (float)Math.sin(phase*2)));
			}
		
	}
	private static void earth(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<45;i++){float x=hash(i*3)-.5F,z=hash(i*3+1)-.5F,cycle=(age*.035F+hash(i*3+2))%1;
				float y=id.equals("terra_fall")?.5F-cycle: -.5F+(float)Math.sin(cycle*Math.PI)*.12F;
				cube(faces,x,y,z,.018F+hash(i+7)*.045F,color,opacity*(1-cycle)*.85F);
			}
			for(int i=0;i<4;i++)ring(faces,((age*.012F+i*.25F)%1)*.5F,-.48F,0,.008F,0xD1BA8F,opacity*.4F,false);
		
	}
	private static void seal(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<7;i++){
				float y=-.45F+i*.15F,r=id.equals("fortress_barrier")?(float)Math.sqrt(Math.max(0,.25-(y+.5F)*(y+.5F)*.25)):.47F;
				ring(faces,r,y,age*(i%2==0?.012F:-.009F)+i,.006F,color,opacity*.7F,false);
			}
			for(int i=0;i<5;i++)ring(faces,.48F,0,age*.008F+i*(float)Math.PI/5,.004F,color,opacity*.42F,true);
		
	}
	private static void holy(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<12;i++){float a=hash(i)*6.28F,cycle=(age*.05F+hash(i+14))%1,r=.035F+hash(i+55)*.12F;
				float x=(float)Math.cos(a)*r,z=(float)Math.sin(a)*r;
				quad(faces,new float[][]{{x-.008F,-.5F,z},{x+.008F,-.5F,z},{x+.004F,.5F,z},{x-.004F,.5F,z}},color,opacity*(1-cycle)*.8F);
			}
			for(int i=0;i<4;i++)ring(faces,.1F+i*.1F,-.45F+i*.02F,age*.015F+i,.009F,color,opacity*.8F,false);
		
	}
	private static void ocean(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<5;i++)ring(faces,Math.min(.5F,age*.003125F)-i*.008F,-.45F+i*.015F,age*.002F,.006F,0xA5EAFF,opacity*.65F,false);
		
	}
	private static void rain(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<64;i++){float x=hash(i*3)-.5F,z=hash(i*3+1)-.5F,y=.5F-(age*.065F+hash(i*3+2))%1;quad(faces,new float[][]{{x-.001F,y,z},{x+.001F,y,z},{x+.002F,y+.045F,z},{x,y+.045F,z}},0xA3CFEB,opacity*.4F);}
		
	}
	private static void runes(List<PixelFace> faces,String id,float age,float opacity,int color){
			for(int i=0;i<4;i++)ring(faces,.22F+i*.065F,-.3F+i*.2F,age*(i%2==0?.025F:-.018F),.012F,color,opacity*.65F,i%2==1);
			for(int i=0;i<24;i++){float a=hash(i)*6.28F+age*.03F,r=.15F+hash(i+12)*.3F,y=(hash(i+28)+age*.02F)%1-.5F;cube(faces,(float)Math.cos(a)*r,y,(float)Math.sin(a)*r,.008F+hash(i+44)*.016F,color,opacity*.8F);}
		
	}
	private static void quad(List<PixelFace> out,float[][] p,int color,float alpha){if(alpha<1F/255)return;out.add(new PixelFace(p,(Math.clamp((int)(alpha*255),0,255)<<24)|color));}
	private static void ring(List<PixelFace> out,float radius,float y,float phase,float thickness,int color,float alpha,boolean vertical){
		if(radius<=0)return;for(int i=0;i<64;i++){if(i%8==7)continue;float a=i*(float)Math.PI/32+phase,b=(i+1)*(float)Math.PI/32+phase;float[][] q=new float[4][3];float[] angles={a,b,b,a},radii={radius-thickness,radius-thickness,radius+thickness,radius+thickness};
			for(int j=0;j<4;j++){float x=(float)Math.cos(angles[j])*radii[j],z=(float)Math.sin(angles[j])*radii[j];q[j]=vertical?new float[]{x,z*(float)Math.cos(phase),z*(float)Math.sin(phase)}:new float[]{x,y,z};}quad(out,q,color,alpha);
		}
	}
	private static void orb(List<PixelFace> out,float radius,int color,float alpha){for(int row=0;row<8;row++)for(int col=0;col<16;col++){float[][] q=new float[4][3];int[] dx={0,1,1,0},dy={0,0,1,1};for(int j=0;j<4;j++){double theta=(row+dy[j])*Math.PI/8,phi=(col+dx[j])*Math.PI/8;q[j]=new float[]{(float)(Math.sin(theta)*Math.cos(phi)*radius),(float)Math.cos(theta)*radius,(float)(Math.sin(theta)*Math.sin(phi)*radius)};}quad(out,q,color,alpha);}}
	private static void cube(List<PixelFace> out,float x,float y,float z,float size,int color,float alpha){float s=size/2;float[][] p={{x-s,y-s,z-s},{x+s,y-s,z-s},{x+s,y+s,z-s},{x-s,y+s,z-s},{x-s,y-s,z+s},{x+s,y-s,z+s},{x+s,y+s,z+s},{x-s,y+s,z+s}};for(int[] f:new int[][]{{0,1,2,3},{5,4,7,6},{4,0,3,7},{1,5,6,2},{3,2,6,7},{4,5,1,0}})quad(out,new float[][]{p[f[0]],p[f[1]],p[f[2]],p[f[3]]},color,alpha);}
	private LayeredPixelMesh(){}
}
