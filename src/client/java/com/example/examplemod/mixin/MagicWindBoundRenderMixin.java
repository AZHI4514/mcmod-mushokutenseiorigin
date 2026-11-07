package com.example.examplemod.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import com.example.examplemod.magic.MagicEffects;
@Mixin(LivingEntityRenderer.class)
public abstract class MagicWindBoundRenderMixin {
	@Inject(method="extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",at=@At("TAIL"))
	private void mushoku$shiver(LivingEntity entity,LivingEntityRenderState state,float delta,CallbackInfo ci) {
		if(entity.hasEffect(MagicEffects.WIND_BOUND))state.isFullyFrozen=true;
	}
}
