package com.example.examplemod.client.magic;
import com.example.examplemod.magic.domain.visual.SpellAtmosphereProfile;

import java.util.ArrayList;
import com.example.examplemod.magic.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

/** Bounded source-derived sprite timelines. No vanilla particle textures or orbiting chant. */
public final class SpellVisualAnimations {
	private static final ArrayList<Burst> ACTIVE=new ArrayList<>();
	private static Object lastLevel;
	private static final class Burst {
		final SpellDefinition spell; final Vec3 at,direction; int age;
		Burst(SpellVisualPayload p) { spell=SpellDefinition.find(p.spell());at=p.position();direction=p.direction(); }
	}
	public static void receive(SpellVisualPayload packet) {
		SpellVolumeEffects.receive(packet);
		var client=Minecraft.getInstance();
		if(client.level==null||SpellDefinition.find(packet.spell())==null)return;
		if(lastLevel!=client.level){ACTIVE.clear();lastLevel=client.level;}
		if(ACTIVE.size()>=32)ACTIVE.removeFirst();
		ACTIVE.add(new Burst(packet));
	}
	public static void tick(Minecraft client) {
		if(client.level==null||client.level!=lastLevel){ACTIVE.clear();lastLevel=client.level;}
		if(client.level==null||client.isPaused())return;
		if(client.player!=null&&client.level.getGameTime()%2==0) {
			int visited=0, windBudget=48;
			for(var entity:client.level.getEntities(client.player,client.player.getBoundingBox().inflate(24),e->e instanceof net.minecraft.world.entity.LivingEntity)) {
				if(++visited>96 || windBudget<=0)break;
				var living=(net.minecraft.world.entity.LivingEntity)entity;
				if(!living.getAttachedOrElse(MagicAttachments.WIND_BOUND_VISUAL,false))continue;
				double a=living.tickCount*.2,r=living.getBbWidth()*.6+.1;
				for(int i=0;i<6 && windBudget>0;i++,windBudget--) {
					var random=client.level.getRandom();
					double angle=a+random.nextDouble()*Math.PI*2, spread=r*(.75+random.nextDouble()*.5);
					client.level.addParticle(SpellParticles.accent(SpellDefinition.WIND_BIND),living.getX()+Math.cos(angle)*spread,living.getY()+random.nextDouble()*living.getBbHeight(),living.getZ()+Math.sin(angle)*spread,-Math.sin(angle)*.04,.01,Math.cos(angle)*.04);
				}
			}
		}
		SpellAtmosphere.tick(client);
		int budget=SpellParticleDensity.IMPACT_BUDGET;
		// New impacts take precedence over lingering smoke when many hits arrive together.
		ACTIVE.sort(java.util.Comparator.comparingInt(b->b.age));
		var it=ACTIVE.iterator();
		while(it.hasNext()) {
			var b=it.next();if(++b.age>SpellAtmosphereProfile.of(b.spell.id()).duration()){it.remove();continue;}
			if(b.at.distanceToSqr(client.gameRenderer.getMainCamera().position())>48*48)continue;
			var random=client.level.getRandom();
			boolean heal=b.spell.element().equals("healing");
			boolean field=b.spell==SpellDefinition.WATER_SPLASH||b.spell==SpellDefinition.ICICLE_FIELD;
			double radius=heal?.45:field?Math.min(6,b.age*.3):b.spell.areaDamage()?Math.min(2,b.age*.12):.2+b.age*.01;
			var profile=SpellAtmosphereProfile.of(b.spell.id());
			var detail=SpellParticleDensity.of(b.spell.rank());
			int count=SpellParticleDensity.impactCount(b.spell.rank(),b.age);
			for(int i=0;i<count&&budget>0;i++,budget--) {
				boolean wave=b.age==4 || b.age==8;
				double reach=Math.max(detail.radius()*.8,radius)*(wave?1.65:1);
				var offset=SpellParticleDensity.volume(random.nextDouble(),random.nextDouble(),random.nextDouble(),reach);
				var point=b.at.add(offset.x(),offset.y()*.8+(heal?b.age*.035:.12),offset.z());
				boolean smoke=b.age>2 && profile.dusty() && i%4==0;
				double speed=b.age==1?.16+random.nextDouble()*.12:wave?.1:b.age<12?.065:.022;
				double length=Math.sqrt(offset.x()*offset.x()+offset.y()*offset.y()+offset.z()*offset.z());
				double factor=speed/Math.max(.05,length);
				var type=smoke?SpellParticles.haze(b.spell):i%5==0?SpellParticles.burst(b.spell):SpellParticles.accent(b.spell);
				client.level.addParticle(type,point.x,point.y,point.z,offset.x()*factor,
						offset.y()*factor+(heal?.025:profile.buoyant()?.025:.008),offset.z()*factor);
			}
		}
	}
	private SpellVisualAnimations() { }
}
