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
	private final float baseSize;
	private SpellSpriteParticle(ClientLevel level,double x,double y,double z,double dx,double dy,double dz,SpriteSet sprites,String role) {
		super(level,x,y,z,sprites.first());
		frames=sprites;glyph=role.equals("glyph");
		lifetime=glyph?24:role.equals("gather")?7:role.equals("trail")?10:16;
		baseSize=glyph?0.085F:role.equals("gather")?0.048F:role.equals("trail")?0.075F:0.16F;
		quadSize=baseSize;hasPhysics=false;gravity=0;friction=glyph?1.0F:0.98F;
		xd=dx;yd=dy;zd=dz;
		// Preserve the eye-stream velocity sent by the server, including diagonal rise.
		if(glyph)setSprite(sprites.get(random));
		else setSpriteFromAge(sprites);
	}
	@Override public void tick() {
		super.tick();
		if(!glyph)setSpriteFromAge(frames);
	}
	@Override public void extract(QuadParticleRenderState state,Camera camera,float delta) {
		float progress=Math.clamp((age+delta)/lifetime,0,1);
		alpha=(float)Math.sin(progress*Math.PI)*(glyph?0.85F:0.8F);
		quadSize=baseSize*(glyph?.65F+.35F*Math.min(1,progress*5):0.6F+progress*0.6F);
		if(!glyph)setSprite(frames.get(Math.min(23,(int)(progress*24)),23));
		super.extract(state,camera,delta);
	}
	@Override protected int getLightCoords(float delta) { return 0xF000F0; }
	@Override protected Layer getLayer() { return Layer.TRANSLUCENT; }
	static void initialize() {
		var registry=ParticleProviderRegistry.getInstance();
		for(var entry:SpellParticles.types().entrySet()) {
			String id=entry.getKey();
			String role=id.equals("chant_glyph")?"glyph":id.substring(id.lastIndexOf('_')+1);
			registry.register(entry.getValue(),(ParticleProviderRegistry.PendingParticleProvider<net.minecraft.core.particles.SimpleParticleType>)sprites ->
					(type,level,x,y,z,dx,dy,dz,random)->new SpellSpriteParticle(level,x,y,z,dx,dy,dz,sprites,role));
		}
	}
}
