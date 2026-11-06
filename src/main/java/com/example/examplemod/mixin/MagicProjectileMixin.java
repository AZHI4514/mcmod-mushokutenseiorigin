package com.example.examplemod.mixin;

import com.example.examplemod.magic.MagicAreas;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 必须在整个弹体tick前拦截，取消父类tick不能阻止子类继续命中。 */
@Mixin(ServerLevel.class)
public abstract class MagicProjectileMixin {
	@Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
	private void mushoku$waterShield(Entity entity, CallbackInfo ci) {
		if (entity instanceof Projectile projectile && MagicAreas.intercept(projectile)) ci.cancel();
	}
}
