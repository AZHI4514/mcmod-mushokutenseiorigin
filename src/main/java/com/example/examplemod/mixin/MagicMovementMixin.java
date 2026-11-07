package com.example.examplemod.mixin;

import com.example.examplemod.magic.MagicEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Both sides apply the synced effect; attacks and camera remain available; bound entities cannot translate. */
@Mixin(Entity.class)
public abstract class MagicMovementMixin {
	@ModifyVariable(method="move",at=@At("HEAD"),argsOnly=true)
	private Vec3 mushoku$bound(Vec3 movement) {
		var entity=(Entity)(Object)this;
		if(entity instanceof LivingEntity living&&living.hasEffect(MagicEffects.WIND_BOUND)) {living.setDeltaMovement(Vec3.ZERO);living.resetFallDistance();return Vec3.ZERO;}
		return com.example.examplemod.magic.PhaseTwoRuntime.clipShieldMovement(entity,movement);
	}
}
