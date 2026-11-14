package com.example.examplemod.mixin;

import com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(LivingEntity.class)
abstract class MagicSummonMixin {
	@Inject(method="aiStep",at=@At("TAIL"))
	private void magic$companion(CallbackInfo info){if((Object)this instanceof Mob mob)SummonRuntimeAdapter.tick(mob);}
	@Inject(method="dropAllDeathLoot",at=@At("HEAD"),cancellable=true)
	private void magic$noSummonLoot(ServerLevel level,DamageSource source,CallbackInfo info){if(SummonRuntimeAdapter.summoned((LivingEntity)(Object)this)||com.example.examplemod.magic.MagicCores.core((LivingEntity)(Object)this))info.cancel();}
}
