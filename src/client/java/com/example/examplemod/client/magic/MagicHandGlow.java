package com.example.examplemod.client.magic;

import com.example.examplemod.magic.MagicAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.phys.Vec3;

/** Client particles follow the physical right hand; color and timing come from the server. */
public final class MagicHandGlow {
	public static void tick(Minecraft client) {
		if(client.level==null||client.isPaused())return;
		for(var player:client.level.players()) {
			if(player.isUsingItem()||player.getAttachedOrElse(MagicAttachments.CAST_TICKS,0)<=0)continue;
			int color=player.getAttachedOrElse(MagicAttachments.CAST_COLOR,0);if(color==0)continue;
			var forward=Vec3.directionFromRotation(0,player.getYRot());var right=new Vec3(forward.z,0,-forward.x);
			var hand=player.position().add(0,player.getBbHeight()*0.72,0).add(forward.scale(0.5)).add(right.scale(0.35));
			if(player==client.player&&client.options.getCameraType().isFirstPerson())hand=player.getEyePosition().add(player.getLookAngle().scale(0.55)).add(right.scale(0.28)).add(0,-0.25,0);
			for(int i=0;i<5;i++)client.level.addParticle(new DustParticleOptions(color,0.65F),hand.x+(Math.random()-0.5)*0.1,hand.y+(Math.random()-0.5)*0.1,hand.z+(Math.random()-0.5)*0.1,0,0,0);
		}
	}
	private MagicHandGlow() { }
}
