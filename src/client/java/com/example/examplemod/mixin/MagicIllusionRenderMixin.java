package com.example.examplemod.mixin;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(EntityRenderDispatcher.class)
abstract class MagicIllusionRenderMixin {
	@Inject(method="shouldRender",at=@At("HEAD"),cancellable=true)
	private void magic$projection(Entity entity,Frustum frustum,double x,double y,double z,CallbackInfoReturnable<Boolean> info){if(com.example.examplemod.client.magic.IllusionProjection.hidden(entity))info.setReturnValue(false);}
}
