package com.example.examplemod.client.magic;

import com.example.examplemod.magic.SpellParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;

/** Own flipbook sprites, with partial-tick motion and gradual growth/fade. */
final class SpellSpriteParticle extends SingleQuadParticle {
	private final SpriteSet frames;
	private final boolean glyph;
	private final boolean haze, accent;
	private final float drift;
	private final float baseSize;
	private SpellSpriteParticle(ClientLevel level,double x,double y,double z,double dx,double dy,double dz,SpriteSet sprites,String role,String spellId) {
		super(level,x,y,z,sprites.first());
		frames=sprites;glyph=role.equals("glyph");haze=role.equals("haze");accent=role.equals("accent");
		drift=random.nextFloat()*(float)(Math.PI*2);
		var profile=glyph?null:SpellAtmosphereProfile.of(spellId);
		lifetime=haze?30+random.nextInt(15):accent?14+random.nextInt(9):glyph?24:role.equals("gather")?12+random.nextInt(5):role.equals("trail")?10:16;
		baseSize=haze?(.24F+random.nextFloat()*.14F)*profile.size():accent?(.11F+random.nextFloat()*.08F)*profile.size():glyph?0.085F:role.equals("gather")?0.048F:role.equals("trail")?0.075F:0.16F;
		quadSize=baseSize;hasPhysics=false;gravity=0;friction=glyph?1.0F:0.98F;
		xd=dx;yd=dy;zd=dz;
		if(haze||accent) {
			friction=haze?.94F:.97F;
			gravity=profile.family().equals("earth")?.06F:profile.family().equals("water")||profile.family().equals("ice")?.025F:0;
			if(profile.buoyant())yd+=haze?.018:.01;
		}
		// Preserve the eye-stream velocity sent by the server, including diagonal rise.
		if(glyph)setSprite(sprites.get(random));
		else setSpriteFromAge(sprites);
	}
	@Override public void tick() {
		super.tick();
		if(haze) {
			xd+=Math.sin(age*.13+drift)*.0006;
			zd+=Math.cos(age*.11+drift)*.0006;
		}
		if(!glyph)setSpriteFromAge(frames);
	}
	@Override public void extract(QuadParticleRenderState state,Camera camera,float delta) {
		float progress=Math.clamp((age+delta)/lifetime,0,1);
		alpha=haze||accent?SpellAtmosphereProfile.envelope(progress)*(haze?.7F:1.7F):(float)Math.sin(progress*Math.PI)*(glyph?0.85F:0.8F);
		quadSize=baseSize*(haze?.55F+1.35F*SpellAtmosphereProfile.smooth(progress):accent?.65F+.65F*SpellAtmosphereProfile.smooth(progress):glyph?.65F+.35F*Math.min(1,progress*5):0.6F+progress*0.6F);
		if(!glyph)setSprite(frames.get(Math.min(23,(int)(progress*24)),23));
		super.extract(state,camera,delta);
	}
	@Override protected int getLightCoords(float delta) { return haze?super.getLightCoords(delta):0xF000F0; }
	@Override protected Layer getLayer() { return Layer.TRANSLUCENT; }
	static void initialize() {
		var registry=ParticleProviderRegistry.getInstance();
		for(var entry:SpellParticles.types().entrySet()) {
			String id=entry.getKey();
			String role=id.equals("chant_glyph")?"glyph":id.substring(id.lastIndexOf('_')+1);
			registry.register(entry.getValue(),(ParticleProviderRegistry.PendingParticleProvider<net.minecraft.core.particles.SimpleParticleType>)sprites ->
					(type,level,x,y,z,dx,dy,dz,random)->new SpellSpriteParticle(level,x,y,z,dx,dy,dz,sprites,role,id.equals("chant_glyph")?"":id.substring(0,id.lastIndexOf('_'))));
		}
	}
}
