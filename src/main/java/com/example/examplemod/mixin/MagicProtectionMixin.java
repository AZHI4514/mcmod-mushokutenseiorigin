package com.example.examplemod.mixin;
import com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;
import com.example.examplemod.magic.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(LivingEntity.class)
abstract class MagicProtectionMixin {
	@Inject(method="getDamageAfterMagicAbsorb",at=@At("RETURN"),cancellable=true)
	private void magic$protection(DamageSource source,float input,CallbackInfoReturnable<Float> info){
		var entity=(LivingEntity)(Object)this;float amount=info.getReturnValue();
		float reduced=PoweredBarrierRuntimeAdapter.auraDamage(entity,source,amount);
		if(entity instanceof ServerPlayer player)reduced=SummonRuntimeAdapter.protect(player,source,reduced);
		info.setReturnValue(Math.max(amount*.4F,reduced));
	}
}
