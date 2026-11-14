package com.example.examplemod.mixin;

import com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetingConditions.class)
abstract class MagicTargetingMixin {
	@Inject(method="test",at=@At("HEAD"),cancellable=true)
	private void magic$limitedSight(ServerLevel level,LivingEntity attacker,LivingEntity target,CallbackInfoReturnable<Boolean> info){
		if(!SmokeFieldRuntimeAdapter.mayAcquire(level,attacker,target))info.setReturnValue(false);
	}
}
