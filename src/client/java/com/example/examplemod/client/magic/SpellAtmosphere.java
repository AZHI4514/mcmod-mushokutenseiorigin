package com.example.examplemod.client.magic;

import com.example.examplemod.magic.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/** Local-only atmosphere: distance LOD, bounded emitters and interpolated projectile wakes. */
final class SpellAtmosphere {
	static void tick(Minecraft client) {
		if(client.player==null || client.level==null)return;
		int budget=SpellParticleDensity.WAKE_BUDGET, emitters=0;
		var camera=client.gameRenderer.getMainCamera().position();
		for(var entity:client.level.getEntities(client.player,client.player.getBoundingBox().inflate(48),e->spell(e)!=null)) {
			if(++emitters>64 || budget<=0)break;
			var spell=spell(entity);
			double distance=entity.position().distanceToSqr(camera);
			int interval=distance>32*32?4:distance>16*16?2:1;
			if(Math.floorMod(entity.tickCount+entity.getId(),interval)!=0)continue;
			var profile=SpellAtmosphereProfile.of(spell.id());
			var random=client.level.getRandom();
			boolean stationary=entity instanceof MagicAreaEntity || entity instanceof PhaseTwoEntity phase && phase.stationary();
			var velocity=entity.getDeltaMovement();
			var detail=SpellParticleDensity.of(spell.rank());
			int count=stationary?detail.wake()+4:detail.wake();
			count=Math.max(2,count/interval);
			for(int i=0;i<count && budget>0;i++,budget--) {
				double angle=random.nextDouble()*Math.PI*2;
				double radius=stationary?Math.max(.3,Math.min(2.5,entity.getBbWidth()*.65)):detail.radius()*.65;
				var offset=SpellParticleDensity.volume(random.nextDouble(),random.nextDouble(),random.nextDouble(),radius);
				Vec3 at=entity.position().subtract(velocity.scale(stationary?0:random.nextDouble()));
				at=at.add(offset.x(),offset.y()+(stationary?random.nextDouble()*Math.min(2,entity.getBbHeight()):0),offset.z());
				boolean haze=profile.dusty() && random.nextInt(4)==0;
				client.level.addParticle(haze?SpellParticles.haze(spell):SpellParticles.accent(spell),at.x,at.y,at.z,
						-velocity.x*.025+Math.cos(angle)*(.02+random.nextDouble()*.025),(profile.buoyant()?.025:.004)+(random.nextDouble()-.5)*.03,-velocity.z*.025+Math.sin(angle)*(.02+random.nextDouble()*.025));
			}
		}
	}
	private static SpellDefinition spell(Entity entity) {
		if(entity instanceof WaterBallEntity e)return e.spell();
		if(entity instanceof IntermediateProjectile e)return e.spell();
		if(entity instanceof PhaseTwoEntity e)return e.spell();
		if(entity instanceof MagicAreaEntity e)return e.spell();
		if(entity instanceof FlameSliceEntity)return SpellDefinition.FLAME_SLICE;
		return null;
	}
	private SpellAtmosphere() { }
}
