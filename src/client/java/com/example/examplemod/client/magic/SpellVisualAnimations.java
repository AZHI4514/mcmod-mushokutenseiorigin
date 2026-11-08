package com.example.examplemod.client.magic;

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
		if(client.player!=null&&client.level.getGameTime()%6==0) {
			int visited=0;
			for(var entity:client.level.getEntities(client.player,client.player.getBoundingBox().inflate(24),e->e instanceof net.minecraft.world.entity.LivingEntity)) {
				if(++visited>96)break;
				var living=(net.minecraft.world.entity.LivingEntity)entity;
				if(!living.getAttachedOrElse(MagicAttachments.WIND_BOUND_VISUAL,false))continue;
				double a=living.tickCount*.2,r=living.getBbWidth()*.6+.1;
				client.level.addParticle(SpellParticles.trail(SpellDefinition.WIND_BIND),living.getX()+Math.cos(a)*r,living.getY()+living.getBbHeight()*.6,living.getZ()+Math.sin(a)*r,0,.01,0);
			}
		}
		int budget=32;
		var it=ACTIVE.iterator();
		while(it.hasNext()) {
			var b=it.next();if(++b.age>24){it.remove();continue;}
			if(b.age%4!=0||b.at.distanceToSqr(client.gameRenderer.getMainCamera().position())>48*48)continue;
			var random=client.level.getRandom();
			boolean heal=b.spell.element().equals("healing");
			boolean field=b.spell==SpellDefinition.WATER_SPLASH||b.spell==SpellDefinition.ICICLE_FIELD;
			double radius=heal?.45:field?Math.min(6,b.age*.3):b.spell.areaDamage()?Math.min(2,b.age*.12):.2+b.age*.01;
			for(int i=0;i<(b.age==4?4:2)&&budget>0;i++,budget--) {
				double a=random.nextDouble()*Math.PI*2,r=radius*Math.sqrt(random.nextDouble());
				var point=b.at.add(Math.cos(a)*r,heal?b.age*.055:random.nextDouble()*.4,Math.sin(a)*r);
				client.level.addParticle(SpellParticles.burst(b.spell),point.x,point.y,point.z,Math.cos(a)*.015,heal?.02:.005,Math.sin(a)*.015);
			}
		}
	}
	private SpellVisualAnimations() { }
}
