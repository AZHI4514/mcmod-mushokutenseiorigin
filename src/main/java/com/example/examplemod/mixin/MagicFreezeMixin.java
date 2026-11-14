package com.example.examplemod.mixin;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter;

import com.example.examplemod.magic.MagicEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MagicFreezeMixin {
	@Unique private boolean mushoku$hadMagicFreeze;
	@Inject(method = "aiStep", at = @At("HEAD"))
	private void mushoku$freeze(CallbackInfo ci) {
		var entity = (LivingEntity) (Object) this;
		if (!entity.level().isClientSide()) {
			boolean active = entity.hasEffect(MagicEffects.FROZEN) && !entity.hasEffect(MagicEffects.WARMTH) && entity.canFreeze();
			// 原版效果可能在aiStep前已到期；清除魔法遗留值，避免再自然解冻数秒。
			if (mushoku$hadMagicFreeze && !active && !entity.isInPowderSnow) entity.setTicksFrozen(0);
			com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter.tickLiving(entity);
			com.example.examplemod.infrastructure.fabric.magic.HypnosisAdapter.tickLiving(entity);
			MagicEffects.beforeFreeze(entity);
			mushoku$hadMagicFreeze = active;
		}
	}
}
