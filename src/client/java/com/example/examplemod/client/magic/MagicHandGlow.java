package com.example.examplemod.client.magic;

import com.example.examplemod.magic.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

/** Bounded pixel motes gathering around the physical right hand. */
public final class MagicHandGlow {
	public static void tick(Minecraft client) {
		if(client.level==null||client.isPaused())return;
		for(var player:client.level.players()) {
			if(!player.isAlive() || player.isUsingItem() || client.player!=null && player.distanceToSqr(client.player)>32*32)continue;
			int left=player.getAttachedOrElse(MagicAttachments.PREPARE_LEFT,0);
			int total=player.getAttachedOrElse(MagicAttachments.PREPARE_TOTAL,0);
			boolean charge=player==client.player && player.getAttachedOrElse(MagicAttachments.CHARGE_MANA,0)>0;
			boolean released=player.getAttachedOrElse(MagicAttachments.CAST_TICKS,0)>0 && player.getAttachedOrElse(MagicAttachments.CAST_COLOR,0)!=0;
			if(left<=0 && !charge && !released)continue;
			if(player.tickCount%2!=0)continue;
			var spell=SpellDefinition.find(player.getAttachedOrElse(MagicAttachments.VISUAL_SPELL,""));
			if(spell==null)continue;
			var look=player.getLookAngle(); var forward=Vec3.directionFromRotation(0,player.getYRot());
			var right=new Vec3(-forward.z,0,forward.x);
			var center=player.position().add(0,player.getBbHeight()*0.72,0).add(look.scale(0.6)).add(right.scale(0.35));
			if(player==client.player && client.options.getCameraType().isFirstPerson())center=player.getEyePosition().add(look.scale(0.65)).add(right.scale(0.28)).add(0,-0.25,0);
			float progress=total>0?1-Math.clamp((float)left/total,0,1):1;
			double radius=0.28-0.06*progress;
			var random=player.getRandom();
			for(int mote=0;mote<2;mote++) {
			var offset=new Vec3((random.nextDouble()-.5)*radius,(random.nextDouble()-.5)*radius,(random.nextDouble()-.5)*radius);
			var at=center.add(offset);
			client.level.addParticle(SpellParticles.gather(spell),at.x,at.y,at.z,-offset.x*.12,-offset.y*.12,-offset.z*.12);
			}
		}
	}
	private MagicHandGlow() { }
}
