package com.example.examplemod.client.magic;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;

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
			if(spell==SpellDefinition.SQUALL||spell==SpellDefinition.CUMULONIMBUS){budget=LocalRainAtmosphere.emit(client,entity,budget,interval);continue;}
			if(spell==SpellDefinition.ABSOLUTE_ZERO){int count=Math.max(4,SpellParticleDensity.of(spell.rank()).wake()/interval);for(int i=0;i<count&&budget>0;i++,budget--){var offset=SpellParticleDensity.volume(random.nextDouble(),random.nextDouble(),random.nextDouble(),16);var at=entity.position().add(offset.x(),offset.y(),offset.z());client.level.addParticle(i%3==0?SpellParticles.haze(spell):SpellParticles.accent(spell),at.x,at.y,at.z,offset.x()*.003,-.015,offset.z()*.003);}continue;}
			if(spell==SpellDefinition.INFERNO||spell==SpellDefinition.FLASH_OVER){
				int count=Math.max(4,SpellParticleDensity.of(spell.rank()).wake()/interval);
				for(int i=0;i<count&&budget>0;i++,budget--){double a=random.nextDouble()*Math.PI*2,rad=(spell==SpellDefinition.INFERNO?20:16)*Math.sqrt(random.nextDouble());var at=entity.position().add(Math.cos(a)*rad,random.nextDouble()*(spell==SpellDefinition.INFERNO?8:6),Math.sin(a)*rad);boolean smoke=random.nextInt(3)==0;client.level.addParticle(smoke?SpellParticles.haze(spell):SpellParticles.accent(spell),at.x,at.y,at.z,(random.nextDouble()-.5)*.03,.03+random.nextDouble()*.06,(random.nextDouble()-.5)*.03);}continue;
			}
			if(spell.execution()==SpellDefinition.Execution.WIND_FIELD){
				double radius=com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).radius(),height=com.example.examplemod.bootstrap.WindFieldComposition.policy(spell).height();
				int count=Math.max(4,SpellParticleDensity.of(spell.rank()).wake()*2/interval);
				for(int i=0;i<count&&budget>0;i++,budget--){
					double y=random.nextDouble()*height,angle=random.nextDouble()*Math.PI*2;
					double taper=(spell==SpellDefinition.TORNADO||spell==SpellDefinition.FLAME_TORNADO)?.25+.75*y/height:1;
					double rad=radius*taper*Math.sqrt(random.nextDouble()),x=Math.cos(angle)*rad,z=Math.sin(angle)*rad;
					var at=entity.position().add(x,y,z);
					if(at.distanceToSqr(camera)>48*48)continue;
					boolean haze=random.nextInt(3)==0;
					client.level.addParticle(haze?SpellParticles.haze(spell):SpellParticles.accent(spell),at.x,at.y,at.z,-Math.sin(angle)*.14-x*.004,.035+random.nextDouble()*.06,Math.cos(angle)*.14-z*.004);
				}
				continue;
			}
			boolean stationary=entity instanceof MagicAreaEntity || entity instanceof DirectionalSpellCarrier phase && phase.stationary();
			var velocity=entity.getDeltaMovement();
			var detail=SpellParticleDensity.of(spell.rank());
			int count=stationary?detail.wake()+4:detail.wake();
			count=Math.max(2,count/interval);
			for(int i=0;i<count && budget>0;i++,budget--) {
				double angle=random.nextDouble()*Math.PI*2;
				boolean blizzard=spell==SpellDefinition.BLIZZARD_STORM;
				double radius=blizzard?8:stationary?Math.max(.3,Math.min(2.5,entity.getBbWidth()*.65)):detail.radius()*.65;
				var offset=SpellParticleDensity.volume(random.nextDouble(),random.nextDouble(),random.nextDouble(),radius);
				Vec3 at=entity.position().subtract(velocity.scale(stationary?0:random.nextDouble()));
				at=at.add(offset.x(),offset.y()+(blizzard?1:stationary?random.nextDouble()*Math.min(2,entity.getBbHeight()):0),offset.z());
				boolean haze=profile.dusty() && random.nextInt(4)==0;
				client.level.addParticle(haze?SpellParticles.haze(spell):SpellParticles.accent(spell),at.x,at.y,at.z,
						blizzard?offset.x()*.045:-velocity.x*.025+Math.cos(angle)*(.02+random.nextDouble()*.025),(profile.buoyant()?.025:.004)+(random.nextDouble()-.5)*.03,blizzard?offset.z()*.045:-velocity.z*.025+Math.sin(angle)*(.02+random.nextDouble()*.025));
			}
		}
	}
	private static SpellDefinition spell(Entity entity) {
		if(entity instanceof WaterBallEntity e)return e.spell();
		if(entity instanceof SweptSpellProjectile e)return e.spell();
		if(entity instanceof DirectionalSpellCarrier e)return e.spell();
		if(entity instanceof MagicAreaEntity e)return e.spell();
		if(entity instanceof FlameSliceEntity)return SpellDefinition.FLAME_SLICE;
		return null;
	}
	private SpellAtmosphere() { }
}
