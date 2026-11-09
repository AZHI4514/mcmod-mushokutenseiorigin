package com.example.examplemod.client.magic;

import com.example.examplemod.magic.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

/** Dense random volumes, peripheral sparks and rank-dependent eddies around the physical hand. */
public final class MagicHandGlow {
	public static void tick(Minecraft client) {
		if(client.level==null||client.isPaused())return;
		int budget=SpellParticleDensity.HAND_BUDGET;
		var players=new java.util.ArrayList<>(client.level.players());
		// The local caster must retain detail when many other players are chanting.
		players.sort(java.util.Comparator.comparingDouble(p->client.player==null?0:p.distanceToSqr(client.player)));
		for(var player:players) {
			if(budget<=0)break;
			double distance=client.player==null?0:player.distanceToSqr(client.player);
			if(!player.isAlive() || player.isUsingItem() || distance>32*32)continue;
			int left=player.getAttachedOrElse(MagicAttachments.PREPARE_LEFT,0);
			int total=player.getAttachedOrElse(MagicAttachments.PREPARE_TOTAL,0);
			boolean charge=player==client.player && player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0)>0;
			boolean released=player.getAttachedOrElse(MagicAttachments.CAST_TICKS,0)>0 && player.getAttachedOrElse(MagicAttachments.CAST_COLOR,0)!=0;
			if(left<=0 && !charge && !released)continue;
			var spell=SpellDefinition.find(player.getAttachedOrElse(MagicAttachments.VISUAL_SPELL,""));
			if(spell==null)continue;
			var detail=SpellParticleDensity.of(spell.rank());
			var profile=SpellAtmosphereProfile.of(spell.id());
			int lod=distance>24*24?4:distance>12*12?2:1;
			var look=player.getLookAngle(); var forward=Vec3.directionFromRotation(0,player.getYRot());
			var right=new Vec3(-forward.z,0,forward.x);
			var center=player.position().add(0,player.getBbHeight()*0.72,0).add(look.scale(0.6)).add(right.scale(0.35));
			boolean firstPerson=player==client.player && client.options.getCameraType().isFirstPerson();
			if(firstPerson)center=player.getEyePosition().add(look.scale(0.65)).add(right.scale(0.28)).add(0,-0.25,0);
			float progress=total>0?1-Math.clamp((float)left/total,0,1):1;
			double radius=detail.radius()*(.85+.15*progress);
			var random=player.getRandom();
			float pulse=SpellParticleDensity.chantPulse(player.tickCount+player.getId()*7.31F);
			int gather=Math.max(1,Math.round(detail.gather()*pulse/lod)), sparks=(int)(detail.sparks()*pulse/lod);
			int haze=player.tickCount%6==0 && pulse>.75F?Math.max(1,detail.haze()/lod):0;
			for(int i=0;i<gather+sparks+haze && budget>0;i++) {
				boolean mote=i<gather, smoke=i>=gather+sparks;
				// A minority of points form the hot core; the others fill the outer volume.
				double spread=mote && i%4==0?radius*.35:smoke?radius*.85:radius;
				var o=SpellParticleDensity.volume(random.nextDouble(),random.nextDouble(),random.nextDouble(),spread);
				Vec3 at=center.add(o.x(),o.y()*.8+MagicCastMotion.lift(player.tickCount),o.z());
				// Keep near-camera motes out of the eye, without flattening their 3D distribution.
				if(firstPerson && at.distanceToSqr(player.getEyePosition())<.22*.22)continue;
				double swirl=mote?.035:.07, inward=mote?.035:-.018;
				double dx=-o.x()*inward-o.z()*swirl+(random.nextDouble()-.5)*.025;
				double dy=-o.y()*inward+(random.nextDouble()-.35)*.035;
				double dz=-o.z()*inward+o.x()*swirl+(random.nextDouble()-.5)*.025;
				// Wind/light magic gets fine drifting glints instead of opaque smoke.
				var type=smoke && profile.dusty()?SpellParticles.haze(spell):mote?SpellParticles.gather(spell):SpellParticles.accent(spell);
				client.level.addParticle(type,at.x,at.y,at.z,dx,dy,dz);
				budget--;
			}
		}
	}
	private MagicHandGlow() { }
}
