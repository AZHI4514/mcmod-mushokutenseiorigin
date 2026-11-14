package com.example.examplemod.mixin;

import com.example.examplemod.magic.MagicAttachments;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** View-distance transition never increases the vanilla underwater/lava/darkness distance. */
@Mixin(FogRenderer.class)
abstract class MagicFogMixin {
	@Unique private float magic$strength;
	@Unique private double magic$last = -1;
	@Unique private ClientLevel magic$level;

	@Inject(method="setupFog",at=@At("RETURN"))
	private void magic$localMist(Camera camera,int distance,DeltaTracker delta,float darken,ClientLevel level,CallbackInfoReturnable<FogData> info){
		if(level!=magic$level){magic$strength=0;magic$last=-1;magic$level=level;}
		double now=level.getGameTime()+delta.getGameTimeDeltaPartialTick(false);
		double elapsed=magic$last<0?0:Math.clamp(now-magic$last,0,2);
		magic$last=now;
		var viewer=camera.entity();
		int left=viewer==null?0:viewer.getAttachedOrElse(MagicAttachments.FOG_LEFT,0);
		int linger=viewer==null?10:viewer.getAttachedOrElse(MagicAttachments.FOG_LINGER,10);
		float desired=left<=0?0:Math.min(1,(float)left/Math.max(1,linger));
		magic$strength += Math.clamp(desired-magic$strength,(float)-elapsed*.1F,(float)elapsed*.1F);
		if(magic$strength<.001F||viewer==null)return;
		float end=viewer.getAttachedOrElse(MagicAttachments.FOG_RANGE,256);
		var fog=info.getReturnValue();
		float effective=net.minecraft.util.Mth.lerp(magic$strength,Math.min(fog.environmentalEnd,fog.renderDistanceEnd),end);
		fog.environmentalEnd=Math.min(fog.environmentalEnd,effective);
		fog.renderDistanceEnd=Math.min(fog.renderDistanceEnd,effective);
		fog.environmentalStart=Math.min(fog.environmentalStart,effective*.12F);
		fog.renderDistanceStart=Math.min(fog.renderDistanceStart,effective*.12F);
		fog.skyEnd=Math.min(fog.skyEnd,effective);
		fog.cloudEnd=Math.min(fog.cloudEnd,effective);
		int color=viewer.getAttachedOrElse(MagicAttachments.FOG_COLOR,0xB7C9D1);
		fog.color.lerp(new org.joml.Vector4f((color>>16&255)/255F,(color>>8&255)/255F,(color&255)/255F,1),magic$strength*.8F);
	}
}
