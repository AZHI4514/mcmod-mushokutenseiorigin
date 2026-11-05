package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import com.example.examplemod.client.magic.MagicPoseState;
import com.example.examplemod.magic.MagicAttachments;

@Mixin(HumanoidMobRenderer.class)
public abstract class MagicExtractPoseMixin {
	@Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
	private static void mushoku$extract(LivingEntity entity, HumanoidRenderState state, float delta, ItemModelResolver resolver, CallbackInfo ci) {
		((MagicPoseState) state).mushoku$setCasting(entity instanceof Player && !entity.isUsingItem()
				&& entity.getAttachedOrElse(MagicAttachments.CAST_TICKS, 0) > 0);
	}
}
